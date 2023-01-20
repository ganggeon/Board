package com.springBootBbs.board.controller;

import com.springBootBbs.board.entity.Board;
import com.springBootBbs.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;


    @GetMapping("/board/write") //localhost:8090/board/write
    public String boardWriteForm() {

        return "BoardWrite";
    }


    @PostMapping("/board/writePro") // html로부터 입력받아 board.java로 넘겨줌
    public String boardWritePro(Board board, Model model, MultipartFile file) throws Exception {

        boardService.boardWrite(board, file);

        model.addAttribute("message", "글 작성이 완료되었습니다");
        model.addAttribute("searchUrl", "/board/list");

        return "message";
      //  return "redirect:/board/list";
    }

    @GetMapping("/board/list")
    public String boardList(Model model,
                            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            String searchKeyword) {

        Page<Board> list = null;

        if(searchKeyword == null) {
             list = boardService.boardList(pageable); // 검색키워드가 들어오지 않으면
        }else{
             list = boardService.boardSearchList(searchKeyword, pageable); // 검색 키워드가 들어왔으면
        }

        int nowPage = list.getPageable().getPageNumber() + 1; // 페이지가 0부터 시작하기 때문에 1부터 보여주기 위해 + 1
        int startPage = Math.max(nowPage - 4, 1); // 페이지가 음수일 경우 1페이지를 반환
        int endPage = Math.min(nowPage + 5, list.getTotalPages()); // 페이지가 최대 페이지를 넘어갈 시 list.getTotalPages()를 반환

        model.addAttribute("list", list); //반환된 boardlist를 list라는 이름으로 BoardList.html로 넘김
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "BoardList";
    }

    @GetMapping("/board/view") //localhost:8090/board/view?id=1 >>> 1이 id에 들어가고 id에 해당하는 글을 get방식으로 불러옴.
    public String boardView(Model model, Integer id) {

        model.addAttribute("board", boardService.boardView(id));
        return "BoardView";
    }

    @GetMapping("/board/delete")
    public String boardDelete(Integer id, Model model) {

        boardService.boardDelete(id);

        model.addAttribute("message", "글 삭제가 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");
        return "message";
    }

    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id, Model model) {

        model.addAttribute("board", boardService.boardView(id));
        return  "BoardModify";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id, Board board, Model model, MultipartFile file) throws Exception {

        Board boardTemp = boardService.boardView(id);
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());

        boardService.boardWrite(boardTemp, file);

        model.addAttribute("message", "글 수정이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");
        return "message";

    }

}