package com.likelion.market.domain.dto.negotiation;

import com.likelion.market.domain.entity.SalesItem;
import lombok.Data;

@Data
public class RequestNegotiationDto {
    private Long id;
    private SalesItem salesItem;
    private Integer suggestedPrice;
    private String status;
    private String username;
    private String password;
}
