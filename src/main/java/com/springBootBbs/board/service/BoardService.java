package com.springBootBbs.board.service;

import com.springBootBbs.board.entity.Board;
import com.springBootBbs.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {

    @Autowired // 자동 주입
    private BoardRepository boardRepository;

    //글 작성 처리
    public void boardWrite(Board board, MultipartFile file) throws Exception{
    
        //파일 업로드 저장 경로 지정
        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
        //
        //파일명에 붙일 랜덤 식별자
        UUID uuid = UUID.randomUUID();
        //파일명 생성
        String fileName = uuid + "_" + file.getOriginalFilename();
        //saveFile 객체 생성해서 경로와 파일명을 받기
        File saveFile = new File(projectPath, fileName);
        file.transferTo(saveFile);

        board.setFilename(fileName);
        board.setFilepath("/files/" + fileName);

        boardRepository.save(board);
    }

    //게시글 리스트 처리
    public Page<Board> boardList(Pageable pageable) {

        return boardRepository.findAll(pageable);
    }

    //특정 게시글 불러오기
    public Board boardView(Integer id) {

        return boardRepository.findById(id).get();
    }

    //검색 기능
    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable) {

        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }

    //특정 게시글 삭제
    public void boardDelete(Integer id) {

        boardRepository.deleteById(id);
    }
}
