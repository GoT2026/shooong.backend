package io.rapa.shooongbackend.member.repository;

import io.rapa.shooongbackend.common.constant.ErrorCode;
import io.rapa.shooongbackend.common.exception.CustomException;
import io.rapa.shooongbackend.member.Members;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Member;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Members, Long> {
    default Members findByIdOrThrow(Long memberId){
        return findById(memberId).orElseThrow(
                ()-> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );
    }

    Boolean existsByLoginId(String loginId);

    Optional<Members> findByLoginId(String loginId);

    default Members findByLoginIdOrThrow(String loginId){
        return findByLoginId(loginId).orElseThrow(
                ()-> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );
    }
}
