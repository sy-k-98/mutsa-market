package com.likelion.market.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.likelion.market.entity.SalesItem;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemWithoutIDResponseDto {
    private String title;
    private String description;
    private String imageUrl;
    private Integer minPriceWanted;
    private String status;

    public static ItemWithoutIDResponseDto fromEntity(SalesItem entity) {
        ItemWithoutIDResponseDto dto = new ItemWithoutIDResponseDto();
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setImageUrl(entity.getImageUrl());
        dto.setMinPriceWanted(entity.getMinPriceWanted());
        dto.setStatus(entity.getStatus());
        return dto;
    }
}
