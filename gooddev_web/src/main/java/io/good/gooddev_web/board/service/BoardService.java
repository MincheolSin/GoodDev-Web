package io.good.gooddev_web.board.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import io.good.gooddev_web.board.dao.BoardDAO;
import io.good.gooddev_web.board.dao.BoardFileDAO;
import io.good.gooddev_web.board.dto.BoardDTO;
import io.good.gooddev_web.board.dto.BoardFileDTO;
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
        List<Integer> totalCategory= boardDAO.getTotalCategory();
        for(int category : totalCategory){
            pageRequestDTO.setCategory_no(String.valueOf(category));
            List <BoardDTO> boardList = boardDAO.getList(pageRequestDTO).stream().map(board -> mapper.map(board, BoardDTO.class)).collect(Collectors.toList());
            if (!boardList.isEmpty()) {
                String categoryName = boardList.get(0).getCategory_name();
                map.put(categoryName, boardList);
            }
        }
        return map;
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
            return boardVO.getBno();
        }catch(RuntimeException e){
            throw new RuntimeException("Transaction failed", e);
        }
    }
    
    public void addLike(final int bno, String mid) {
        if (!boardDAO.existsLike(mid, bno)) {
            // 좋아요가 없으면 추가 (DELETEYN 'N' 설정 및 like_board 1)
            boardDAO.insertLike(mid, bno, 1);
            boardDAO.updateLikeCount(bno, 1);
        }
    }

    public void cancelLike(int bno, String mid) {
        // 좋아요가 있으면 취소 (DELETEYN 'Y' 설정 및 like_board 0)
        boardDAO.updateDeleteYN(mid, bno, 'Y');
        boardDAO.updateLikeCount(bno, -1);
    }

    public boolean hasUserLiked(int bno, String mid) {
        return boardDAO.existsLike(mid, bno);
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
		//return new PageResponseDTO(pageRequestDTO, getList, boardDAO.getTotalCount(pageRequestDTO));
        return new PageResponseDTO<BoardDTO>(pageRequestDTO, getList, boardDAO.getTotalCount(pageRequestDTO));
	}

}
