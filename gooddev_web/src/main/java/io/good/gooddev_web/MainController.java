package io.good.gooddev_web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.good.gooddev_web.board.dto.BoardDTO;
import io.good.gooddev_web.board.service.BoardService;
import io.good.gooddev_web.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {
    private final MemberService memberService;
    private final BoardService boardService;

    @GetMapping("/api/main")
    public ResponseEntity<Map<String, Object>> main() {
        Map<String, Object> response = new HashMap<>();
        List<BoardDTO> noticeList = boardService.getNoticeList();
        List<BoardDTO> galleryList  = boardService.getGalleryList();
        Map<String,List<BoardDTO>> mainMap = boardService.getMainList();
        List<BoardDTO> topTenList = boardService.topTenList();
        response.put("noticeList",noticeList);
        response.put("mainMap", mainMap);
        response.put("galleryList",galleryList);
        response.put("topTenList", topTenList);
        return ResponseEntity.ok(response);
    }
}
