package com.likelion.market.service;

import com.likelion.market.dto.*;
import com.likelion.market.entity.Comment;
import com.likelion.market.entity.SalesItem;
import com.likelion.market.repository.CommentRepository;
import com.likelion.market.repository.ItemRepository;
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
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;

    public void createComment(Long itemId, CommentDto dto) {
        if (!itemRepository.existsById(itemId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        Comment comment = new Comment();
        comment.setWriter(dto.getWriter());
        comment.setPassword(dto.getPassword());
        comment.setContent(dto.getContent());
        comment.setReply(dto.getReply());

        commentRepository.save(comment);
    }

    public Page<CommentResponseDto> readComments(Long itemId) {
        Pageable pageable = PageRequest.of(
                0, 25, Sort.by("id")
        );
        Page<Comment> commentPage = commentRepository.findAll(pageable);

        Page<CommentResponseDto> commentResponseDtoPage = commentPage.map(CommentResponseDto::fromEntity);
        return commentResponseDtoPage;
    }

    public void updateComment(Long itemId, Long commentId, CommentDto dto) {
        Optional<SalesItem> optionalItem = itemRepository.findById(itemId);
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

    public void updateCommentReply(Long itemId, Long commentId, CommentReplyDto dto) {
        Optional<SalesItem> optionalItem = itemRepository.findById(itemId);
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

    public void deleteComment(Long itemId, Long commentId, UserDto userDto) {
        if (!itemRepository.existsById(itemId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        if (!commentRepository.existsById(commentId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        Comment comment = optionalComment.get();
        if (!userDto.getPassword().equals(comment.getPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        commentRepository.deleteById(commentId);
    }
}
