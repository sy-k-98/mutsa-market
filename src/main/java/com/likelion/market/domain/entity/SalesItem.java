package com.likelion.market.domain.entity;

import com.likelion.market.domain.dto.salesItem.RequestSalesItemDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "salesItem", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "salesItem", cascade = CascadeType.ALL)
    private List<Negotiation> negotiations = new ArrayList<>();

    public static SalesItem fromDto(RequestSalesItemDto requestSalesItemDto) {
        SalesItem salesItem = new SalesItem();
        salesItem.setTitle(requestSalesItemDto.getTitle());
        salesItem.setDescription(requestSalesItemDto.getDescription());
        salesItem.setImageUrl(requestSalesItemDto.getImageUrl());
        salesItem.setMinPriceWanted(requestSalesItemDto.getMinPriceWanted());
        salesItem.setStatus("판매중");

        return salesItem;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUser(User user) {
        this.user = user;
        user.getSalesItems().add(this);
    }

    public void updateInfo(RequestSalesItemDto dto) {
        this.title = dto.getTitle();
        this.description = dto.getDescription();
        this.minPriceWanted = dto.getMinPriceWanted();
    }

    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
