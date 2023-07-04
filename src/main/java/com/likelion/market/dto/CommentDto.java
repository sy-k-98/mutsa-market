package com.likelion.market.dto;

import com.likelion.market.entity.Comment;
import lombok.Data;

@Data
public class CommentDto {
    private Long id;
    private Long itemId;
    private String writer;
    private String password;
    private String content;
    private String reply;

    public static CommentDto fromEntity(Comment entity){
        CommentDto dto = new CommentDto();
        dto.setId(entity.getId());
        dto.setItemId(entity.getItem().getId());
        dto.setWriter(entity.getWriter());
        dto.setPassword(entity.getPassword());
        dto.setContent(entity.getContent());
        dto.setReply(entity.getReply());
        return dto;
    }
}
