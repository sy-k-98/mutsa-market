package com.likelion.market.dto;

import com.likelion.market.entity.Proposal;
import lombok.Data;

@Data
public class ProposalDto {
    private Long id;
    private Long itemId;
    private Integer suggestedPrice;
    private String status;
    private String writer;
    private String password;

    public static ProposalDto fromEntity(Proposal entity) {
        ProposalDto dto = new ProposalDto();
        dto.setId(entity.getId());
        dto.setItemId(entity.getItem().getId());
        dto.setSuggestedPrice(entity.getSuggestedPrice());
        dto.setWriter(entity.getWriter());
        dto.setPassword(entity.getPassword());
        return dto;
    }

}
