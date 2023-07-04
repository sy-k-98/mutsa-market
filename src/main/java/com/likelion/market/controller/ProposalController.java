package com.likelion.market.controller;

import com.likelion.market.dto.*;
import com.likelion.market.service.ProposalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items/{itemId}/proposals")
public class ProposalController {

    private final ProposalService proposalService;

    @PostMapping
    public MessageDto create(@PathVariable("itemId") Long itemId,
                             @RequestBody ProposalDto proposalDto) {
        proposalService.createProposal(itemId, proposalDto);
        MessageDto response = new MessageDto();
        response.setMessage("구매 제안이 등록되었습니다.");
        return response;
    }

    @GetMapping
    public Page<ProposalResponseDto> readAll(@PathVariable("itemId") Long itemId,
                                             @RequestParam("writer") String writer,
                                             @RequestParam("password") String password,
                                             @RequestParam("page") Integer page) {
        return proposalService.readProposals(itemId, writer, password, page);
    }

    @PutMapping("/{proposalId}")
    public MessageDto update(@PathVariable("itemId") Long itemId,
                             @PathVariable("proposalId") Long proposalId,
                             @RequestBody ProposalDto proposalDto) {
        MessageDto response = new MessageDto();
        if (proposalDto.getSuggestedPrice() != null) {
            proposalService.updateProposal(itemId, proposalId, proposalDto);
            response.setMessage("제안이 수정되었습니다.");
        }
        if (proposalDto.getStatus() != null) {
            proposalService.updateStatus(itemId, proposalId, proposalDto);
            if (proposalDto.getStatus().equals("수락") || proposalDto.getStatus().equals("거절"))
                response.setMessage("제안의 상태가 변경되었습니다.");
            else if (proposalDto.getStatus().equals("확정"))
                response.setMessage("구매가 확정되었습니다.");
        }
        return response;
    }

//    @PutMapping("/{proposalId}")
//    public MessageDto update(@PathVariable("itemId") Long itemId,
//                              @PathVariable("proposalId") Long proposalId,
//                              @RequestBody ProposalDto proposalDto) {
//        proposalService.updateProposal(itemId, proposalId, proposalDto);
//        MessageDto response = new MessageDto();
//        response.setMessage("제안이 수정되었습니다.");
//        return response;
//    }
//
//    @PutMapping("/{proposalId}")
//    public MessageDto updateOKorNO(@PathVariable("itemId") Long itemId,
//                             @PathVariable("proposalId") Long proposalId,
//                             @RequestBody ItemDto itemDto) {
//        proposalService.updateStatus(itemId, proposalId, itemDto);
//
//        String message = "";
//        if (itemDto.getStatus().equals("수락") || itemDto.getStatus().equals("거절"))
//            message = "제안의 상태가 변경되었습니다.";
//        else if (itemDto.getStatus().equals("확정"))
//            message = "구매가 확정되었습니다.";
//        MessageDto response = new MessageDto();
//        response.setMessage(message);
//        return response;
//    }

    @DeleteMapping("/{proposalId}")
    public MessageDto delete(@PathVariable("itemId") Long itemId,
                             @PathVariable("proposalId") Long proposalId,
                             @RequestBody UserDto dto) {
        proposalService.deleteProposal(itemId, proposalId, dto);
        MessageDto response = new MessageDto();
        response.setMessage("제안이 삭제되었습니다.");
        return response;
    }

}
