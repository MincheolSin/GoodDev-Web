package io.good.gooddev_web.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.good.gooddev_web.board.dto.BoardDTO;
import io.good.gooddev_web.board.service.BoardService;
import io.good.gooddev_web.search.dto.PageRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequiredArgsConstructor
public class SearchController {
    private final BoardService boardService;

    @GetMapping("/api/search")
    public ResponseEntity<Map<String,Object>> serachPost(@RequestParam(required = false) String keyword){
        Map<String,Object> response = new HashMap<>();
        PageRequestDTO pageRequestDTO = new PageRequestDTO();
        pageRequestDTO.setKeyword(keyword);
        pageRequestDTO.setSize(4);
        HashMap<String,List<BoardDTO>> totalMap = boardService.getTotalList(pageRequestDTO);
		response.put("totalMap",totalMap);
        return ResponseEntity.ok(response);
    }
}
