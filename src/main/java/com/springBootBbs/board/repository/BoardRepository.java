package com.springBootBbs.board.repository;

import com.springBootBbs.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// JpaRepository를 사용. Board의 @Id로 지정한 id의 타입인 Integer를 상속받음 
@Repository
public interface BoardRepository extends JpaRepository<Board, Integer > {

    Page<Board> findByTitleContaining(String searchKeyword, Pageable pageable);
}
