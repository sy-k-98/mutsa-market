package com.likelion.market.dto;

import com.likelion.market.entity.Proposal;
import lombok.Data;

@Data
public class ProposalResponseDto {
    private Long id;
    private Integer suggestedPrice;
    private String status;

    public static ProposalResponseDto fromEntity(Proposal entity) {
        ProposalResponseDto dto = new ProposalResponseDto();
        dto.setId(entity.getId());
        dto.setSuggestedPrice(entity.getSuggestedPrice());
        dto.setStatus(entity.getStatus());
        return dto;
    }
}
