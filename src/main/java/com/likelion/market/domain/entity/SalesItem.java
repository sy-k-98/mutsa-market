package com.likelion.market.domain.entity;

import com.likelion.market.domain.dto.salesItem.RequestSalesItemDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "sales_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SalesItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    private Integer minPriceWanted;

    private String status;

    private String writer;

    private String password;

    public static SalesItem fromDto(RequestSalesItemDto requestSalesItemDto) {
        SalesItem salesItem = new SalesItem();
        salesItem.setTitle(requestSalesItemDto.getTitle());
        salesItem.setDescription(requestSalesItemDto.getDescription());
        salesItem.setImageUrl(requestSalesItemDto.getImageUrl());
        salesItem.setMinPriceWanted(requestSalesItemDto.getMinPriceWanted());
        salesItem.setStatus("판매중");
        salesItem.setWriter(requestSalesItemDto.getWriter());
        salesItem.setPassword(requestSalesItemDto.getPassword());

        return salesItem;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
