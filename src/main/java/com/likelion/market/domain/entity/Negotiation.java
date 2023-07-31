package com.likelion.market.domain.entity;

import com.likelion.market.domain.dto.negotiation.RequestNegotiationDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Negotiation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private SalesItem salesItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Integer suggestedPrice;

    private String status;


    public static Negotiation fromDto(RequestNegotiationDto requestNegotiationDto) {
        Negotiation negotiation = new Negotiation();
        negotiation.setSuggestedPrice(requestNegotiationDto.getSuggestedPrice());
        negotiation.setSalesItem(requestNegotiationDto.getSalesItem());
        negotiation.setStatus("제안");

        return negotiation;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUser(User user) {
        this.user = user;
        user.getNegotiations().add(this);
    }

    public void setSalesItem(SalesItem salesItem) {
        this.salesItem = salesItem;
        salesItem.getNegotiations().add(this);
    }
}
