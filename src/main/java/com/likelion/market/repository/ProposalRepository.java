package com.likelion.market.repository;

import com.likelion.market.entity.Proposal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProposalRepository extends JpaRepository<Proposal, Long> {
    Page<Proposal> findByWriterAndPassword(String writer, String password, Pageable pageable);
    List<Proposal> findByIdNot(Long id);
}
