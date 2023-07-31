package com.likelion.market.domain.dto.comment;

import com.likelion.market.domain.entity.Comment;
import lombok.Data;

@Data
public class RequestCommentDto {
    private Long id;
    private Long itemId;
    private String username;
    private String password;
    private String content;
    private String reply;
}
