package com.likelion.market.repository;

import com.likelion.market.domain.entity.Comment;
import com.likelion.market.domain.entity.SalesItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllBySalesItem(SalesItem salesItem, Pageable pageable);
}
