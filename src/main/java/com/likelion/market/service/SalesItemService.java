package com.likelion.market.service;

import com.likelion.market.domain.dto.comment.RequestCommentUserDto;
import com.likelion.market.domain.entity.User;
import com.likelion.market.repository.SalesItemRepository;
import com.likelion.market.domain.dto.salesItem.RequestSalesItemDto;
import com.likelion.market.domain.dto.salesItem.ResponseSalesItemsDto;
import com.likelion.market.domain.dto.salesItem.ResponseSalesItemDto;
import com.likelion.market.domain.entity.SalesItem;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
@RequiredArgsConstructor
public class SalesItemService {

    private final SalesItemRepository salesItemRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void addItem(RequestSalesItemDto dto) {
        checkUserToken(dto.getUsername());
        User user = userRepository.findByUsername(dto.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        checkUserPassword(dto.getPassword(), user.getPassword());

        SalesItem salesItem = SalesItem.fromDto(dto);
        salesItem.setUser(user);
        salesItemRepository.save(salesItem);
    }

    public ResponseSalesItemDto readItem(Long id) {
        SalesItem salesItem = salesItemRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseSalesItemDto.fromEntity(salesItem);
    }

    public Page<ResponseSalesItemsDto> readItemAllPaged(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(
                pageNumber, pageSize, Sort.by("id")
        );
        Page<SalesItem> salesItemPage = salesItemRepository.findAll(pageable);
        return salesItemPage.map(ResponseSalesItemsDto::fromEntity);
    }

    public void updateItem(Long id, RequestSalesItemDto dto) {
        checkUserToken(dto.getUsername());
        SalesItem salesItem = salesItemRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        checkUserMatch(salesItem, dto.getUsername(), dto.getPassword());

        salesItem.updateInfo(dto);
        salesItemRepository.save(salesItem);
    }

    public void updateItemImage(Long id, MultipartFile itemImage, String username, String password) {
        checkUserToken(username);
        SalesItem salesItem = salesItemRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        checkUserMatch(salesItem, username, password);

        String profileDir = String.format("media/%d/", id);
        try {
            Files.createDirectories(Path.of(profileDir));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String originalFilename = itemImage.getOriginalFilename();
        String[] fileNameSplit = originalFilename.split("\\.");
        String extension = fileNameSplit[fileNameSplit.length - 1];
        String profileFilename = "image." + extension;
        String profilePath = profileDir + profileFilename;

        try {
            itemImage.transferTo(Path.of(profilePath));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        salesItem.updateImageUrl(String.format("/static/images/%d/%s", id, profileFilename));
        salesItemRepository.save(salesItem);
    }

    public void deleteItem(Long id, RequestCommentUserDto dto) {
        checkUserToken(dto.getUsername());
        SalesItem salesItem = salesItemRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        checkUserMatch(salesItem, dto.getUsername(), dto.getPassword());
        salesItemRepository.deleteById(id);
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

    private void checkUserPassword(String inputPassword, String password) {
        if (!passwordEncoder.matches(inputPassword, password))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
}
