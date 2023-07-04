package com.likelion.market.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "negotiation")
public class Proposal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private SalesItem item;

    @NotNull
    private Integer suggestedPrice;

    private String status;

    @NotNull
    private String writer;

    @NotNull
    private String password;
}
