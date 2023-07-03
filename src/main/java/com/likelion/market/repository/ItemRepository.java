package com.likelion.market.repository;

import com.likelion.market.entity.SalesItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<SalesItem, Long> {
}
