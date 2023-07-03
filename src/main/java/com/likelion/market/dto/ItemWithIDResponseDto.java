package com.likelion.market.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.likelion.market.entity.SalesItem;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemWithIDResponseDto {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private Integer minPriceWanted;
    private String status;

    public static ItemWithIDResponseDto fromEntity(SalesItem entity) {
        ItemWithIDResponseDto dto = new ItemWithIDResponseDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setImageUrl(entity.getImageUrl());
        dto.setMinPriceWanted(entity.getMinPriceWanted());
        dto.setStatus(entity.getStatus());
        return dto;
    }
}
