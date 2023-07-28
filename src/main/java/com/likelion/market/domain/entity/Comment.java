package com.likelion.market.domain.entity;

import com.likelion.market.domain.dto.comment.RequestCommentDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "salesItem_id")
    private SalesItem item;

    private String writer;

    private String password;

    private String content;

    private String reply;

    public static Comment fromDto(RequestCommentDto commentDto) {
        Comment comment = new Comment();
        comment.setWriter(commentDto.getWriter());
        comment.setPassword(commentDto.getPassword());
        comment.setContent(commentDto.getContent());
        comment.setReply(commentDto.getReply());

        return comment;
    }

}
