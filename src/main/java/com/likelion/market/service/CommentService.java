package com.likelion.market.service;

import com.likelion.market.domain.dto.comment.RequestCommentUserDto;
import com.likelion.market.domain.entity.Comment;
import com.likelion.market.domain.dto.comment.RequestCommentDto;
import com.likelion.market.domain.dto.comment.RequestCommentReplyDto;
import com.likelion.market.domain.dto.comment.ResponseCommentDto;
import com.likelion.market.repository.CommentRepository;
import com.likelion.market.domain.entity.SalesItem;
import com.likelion.market.repository.SalesItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final SalesItemRepository salesItemRepository;
    private final CommentRepository commentRepository;

    public void createComment(Long itemId, RequestCommentDto dto) {
        if (!salesItemRepository.existsById(itemId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        Comment comment = Comment.fromDto(dto);
        commentRepository.save(comment);
    }

    public Page<ResponseCommentDto> readComments(Long itemId) {
        Pageable pageable = PageRequest.of(
                0, 25, Sort.by("id")
        );
        Page<Comment> commentPage = commentRepository.findAll(pageable);
        Page<ResponseCommentDto> commentResponseDtoPage = commentPage.map(ResponseCommentDto::fromEntity);
        return commentResponseDtoPage;
    }

    public void updateComment(Long itemId, Long commentId, RequestCommentDto dto) {
        Optional<SalesItem> optionalItem = salesItemRepository.findById(itemId);
        if (optionalItem.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        Comment comment = optionalComment.get();
        if (!dto.getPassword().equals(comment.getPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        comment.setContent(dto.getContent());

        commentRepository.save(comment);
    }

    public void createCommentReply(Long itemId, Long commentId, RequestCommentReplyDto dto) {
        Optional<SalesItem> optionalItem = salesItemRepository.findById(itemId);
        if (optionalItem.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        SalesItem salesItem = optionalItem.get();
        Comment comment = optionalComment.get();
        if (!dto.getPassword().equals(salesItem.getPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        comment.setReply(dto.getReply());

        commentRepository.save(comment);
    }

    public void deleteComment(Long itemId, Long commentId, RequestCommentUserDto dto) {
        if (!salesItemRepository.existsById(itemId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        if (!commentRepository.existsById(commentId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        Comment comment = commentRepository.findById(commentId).get();
        if (!dto.getPassword().equals(comment.getPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        commentRepository.deleteById(commentId);
    }
}
