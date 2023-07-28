package com.likelion.market.service;

import com.likelion.market.domain.dto.comment.RequestCommentUserDto;
import com.likelion.market.domain.dto.negotiation.RequestNegotiationDto;
import com.likelion.market.domain.dto.negotiation.ResponseNegotiationDto;
import com.likelion.market.domain.entity.Negotiation;
import com.likelion.market.repository.NegotiationRepository;
import com.likelion.market.domain.entity.SalesItem;
import com.likelion.market.repository.SalesItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NegotiationService {

    private final SalesItemRepository salesItemRepository;
    private final NegotiationRepository negotiationRepository;

    public void createProposal(Long itemId, RequestNegotiationDto dto) {
        if (!salesItemRepository.existsById(itemId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        Negotiation proposal = Negotiation.fromDto(dto);
        negotiationRepository.save(proposal);
    }

    public Page<ResponseNegotiationDto> readProposals(Long itemId, String writer, String password, Integer page) {
        if (!salesItemRepository.existsById(itemId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        Pageable pageable = PageRequest.of(
                page, 25, Sort.by("id")
        );

        SalesItem salesItem = salesItemRepository.findById(itemId).get();
        if (writer.equals(salesItem.getWriter()) && password.equals(salesItem.getPassword()))
        {
            Page<Negotiation> proposalPage = negotiationRepository.findAll(pageable);
            Page<ResponseNegotiationDto> proposalResponseDtoPage = proposalPage.map(ResponseNegotiationDto::fromEntity);
            return proposalResponseDtoPage;
        }
        else {
            Page<Negotiation> proposalPage = negotiationRepository.findByWriterAndPassword(writer, password, pageable);
            Page<ResponseNegotiationDto> proposalResponseDtoPage = proposalPage.map(ResponseNegotiationDto::fromEntity);
            return proposalResponseDtoPage;
        }
    }

    public void updateProposal(Long itemId, Long proposalId, RequestNegotiationDto dto) {
        Optional<SalesItem> optionalItem = salesItemRepository.findById(itemId);
        if (optionalItem.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        Optional<Negotiation> optionalProposal = negotiationRepository.findById(proposalId);
        if (optionalProposal.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        Negotiation proposal = optionalProposal.get();
        if (dto.getWriter().equals(proposal.getWriter())
                && dto.getPassword().equals(proposal.getPassword())) {
            proposal.setSuggestedPrice(dto.getSuggestedPrice());
            negotiationRepository.save(proposal);
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    public void updateStatus(Long itemId, Long proposalId, RequestNegotiationDto dto) {
        Optional<SalesItem> optionalItem = salesItemRepository.findById(itemId);
        if (optionalItem.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        Optional<Negotiation> optionalProposal = negotiationRepository.findById(proposalId);
        if (optionalProposal.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        SalesItem salesItem = optionalItem.get();
        Negotiation proposal = optionalProposal.get();

        if (dto.getStatus().equals("수락") || dto.getStatus().equals("거절")) {
            if (dto.getWriter().equals(salesItem.getWriter())
                    && dto.getPassword().equals(salesItem.getPassword())) {
                proposal.setStatus(dto.getStatus());
                negotiationRepository.save(proposal);
            } else
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        } else if (dto.getStatus().equals("확정")) {
            if (dto.getWriter().equals(proposal.getWriter())
                    && dto.getPassword().equals(proposal.getPassword())
                    && proposal.getStatus().equals("수락")) {
                salesItem.setStatus("판매완료");
                proposal.setStatus(dto.getStatus());
                salesItemRepository.save(salesItem);
                negotiationRepository.save(proposal);

                List<Negotiation> proposalList = negotiationRepository.findByIdNot(proposalId);
                for (Negotiation p : proposalList) {
                    p.setStatus("거절");
                    negotiationRepository.save(p);
                }
            }
        } else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    public void deleteProposal(Long itemId, Long proposalId, RequestCommentUserDto dto) {
        if (!salesItemRepository.existsById(itemId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        if (!negotiationRepository.existsById(proposalId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        Negotiation proposal = negotiationRepository.findById(proposalId).get();
        if (!dto.getPassword().equals(proposal.getPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        negotiationRepository.deleteById(proposalId);
    }

}
