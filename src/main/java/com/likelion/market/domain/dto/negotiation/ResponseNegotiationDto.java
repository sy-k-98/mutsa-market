package com.likelion.market.domain.dto.negotiation;

import com.likelion.market.domain.entity.Negotiation;
import lombok.Data;

@Data
public class ResponseNegotiationDto {
    private Long id;
    private Integer suggestedPrice;
    private String status;

    public static ResponseNegotiationDto fromEntity(Negotiation entity) {
        ResponseNegotiationDto dto = new ResponseNegotiationDto();
        dto.setId(entity.getId());
        dto.setSuggestedPrice(entity.getSuggestedPrice());
        dto.setStatus(entity.getStatus());
        return dto;
    }
}
