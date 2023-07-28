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

    @ManyToOne
    @JoinColumn(name = "item_id")
    private SalesItem salesItem;

    private Integer suggestedPrice;

    private String status;

    private String writer;

    private String password;

    public static Negotiation fromDto(RequestNegotiationDto requestNegotiationDto) {
        Negotiation negotiation = new Negotiation();
        negotiation.setSuggestedPrice(requestNegotiationDto.getSuggestedPrice());
        negotiation.setSalesItem(requestNegotiationDto.getSalesItem());
        negotiation.setStatus("제안");
        negotiation.setWriter(requestNegotiationDto.getWriter());
        negotiation.setPassword(requestNegotiationDto.getPassword());

        return negotiation;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
