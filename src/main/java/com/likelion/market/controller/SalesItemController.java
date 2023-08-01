package com.likelion.market.controller;

import com.likelion.market.domain.dto.ResponseDto;
import com.likelion.market.domain.dto.comment.RequestCommentUserDto;
import com.likelion.market.domain.dto.salesItem.RequestSalesItemDto;
import com.likelion.market.service.SalesItemService;
import com.likelion.market.domain.dto.salesItem.ResponseSalesItemsDto;
import com.likelion.market.domain.dto.salesItem.ResponseSalesItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class SalesItemController {

    private final SalesItemService salesItemService;

    @PostMapping
    public ResponseEntity<ResponseDto> create(@RequestBody RequestSalesItemDto salesItemDto) {
        salesItemService.addItem(salesItemDto);
        return ResponseEntity.ok(ResponseDto.getMessage("물품이 등록되었습니다."));
    }

    @GetMapping
    public ResponseEntity<Page<ResponseSalesItemsDto>> readAll(
            @RequestParam("page") Integer page,
            @RequestParam("limit") Integer limit
    ) {
        return ResponseEntity.ok(salesItemService.readItemAllPaged(page, limit));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ResponseSalesItemDto> read(@PathVariable("itemId") Long id) {
        return ResponseEntity.ok(salesItemService.readItem(id));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<ResponseDto> update(@PathVariable("itemId") Long id, @RequestBody RequestSalesItemDto requestSalesItemDto) {
        salesItemService.updateItem(id, requestSalesItemDto);
        return ResponseEntity.ok(ResponseDto.getMessage("물품이 수정되었습니다."));
    }

    @PutMapping(value = "/{itemId}/image")
    public ResponseEntity<ResponseDto> updateImage(@PathVariable("itemId") Long id,
                                                   @RequestParam("image") MultipartFile itemImage,
                                                   @RequestParam("username") String username,
                                                   @RequestParam("password") String password) {
        salesItemService.updateItemImage(id, itemImage, username, password);
        return ResponseEntity.ok(ResponseDto.getMessage("이미지가 등록되었습니다."));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<ResponseDto> delete(@PathVariable("itemId") Long id, @RequestBody RequestCommentUserDto requestCommentUserDto) {
        salesItemService.deleteItem(id, requestCommentUserDto);
        return ResponseEntity.ok(ResponseDto.getMessage("물품을 삭제했습니다."));
    }

}
