package com.likelion.market.domain.dto.salesItem;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.likelion.market.domain.entity.SalesItem;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseSalesItemsDto {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private Integer minPriceWanted;
    private String status;

    public static ResponseSalesItemsDto fromEntity(SalesItem entity) {
        ResponseSalesItemsDto dto = new ResponseSalesItemsDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setImageUrl(entity.getImageUrl());
        dto.setMinPriceWanted(entity.getMinPriceWanted());
        dto.setStatus(entity.getStatus());
        return dto;
    }
}
