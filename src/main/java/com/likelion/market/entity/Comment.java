package com.likelion.market.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private SalesItem item;

    @NotNull
    private String writer;

    @NotNull
    private String password;

    @NotNull
    private String content;

    private String reply;
}
