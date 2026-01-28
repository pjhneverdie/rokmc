package com.pdium.member.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.pdium.auth.service.exception.WrongIdOrPasswordException;
import com.pdium.member.domain.Member;
import com.pdium.member.dto.MemberPrincipal;
import com.pdium.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new WrongIdOrPasswordException());

        return MemberPrincipal.fromMember(member);
    }

}
