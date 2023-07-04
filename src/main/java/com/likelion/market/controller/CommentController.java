package com.likelion.market.controller;

import com.likelion.market.dto.*;
import com.likelion.market.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items/{itemId}/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public MessageDto create(@PathVariable("itemId") Long itemId,
                             @RequestBody CommentDto dto) {
        commentService.createComment(itemId, dto);
        MessageDto response = new MessageDto();
        response.setMessage("댓글이 등록되었습니다.");
        return response;
    }

    @GetMapping
    public Page<CommentResponseDto> readAll(
            @PathVariable("itemId") Long itemId
    ) {
        return commentService.readComments(itemId);
    }

    @PutMapping("/{commentId}")
    public MessageDto update(@PathVariable("itemId") Long itemId,
                             @PathVariable("commentId") Long commentId,
                             @RequestBody CommentDto commentDto) {
        commentService.updateComment(itemId, commentId, commentDto);
        MessageDto response = new MessageDto();
        response.setMessage("댓글이 수정되었습니다.");
        return response;
    }

    @PutMapping("/{commentId}/reply")
    public MessageDto updateReply(@PathVariable("itemId") Long itemId,
                                  @PathVariable("commentId") Long commentId,
                                  @RequestBody CommentReplyDto commentReplyDto) {
        commentService.updateCommentReply(itemId, commentId, commentReplyDto);
        MessageDto response = new MessageDto();
        response.setMessage("댓글에 답변이 추가되었습니다.");
        return response;
    }

    @DeleteMapping("/{commentId}")
    public MessageDto delete(@PathVariable("itemId") Long itemId,
                             @PathVariable("commentId") Long commentId,
                             @RequestBody UserDto userDto) {
        commentService.deleteComment(itemId, commentId, userDto);
        MessageDto response = new MessageDto();
        response.setMessage("댓글을 삭제했습니다.");
        return response;
    }

}
