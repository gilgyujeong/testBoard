package com.sparta.board.repository;

import com.sparta.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
     @Query("select b from Board b left join fetch b.comments cs order by b.createdAt desc, cs.createdAt desc")
     List<Board> findAllByOrderByCreatedAtDesc();

     @Query("select b from Board b left join fetch b.comments cs where b.id=:board_id order by b.createdAt desc, cs.createdAt desc")
     Optional<Board> findBoardJoin(@Param("board_id")Long id);

}
