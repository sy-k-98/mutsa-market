package com.likelion.market.service;

import com.likelion.market.dto.ItemWithIDResponseDto;
import com.likelion.market.dto.ItemDto;
import com.likelion.market.dto.ItemWithoutIDResponseDto;
import com.likelion.market.dto.UserDto;
import com.likelion.market.entity.SalesItem;
import com.likelion.market.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    public void createPost(ItemDto dto) {
        SalesItem salesItem = new SalesItem();
        salesItem.setTitle(dto.getTitle());
        salesItem.setDescription(dto.getDescription());
        salesItem.setImageUrl(dto.getImageUrl());
        salesItem.setMinPriceWanted(dto.getMinPriceWanted());
        salesItem.setStatus("판매중");
        salesItem.setWriter(dto.getWriter());
        salesItem.setPassword(dto.getPassword());

        itemRepository.save(salesItem);
    }

    public ItemWithoutIDResponseDto readPost(Long id) {
        Optional<SalesItem> salesItem = itemRepository.findById(id);
        if (salesItem.isPresent())
            return ItemWithoutIDResponseDto.fromEntity(salesItem.get());
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public Page<ItemWithIDResponseDto> readPostPaged(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(
                pageNumber, pageSize, Sort.by("id")
        );
        Page<SalesItem> salesItemPage = itemRepository.findAll(pageable);

        Page<ItemWithIDResponseDto> ItemDtoPage = salesItemPage.map(ItemWithIDResponseDto::fromEntity);
        return ItemDtoPage;
    }

    public void updatePost(Long id, ItemWithIDResponseDto dto) {
        Optional<SalesItem> optionalItem = itemRepository.findById(id);
        if (optionalItem.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        SalesItem salesItem = optionalItem.get();
        salesItem.setTitle(dto.getTitle());
        salesItem.setDescription(dto.getDescription());
        salesItem.setImageUrl(dto.getImageUrl());
        salesItem.setMinPriceWanted(dto.getMinPriceWanted());
        salesItem.setStatus("판매중");

        itemRepository.save(salesItem);
    }

    public void updatePostImage(Long id, MultipartFile itemImage, String writer, String password) {
        Optional<SalesItem> optionalItem = itemRepository.findById(id);
        // 아이템 있는지 확인
        if (optionalItem.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        // 입력한 비밀번호와 원래 아이템의 비밀번호가 일치하는지 확안
        if (!optionalItem.get().getPassword().equals(password))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        // 폴더 만들기
        String profileDir = String.format("media/%d/", id);
        try {
            Files.createDirectories(Path.of(profileDir));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 확장자를 포함한 이미지 이름 만들기
        String originalFilename = itemImage.getOriginalFilename();
        String[] fileNameSplit = originalFilename.split("\\.");
        String extension = fileNameSplit[fileNameSplit.length - 1];
        String profileFilename = "image." + extension;
        // 폴더와 파일 경로를 포함한 이름 만들기
        String profilePath = profileDir + profileFilename;

        // 저장하기
        try {
            itemImage.transferTo(Path.of(profilePath));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // SalesItem 업데이트
        SalesItem salesItem = optionalItem.get();
        salesItem.setImageUrl((String.format("/static/images/%d/%s", id, profileFilename)));

        itemRepository.save(salesItem);
    }

    public void deletePost(Long id, UserDto userDto) {
        if (itemRepository.existsById(id))
            if (userDto.getPassword().equals(itemRepository.findById(id).get().getPassword()))
                itemRepository.deleteById(id);
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
