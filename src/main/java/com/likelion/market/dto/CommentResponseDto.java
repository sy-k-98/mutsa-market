package com.likelion.market.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.likelion.market.entity.Comment;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentResponseDto {
    private Long id;
    private String content;
    private String reply;

    public static CommentResponseDto fromEntity(Comment entity) {
        CommentResponseDto dto = new CommentResponseDto();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setReply(entity.getReply());
        return dto;
    }
}
