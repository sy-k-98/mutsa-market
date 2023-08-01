package com.likelion.market.domain.entity;

import com.likelion.market.domain.dto.comment.RequestCommentDto;
import com.likelion.market.domain.dto.comment.RequestCommentReplyDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salesItem_id")
    private SalesItem salesItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String content;

    private String reply;

    public static Comment fromDto(RequestCommentDto commentDto) {
        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());

        return comment;
    }

    public void setUser(User user) {
        this.user = user;
        user.getComments().add(this);
    }

    public void setSalesItem(SalesItem salesItem) {
        this.salesItem = salesItem;
        salesItem.getComments().add(this);
    }

    public void setReply(RequestCommentReplyDto dto) {
        this.reply = dto.getReply();
    }

    public void updateComment(RequestCommentDto dto) {
        this.content = dto.getContent();
    }



}
