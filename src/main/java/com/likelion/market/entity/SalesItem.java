package com.likelion.market.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "sales_item")
public class SalesItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @NotNull
    private Integer minPriceWanted;

    private String status;

    @NotNull
    private String writer;

    @NotNull
    private String password;
}
