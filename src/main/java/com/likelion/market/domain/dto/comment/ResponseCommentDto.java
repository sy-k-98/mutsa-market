package com.likelion.market.domain.dto.comment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.likelion.market.domain.entity.Comment;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseCommentDto {
    private Long id;
    private String content;
    private String reply;

    public static ResponseCommentDto fromEntity(Comment comment) {
        ResponseCommentDto dto = new ResponseCommentDto();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setReply(comment.getReply());
        return dto;
    }
}
