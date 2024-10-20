package io.good.gooddev_web.board.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import io.good.gooddev_web.board.dao.BoardDAO;
import io.good.gooddev_web.board.dao.BoardFileDAO;
import io.good.gooddev_web.board.dto.BoardCategoryDTO;
import io.good.gooddev_web.board.dto.BoardDTO;
import io.good.gooddev_web.board.dto.BoardFileDTO;
import io.good.gooddev_web.board.vo.BoardCategoryVO;
import io.good.gooddev_web.board.vo.BoardFileVO;
import io.good.gooddev_web.board.vo.BoardVO;
import io.good.gooddev_web.search.dto.PageRequestDTO;
import io.good.gooddev_web.search.dto.PageResponseDTO;
import io.good.gooddev_web.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    
    @Qualifier("uploadPath")
    private final String uploadPath;
    
    private final BoardDAO boardDAO;
    private final MapperUtil mapper;
    private final BoardFileDAO boardFileDAO;

    public HashMap<String,List<BoardDTO>> getTotalList(PageRequestDTO pageRequestDTO) {
        HashMap<String,List<BoardDTO>> map = new HashMap<>();
        List<BoardCategoryVO> totalCategory= boardDAO.getTotalCategory();
        for(BoardCategoryVO category : totalCategory){
            pageRequestDTO.setCategory_no(String.valueOf(category.getCategory_no()));
            List <BoardDTO> boardList = boardDAO.getList(pageRequestDTO).stream().map(board ->
            mapper.map(board, BoardDTO.class)).collect(Collectors.toList());
            if (!boardList.isEmpty()) {
                map.put(category.getCategory_name(), boardList);
            }
        }
        return map;
    }
    
    public List<BoardCategoryDTO> getTotalCategory(){
        return boardDAO.getTotalCategory().stream().map(category -> mapper.map(category, BoardCategoryDTO.class)).collect(Collectors.toList());
    }


    public PageResponseDTO<BoardDTO> getList(PageRequestDTO pageRequestDTO) {
		List<BoardDTO> getList = boardDAO.getList(pageRequestDTO).stream().map(board -> mapper.map(board, BoardDTO.class)).collect(Collectors.toList());
		//return new PageResponseDTO(pageRequestDTO, getList, boardDAO.getTotalCount(pageRequestDTO));
        return new PageResponseDTO<BoardDTO>(pageRequestDTO, getList, boardDAO.getTotalCount(pageRequestDTO));
	}
    
	public int remove(long mid) {
		return boardDAO.remove(mid);
	}
    
    public BoardDTO getRead(int bno) {
    	BoardVO board = boardDAO.getRead(bno).orElse(null);
        BoardDTO boardDTO = board!= null ? mapper.map(board, BoardDTO.class) : null;
        if(boardDTO!=null){
            boardDTO.setBoardFileDTOList(boardFileDAO.getList(bno).stream().map(file->mapper.map(file, BoardFileDTO.class)).collect(Collectors.toList()));
        }
    	return boardDTO;
    }

    public void viewCount(int num) {
    	boardDAO.viewCount(num);
    }

    @Transactional
    public int insert(final BoardVO boardVO) {
        try{
            final int result = boardDAO.insert(boardVO);
            if (boardVO.getFile() !=null){
                for (MultipartFile file : boardVO.getFile()) {
                    if (file.getSize() != 0) {
                        //유일한 파일명 생성
                        String fid = UUID.randomUUID().toString();
                        //첨부파일 저장
                        try( BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(uploadPath + fid));
                            BufferedInputStream bis = new BufferedInputStream(file.getInputStream());
                        ){
                            bis.transferTo(bos);
                        }catch (IOException e) {
                            throw new RuntimeException("File insert failed");
                        }
                        BoardFileVO boardFileVO = new BoardFileVO();
                        
                        boardFileVO.setFid(fid);
                        boardFileVO.setBno((int)boardVO.getBno());
                        boardFileVO.setFile_name(file.getOriginalFilename());
                        boardFileVO.setFile_type(file.getContentType());
                        boardFileVO.setFile_size((int)file.getSize());
                        final int resultFileInsert = boardFileDAO.insert(boardFileVO);
                        if(resultFileInsert !=1 || result == -1) throw new RuntimeException("File insert failed");
                    }
                    
                }
                return result;
            }
            else return result;
        }catch(RuntimeException e){
            throw new RuntimeException("Transaction failed", e);
        }
        
    }
    
    public void deleteFile(String fileName) {
        String filePath = uploadPath+fileName; // 삭제하려는 파일의 경로
        File fileToDelete = new File(filePath);
        if (fileToDelete.exists()) {
                fileToDelete.delete();
        }
    }
    public void addLike(final int bno, String mid) {
        if (!boardDAO.existsLike(mid, bno)) {
            // 좋아요가 없으면 추가 (DELETEYN 'N' 설정 및 like_board 1)
            boardDAO.insertLike(mid, bno, 1);
            boardDAO.updateLikeCount(bno, 1);
        }
    }
    
    public void addHate(final int bno, String mid) {
    	if (!boardDAO.existsHate(mid, bno)) {
    		boardDAO.insertHate(mid, bno, 0);
    		boardDAO.updateHateCount(bno, 1);
    	}
    }

    public void cancelLike(int bno, String mid) {
        // 좋아요가 있으면 취소 (DELETEYN 'Y' 설정 및 like_board null)
        boardDAO.updateDeleteYN(mid, bno, 'Y');
        boardDAO.updateLikeCount(bno, -1);
    }
    
    public void cancelHate(int bno, String mid) {
    	boardDAO.updateDeleteYN(mid, bno, 'Y');
    	boardDAO.updateHateCount(bno, -1);
    }

    public boolean hasUserLiked(int bno, String mid) {
        return boardDAO.existsLike(mid, bno);
    }
    
    public boolean hasUserHated(int bno, String mid) {
    	return boardDAO.existsHate(mid, bno);
    }
    
    public int getLikeCount(int bno) {
        return boardDAO.getLikeCount(bno);
    }

    public int getHateCount(int bno) {
        return boardDAO.getHateCount(bno);
    }

    public List<BoardDTO> topTenList() {
        return boardDAO.topTenList().stream()
                       .map(board -> mapper.map(board, BoardDTO.class))
                       .collect(Collectors.toList());
        
    }

    public BoardFileDTO getBoardFile(String fid) {
		BoardFileVO todoFile = boardFileDAO.getRead(fid).orElse(null);
		return todoFile != null ? mapper.map(todoFile, BoardFileDTO.class) : null;
	}

    public PageResponseDTO<BoardDTO> getGalleryList(PageRequestDTO pageRequestDTO) {
		List<BoardDTO> getList = boardDAO.getList(pageRequestDTO).stream().map(board -> mapper.map(board, BoardDTO.class)).collect(Collectors.toList());
        if(getList!=null){
            for(BoardDTO boardDTO : getList){
                boardDTO.setBoardFileDTOList(boardFileDAO.getList(boardDTO.getBno()).stream().map(file->mapper.map(file, BoardFileDTO.class)).collect(Collectors.toList()));
            }
        }
        return new PageResponseDTO<BoardDTO>(pageRequestDTO, getList, boardDAO.getTotalCount(pageRequestDTO));
	}

    @Transactional
    public boolean update(final BoardVO boardVO) {
        try{
            boardFileDAO.delete(boardVO.getBno());
            return boardDAO.update(boardVO)==1;
        }catch(RuntimeException e){
            throw new RuntimeException("Transaction failed", e);
    }
    }
    
    @Transactional
    public boolean delete(int bno, String board_password) {
        try{
            int deleteBoard = boardDAO.delete(bno, board_password);

            boardFileDAO.delete(bno);
            
            return deleteBoard > 0;
        }catch(RuntimeException e){
            throw new RuntimeException("Transaction failed", e);
        }
    }
    
    public int deleteBoard(int bno) {
    	return boardDAO.deleteBoard(bno);
    }

    public Map<String, List<BoardDTO>> getMainList() {
        Map<String, List<BoardDTO>> map= new HashMap<>();
        List<BoardCategoryVO> totalCategory= boardDAO.getTotalCategory();
        for(BoardCategoryVO category : totalCategory){
            if(category.getCategory_no() != 10 && category.getCategory_no() != 50){
                PageRequestDTO pageRequestDTO = new PageRequestDTO();
                pageRequestDTO.setCategory_no(String.valueOf(category.getCategory_no()));
                pageRequestDTO.setSize(4);
                List <BoardDTO> boardList = boardDAO.getList(pageRequestDTO).stream().map(board -> mapper.map(board, BoardDTO.class)).collect(Collectors.toList());
                if (!boardList.isEmpty()) {
                    map.put(category.getCategory_name(), boardList);
                }
            }
        }
        return map;
    }

    public List<BoardDTO> getNoticeList() {
        PageRequestDTO pageRequestDTO = new PageRequestDTO();
        pageRequestDTO.setCategory_no("10");
        pageRequestDTO.setSize(4);
        return boardDAO.getList(pageRequestDTO).stream().map(board -> mapper.map(board, BoardDTO.class)).collect(Collectors.toList());
    }

    public List<BoardDTO> getGalleryList() {
        PageRequestDTO pageRequestDTO = new PageRequestDTO();
        pageRequestDTO.setCategory_no("50");
        pageRequestDTO.setSize(4);
        List<BoardDTO> getList = boardDAO.getList(pageRequestDTO).stream().map(board -> mapper.map(board, BoardDTO.class)).collect(Collectors.toList());
        if(getList!=null){
            for(BoardDTO boardDTO : getList){
                boardDTO.setBoardFileDTOList(boardFileDAO.getList(boardDTO.getBno()).stream().map(file->mapper.map(file, BoardFileDTO.class)).collect(Collectors.toList()));
            }
        }
        return getList;
    }

}
