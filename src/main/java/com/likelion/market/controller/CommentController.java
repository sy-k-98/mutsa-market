package com.likelion.market.controller;

import com.likelion.market.domain.dto.ResponseDto;
import com.likelion.market.domain.dto.comment.RequestCommentUserDto;
import com.likelion.market.domain.dto.comment.RequestCommentDto;
import com.likelion.market.domain.dto.comment.RequestCommentReplyDto;
import com.likelion.market.domain.dto.comment.ResponseCommentDto;
import com.likelion.market.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items/{itemId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ResponseDto> create(@PathVariable("itemId") Long itemId,
                                 @RequestBody RequestCommentDto dto) {
        commentService.createComment(itemId, dto);
        return ResponseEntity.ok(ResponseDto.getMessage("댓글이 등록되었습니다."));
    }

    @GetMapping
    public ResponseEntity<Page<ResponseCommentDto>> readAll(
            @PathVariable("itemId") Long itemId
    ) {
        return ResponseEntity.ok(commentService.readComments(itemId));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<ResponseDto> update(@PathVariable("itemId") Long itemId,
                                              @PathVariable("commentId") Long commentId,
                                              @RequestBody RequestCommentDto requestCommentDto) {
        commentService.updateComment(itemId, commentId, requestCommentDto);
        return ResponseEntity.ok(ResponseDto.getMessage("댓글이 수정되었습니다."));
    }

    @PutMapping("/{commentId}/reply")
    public ResponseEntity<ResponseDto> createReply(@PathVariable("itemId") Long itemId,
                                                   @PathVariable("commentId") Long commentId,
                                                   @RequestBody RequestCommentReplyDto requestCommentReplyDto) {
        commentService.createCommentReply(itemId, commentId, requestCommentReplyDto);
        return ResponseEntity.ok(ResponseDto.getMessage("댓글에 답변이 추가되었습니다."));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResponseDto> delete(@PathVariable("itemId") Long itemId,
                                              @PathVariable("commentId") Long commentId,
                                              @RequestBody RequestCommentUserDto requestCommentUserDto) {
        commentService.deleteComment(itemId, commentId, requestCommentUserDto);
        return ResponseEntity.ok(ResponseDto.getMessage("댓글을 삭제했습니다."));
    }

}
