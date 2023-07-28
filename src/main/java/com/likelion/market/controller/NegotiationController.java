package com.likelion.market.controller;

import com.likelion.market.domain.dto.ResponseDto;
import com.likelion.market.domain.dto.comment.RequestCommentUserDto;
import com.likelion.market.domain.dto.negotiation.RequestNegotiationDto;
import com.likelion.market.domain.dto.negotiation.ResponseNegotiationDto;
import com.likelion.market.service.NegotiationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items/{itemId}/proposals")
public class NegotiationController {

    private final NegotiationService negotiationService;

    @PostMapping
    public ResponseEntity<ResponseDto> create(@PathVariable("itemId") Long itemId,
                                              @RequestBody RequestNegotiationDto requestNegotiationDto) {
        negotiationService.createProposal(itemId, requestNegotiationDto);
        return ResponseEntity.ok(ResponseDto.getMessage("구매 제안이 등록되었습니다."));
    }

    @GetMapping
    public ResponseEntity<Page<ResponseNegotiationDto> >readAll(@PathVariable("itemId") Long itemId,
                                                @RequestParam("writer") String writer,
                                                @RequestParam("password") String password,
                                                @RequestParam("page") Integer page) {
        return ResponseEntity.ok(negotiationService.readProposals(itemId, writer, password, page));
    }

    @PutMapping("/{proposalId}")
    public ResponseEntity<ResponseDto> update(@PathVariable("itemId") Long itemId,
                              @PathVariable("proposalId") Long proposalId,
                              @RequestBody RequestNegotiationDto requestNegotiationDto) {
        if (requestNegotiationDto.getStatus() == null) {
            negotiationService.updateProposal(itemId, proposalId, requestNegotiationDto);
            return ResponseEntity.ok(ResponseDto.getMessage("제안이 수정되었습니다."));
        }
        else {
            negotiationService.updateStatus(itemId, proposalId, requestNegotiationDto);
            if (requestNegotiationDto.getStatus().equals("수락") || requestNegotiationDto.getStatus().equals("거절"))
                return ResponseEntity.ok(ResponseDto.getMessage("제안의 상태가 변경되었습니다."));
            else if (requestNegotiationDto.getStatus().equals("확정"))
                return ResponseEntity.ok(ResponseDto.getMessage("구매가 확정되었습니다."));

        }
        return null;
    }

    @DeleteMapping("/{proposalId}")
    public ResponseEntity<ResponseDto> delete(@PathVariable("itemId") Long itemId,
                              @PathVariable("proposalId") Long proposalId,
                              @RequestBody RequestCommentUserDto dto) {
        negotiationService.deleteProposal(itemId, proposalId, dto);
        return ResponseEntity.ok(ResponseDto.getMessage("제안을 삭제했습니다."));
    }

}
