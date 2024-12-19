package com.poly.gestioncoworkingspace.services;

import com.poly.gestioncoworkingspace.entities.Member;
import com.poly.gestioncoworkingspace.repositories.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@AllArgsConstructor
@Service
public class MemberService {
    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);

    @Autowired
    private MemberRepository memberRepository;

    public List<Member> getAllMembers() {
        logger.info("Fetching all members...");
        return memberRepository.findAll();
    }

    public Member addMember(Member member) {
        logger.info("Adding a new member: {}", member);
        return memberRepository.save(member);
    }

    public void deleteMember(Long id) {
        logger.info("Deleting member with ID: {}", id);
        memberRepository.deleteById(id);
    }

    public Member getMemberById(Long id) {
        logger.info("Fetching member with ID: {}", id);
        return memberRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Member with ID {} not found", id);
                    return new RuntimeException("Member with ID " + id + " not found");
                });
    }

    @Transactional
    public Member updateMember(Long id, Member member) {
        logger.info("Updating member with ID: {}", id);
        return memberRepository.findById(id).map(existingMember -> {
            existingMember.setFullName(member.getFullName());
            existingMember.setEmail(member.getEmail());
            existingMember.setPhoneNumber(member.getPhoneNumber());
            logger.info("Member updated: {}", existingMember);
            return memberRepository.save(existingMember);
        }).orElseThrow(() -> {
            logger.error("Member with ID {} not found", id);
            return new RuntimeException("Member with ID " + id + " not found");
        });
    }

    public Page<Member> getMembersByMC(String mc, Pageable pageable) {
        return memberRepository.findByFullNameContains(mc, pageable);
    }
}
