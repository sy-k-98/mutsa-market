package com.likelion.market.repository;

import com.likelion.market.domain.entity.Negotiation;
import com.likelion.market.domain.entity.SalesItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NegotiationRepository extends JpaRepository<Negotiation, Long> {
    Page<Negotiation> findAllBySalesItem(SalesItem salesItem, Pageable pageable);
    Page<Negotiation> findAllBySalesItemIdAndUserId(Long itemId, Long userId, Pageable pageable);

    List<Negotiation> findAllBySalesItem(SalesItem salesItem);
}
