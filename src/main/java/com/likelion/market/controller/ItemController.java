package com.likelion.market.controller;

import com.likelion.market.dto.*;
import com.likelion.market.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public MessageDto create(@RequestBody ItemDto itemDto) {
        itemService.createPost(itemDto);
        MessageDto response = new MessageDto();
        response.setMessage("등록이 완료되었습니다.");
        return response;
    }

    @GetMapping
    public Page<ItemWithIDResponseDto> readAll(
            @RequestParam("page") Integer page,
            @RequestParam("limit") Integer limit
    ) {
        return itemService.readPostPaged(page, limit);
    }

    @GetMapping("/{itemId}")
    public ItemWithoutIDResponseDto read(@PathVariable("itemId") Long id) {
        return itemService.readPost(id);
    }

    @PutMapping("/{itemId}")
    public MessageDto update(@PathVariable("itemId") Long id, @RequestBody ItemWithIDResponseDto itemDto) {
        itemService.updatePost(id, itemDto);
        MessageDto response = new MessageDto();
        response.setMessage("물품이 수정되었습니다.");
        return response;
    }

    @PutMapping(value = "/{itemId}/image")
    public MessageDto updateImage(@PathVariable("itemId") Long id,
                                  @RequestParam("image") MultipartFile itemImage,
                                  @RequestParam("writer") String writer,
                                  @RequestParam("password") String password) {
        itemService.updatePostImage(id, itemImage, writer, password);
        MessageDto response = new MessageDto();
        response.setMessage("이미지가 등록되었습니다.");
        return response;
    }

    @DeleteMapping("/{itemId}")
    public MessageDto delete(@PathVariable("itemId") Long id, @RequestBody UserDto userDto) {
        itemService.deletePost(id, userDto);
        MessageDto response = new MessageDto();
        response.setMessage("물품을 삭제했습니다.");
        return response;
    }

}
