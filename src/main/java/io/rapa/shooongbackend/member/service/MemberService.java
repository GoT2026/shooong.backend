package io.rapa.shooongbackend.member.service;

import io.rapa.shooongbackend.common.constant.ErrorCode;
import io.rapa.shooongbackend.common.util.PreConditions;
import io.rapa.shooongbackend.member.Members;
import io.rapa.shooongbackend.member.repository.MemberRepository;
import io.rapa.shooongbackend.member.dto.UserCreateRequest;
import io.rapa.shooongbackend.security.entity.DefaultCurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public DefaultCurrentUser loadUserDetailsFromMember(Long userId){
        return DefaultCurrentUser.from(
                memberRepository.findByIdOrThrow(userId)
        );
    }

    @Transactional
    public void signUp(UserCreateRequest request){
        PreConditions.validate(
                !memberRepository.existsByLoginId(request.id()),
                ErrorCode.USER_ID_ALREADY_EXISTS
        );

        memberRepository.save(
                Members.builder()
                        .name(request.name())
                        .password(passwordEncoder.encode(request.password()))
                        .loginId(request.id())
                        .build()
        );
    }
}
