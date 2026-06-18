package io.rapa.shooongbackend.security.service;

import io.rapa.shooongbackend.common.constant.ErrorCode;
import io.rapa.shooongbackend.common.util.PreConditions;
import io.rapa.shooongbackend.member.entity.Members;
import io.rapa.shooongbackend.member.repository.MemberRepository;
import io.rapa.shooongbackend.security.dto.KeyPair;
import io.rapa.shooongbackend.security.dto.UserLoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;


    @Transactional
    public KeyPair signIn(UserLoginRequest request){

        Members foundedMember = memberRepository.findByLoginIdOrThrow(request.userId());

        PreConditions.validate(
                passwordEncoder.matches(request.password(), foundedMember.getPassword()),
                ErrorCode.AUTHENTICATION_INCORRECT
        );

        return tokenProvider.issueKeyPair(foundedMember.getMemberId());
    }
}
