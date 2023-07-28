package com.likelion.market.domain.dto.salesItem;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.likelion.market.domain.entity.SalesItem;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestSalesItemDto {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private Integer minPriceWanted;
    private String status;
    private String writer;
    private String password;

}
