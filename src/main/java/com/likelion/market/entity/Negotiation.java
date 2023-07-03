package com.likelion.market.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Negotiation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private SalesItem item;

    private Integer suggestedPrice;

    private String status;

    private String writer;

    private String password;
}
