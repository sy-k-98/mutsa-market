package com.likelion.market.repository;

import com.likelion.market.domain.entity.Negotiation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NegotiationRepository extends JpaRepository<Negotiation, Long> {
    Page<Negotiation> findByWriterAndPassword(String writer, String password, Pageable pageable);
    List<Negotiation> findByIdNot(Long id);
}
