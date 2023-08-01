package com.likelion.market.service;

import com.likelion.market.domain.dto.comment.RequestCommentDto;
import com.likelion.market.domain.dto.comment.RequestCommentReplyDto;
import com.likelion.market.domain.dto.comment.RequestCommentUserDto;
import com.likelion.market.domain.dto.comment.ResponseCommentDto;
import com.likelion.market.domain.entity.Comment;
import com.likelion.market.domain.entity.SalesItem;
import com.likelion.market.domain.entity.User;
import com.likelion.market.repository.CommentRepository;
import com.likelion.market.repository.SalesItemRepository;
import com.likelion.market.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final SalesItemRepository salesItemRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void addComment(Long itemId, RequestCommentDto dto) {
        checkUserToken(dto.getUsername());
        User user = userRepository.findByUsername(dto.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        SalesItem salesItem = salesItemRepository.findById(itemId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        checkUserPassword(dto.getPassword(), user.getPassword());

        Comment comment = Comment.fromDto(dto);
        comment.setUser(user);
        comment.setSalesItem(salesItem);
        commentRepository.save(comment);
    }

    public Page<ResponseCommentDto> readComments(Long itemId) {
        SalesItem salesItem = salesItemRepository.findById(itemId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Pageable pageable = PageRequest.of(
                0, 25, Sort.by("id")
        );
        Page<Comment> commentPage = commentRepository.findAllBySalesItem(salesItem, pageable);
        return commentPage.map(ResponseCommentDto::fromEntity);
    }

    public void updateComment(Long itemId, Long commentId, RequestCommentDto dto) {
        checkUserToken(dto.getUsername());
        SalesItem salesItem = salesItemRepository.findById(itemId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        checkSameItem(salesItem, comment);
        checkUserMatch(comment, dto.getUsername(), dto.getPassword());

        comment.updateComment(dto);
        commentRepository.save(comment);
    }

    public void addCommentReply(Long itemId, Long commentId, RequestCommentReplyDto dto) {
        checkUserToken(dto.getUsername());
        SalesItem salesItem = salesItemRepository.findById(itemId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        checkSameItem(salesItem, comment);
        checkUserMatch(salesItem, dto.getUsername(), dto.getPassword());

        comment.setReply(dto);
        commentRepository.save(comment);
    }

    public void deleteComment(Long itemId, Long commentId, RequestCommentUserDto dto) {
        checkUserToken(dto.getUsername());
        SalesItem salesItem = salesItemRepository.findById(itemId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        checkSameItem(salesItem, comment);
        checkUserMatch(comment, dto.getUsername(), dto.getPassword());

        commentRepository.deleteById(commentId);
    }

    private void checkSameItem(SalesItem salesItem, Comment comment) {
        if (!salesItem.getId().equals(comment.getSalesItem().getId()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    private void checkUserToken(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getName().equals(username))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    private void checkUserMatch(SalesItem salesItem, String username, String password) {
        if (!salesItem.getUser().getUsername().equals(username) || !passwordEncoder.matches(password, salesItem.getUser().getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    private void checkUserMatch(Comment comment, String username, String password) {
        if (!comment.getUser().getUsername().equals(username) || !passwordEncoder.matches(password, comment.getUser().getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    private void checkUserPassword(String inputPassword, String password) {
        if (!passwordEncoder.matches(inputPassword, password))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
}
