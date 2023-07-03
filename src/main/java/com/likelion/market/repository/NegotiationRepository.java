package com.likelion.market.repository;

import com.likelion.market.entity.Negotiation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NegotiationRepository extends JpaRepository<Negotiation, Long> {
}
