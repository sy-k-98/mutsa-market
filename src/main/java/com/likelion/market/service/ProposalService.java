package com.likelion.market.service;

import com.likelion.market.dto.*;
import com.likelion.market.entity.Comment;
import com.likelion.market.entity.Proposal;
import com.likelion.market.entity.SalesItem;
import com.likelion.market.repository.ItemRepository;
import com.likelion.market.repository.ProposalRepository;
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
public class ProposalService {

    private final ItemRepository itemRepository;
    private final ProposalRepository proposalRepository;

    public void createProposal(Long itemId, ProposalDto dto) {
        if (!itemRepository.existsById(itemId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        Proposal proposal = new Proposal();
        proposal.setStatus("제안");
        proposal.setWriter(dto.getWriter());
        proposal.setPassword(dto.getPassword());
        proposal.setSuggestedPrice(dto.getSuggestedPrice());

        proposalRepository.save(proposal);
    }

    public Page<ProposalResponseDto> readProposals(Long itemId, String writer, String password, Integer page) {
        if (!itemRepository.existsById(itemId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        Pageable pageable = PageRequest.of(
                page, 25, Sort.by("id")
        );

        SalesItem salesItem = itemRepository.findById(itemId).get();
        if (writer.equals(salesItem.getWriter()) && password.equals(salesItem.getPassword()))
        {
            Page<Proposal> proposalPage = proposalRepository.findAll(pageable);
            Page<ProposalResponseDto> proposalResponseDtoPage = proposalPage.map(ProposalResponseDto::fromEntity);
            return proposalResponseDtoPage;
        }
        else {
            Page<Proposal> proposalPage = proposalRepository.findByWriterAndPassword(writer, password, pageable);
            Page<ProposalResponseDto> proposalResponseDtoPage = proposalPage.map(ProposalResponseDto::fromEntity);
            return proposalResponseDtoPage;
        }
    }

    public void updateProposal(Long itemId, Long proposalId, ProposalDto dto) {
        Optional<SalesItem> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        Optional<Proposal> optionalProposal = proposalRepository.findById(proposalId);
        if (optionalProposal.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        Proposal proposal = optionalProposal.get();
        if (dto.getWriter().equals(proposal.getWriter())
                && dto.getPassword().equals(proposal.getPassword())) {
            proposal.setSuggestedPrice(dto.getSuggestedPrice());
            proposalRepository.save(proposal);
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    public void updateStatus(Long itemId, Long proposalId, ProposalDto dto) {
        Optional<SalesItem> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        Optional<Proposal> optionalProposal = proposalRepository.findById(proposalId);
        if (optionalProposal.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        SalesItem salesItem = optionalItem.get();
        Proposal proposal = optionalProposal.get();

        if (dto.getStatus().equals("수락") || dto.getStatus().equals("거절")) {
            if (dto.getWriter().equals(salesItem.getWriter())
                    && dto.getPassword().equals(salesItem.getPassword())) {
                proposal.setStatus(dto.getStatus());
                proposalRepository.save(proposal);
            } else
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        } else if (dto.getStatus().equals("확정")) {
            if (dto.getWriter().equals(proposal.getWriter())
                    && dto.getPassword().equals(proposal.getPassword())
                    && proposal.getStatus().equals("수락")) {
                salesItem.setStatus("판매완료");
                proposal.setStatus(dto.getStatus());
                itemRepository.save(salesItem);
                proposalRepository.save(proposal);

                List<Proposal> proposalList = proposalRepository.findByIdNot(proposalId);
                for (Proposal p : proposalList) {
                    p.setStatus("거절");
                    proposalRepository.save(p);
                }
            }
        } else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    public void deleteProposal(Long itemId, Long proposalId, UserDto dto) {
        if (!itemRepository.existsById(itemId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        if (!proposalRepository.existsById(proposalId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        Proposal proposal = proposalRepository.findById(proposalId).get();
        if (!dto.getPassword().equals(proposal.getPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        proposalRepository.deleteById(proposalId);
    }

}
