package com.likelion.market.service;

import com.likelion.market.domain.dto.comment.RequestCommentUserDto;
import com.likelion.market.domain.dto.negotiation.RequestNegotiationDto;
import com.likelion.market.domain.dto.negotiation.ResponseNegotiationDto;
import com.likelion.market.domain.entity.Negotiation;
import com.likelion.market.domain.entity.SalesItem;
import com.likelion.market.domain.entity.User;
import com.likelion.market.repository.NegotiationRepository;
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

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NegotiationService {

    private final SalesItemRepository salesItemRepository;
    private final NegotiationRepository negotiationRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void addProposal(Long itemId, RequestNegotiationDto dto) {
        checkUserToken(dto.getUsername());
        User user = userRepository.findByUsername(dto.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        SalesItem salesItem = salesItemRepository.findById(itemId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        checkUserPassword(dto.getPassword(), user.getPassword());

        Negotiation negotiation = Negotiation.fromDto(dto);
        negotiation.setSalesItem(salesItem);
        negotiation.setUser(user);
        negotiationRepository.save(negotiation);
    }

    public Page<ResponseNegotiationDto> readProposals(Long itemId, String username, String password, Integer page) {
        checkUserToken(username);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        SalesItem salesItem = salesItemRepository.findById(itemId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Pageable pageable = PageRequest.of(
                page, 25, Sort.by("id")
        );

        Page<Negotiation> negotiationPage;
        // 글쓴이는 모든 제안 볼 수 있음
        if (user.getUsername().equals(salesItem.getUser().getUsername()) && passwordEncoder.matches(password, user.getPassword())) {
            negotiationPage = negotiationRepository.findAllBySalesItem(salesItem, pageable);
            return negotiationPage.map(ResponseNegotiationDto::fromEntity);
        }
        // 댓글 쓴 사람은 본인글만 볼 수 있음
        else {
            negotiationPage = negotiationRepository.findAllBySalesItemIdAndUserId(itemId, user.getId(), pageable);
            return negotiationPage.map(ResponseNegotiationDto::fromEntity);
        }
    }

    public void updateProposal(Long itemId, Long proposalId, RequestNegotiationDto dto) {
        checkUserToken(dto.getUsername());
        SalesItem salesItem = salesItemRepository.findById(itemId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Negotiation negotiation = negotiationRepository.findById(proposalId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        checkSameItem(salesItem, negotiation);
        checkUserMatch(negotiation, dto.getUsername(), dto.getPassword());

        negotiation.updatePrice(dto);
        negotiationRepository.save(negotiation);
    }

    public void updateStatus(Long itemId, Long proposalId, RequestNegotiationDto dto) {
        checkUserToken(dto.getUsername());
        SalesItem salesItem = salesItemRepository.findById(itemId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Negotiation negotiation = negotiationRepository.findById(proposalId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        checkSameItem(salesItem, negotiation);
        checkUserMatch(salesItem, dto.getUsername(), dto.getPassword());

        negotiation.setStatus(dto.getStatus()); // 수락 / 거절
        negotiationRepository.save(negotiation);
    }

    public void acceptProposal(Long itemId, Long proposalId, RequestNegotiationDto dto) {
        checkUserToken(dto.getUsername());
        SalesItem salesItem = salesItemRepository.findById(itemId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Negotiation negotiation = negotiationRepository.findById(proposalId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        checkSameItem(salesItem, negotiation);
        checkUserMatch(negotiation, dto.getUsername(), dto.getPassword());

        if (negotiation.getStatus().equals("수락")) {
            negotiation.setStatus(dto.getStatus()); // 확정
            negotiationRepository.save(negotiation);

            salesItem.setStatus(dto.getStatus()); // 판매완료
            salesItemRepository.save(salesItem);

            List<Negotiation> negotiationList = negotiationRepository.findAllBySalesItem(salesItem);
            for (Negotiation ne : negotiationList) {
                if (!ne.getStatus().equals("확정")) {
                    ne.setStatus("거절");
                    negotiationRepository.save(ne);
                }
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public void deleteProposal(Long itemId, Long proposalId, RequestCommentUserDto dto) {
        checkUserToken(dto.getUsername());
        SalesItem salesItem = salesItemRepository.findById(itemId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Negotiation negotiation = negotiationRepository.findById(proposalId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        checkSameItem(salesItem, negotiation);
        checkUserMatch(negotiation, dto.getUsername(), dto.getPassword());

        negotiationRepository.deleteById(proposalId);
    }

    private void checkSameItem(SalesItem salesItem, Negotiation negotiation) {
        if (!salesItem.getId().equals(negotiation.getSalesItem().getId()))
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

    private void checkUserMatch(Negotiation negotiation, String username, String password) {
        if (!negotiation.getUser().getUsername().equals(username) || !passwordEncoder.matches(password, negotiation.getUser().getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    private void checkUserPassword(String inputPassword, String password) {
        if (!passwordEncoder.matches(inputPassword, password))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

}
