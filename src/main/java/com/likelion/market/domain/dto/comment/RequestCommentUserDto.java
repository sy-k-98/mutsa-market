package com.likelion.market.domain.dto.comment;

import lombok.Data;

@Data
public class RequestCommentUserDto {
    private String writer;
    private String password;
}
