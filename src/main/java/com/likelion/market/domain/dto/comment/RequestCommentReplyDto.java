package com.likelion.market.domain.dto.comment;

import lombok.Data;

@Data
public class RequestCommentReplyDto {
    private String username;
    private String password;
    private String reply;
}
