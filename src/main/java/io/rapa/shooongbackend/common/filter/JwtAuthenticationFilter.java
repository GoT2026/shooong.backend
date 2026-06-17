package io.rapa.shooongbackend.common.filter;

import io.rapa.shooongbackend.member.service.MemberService;
import io.rapa.shooongbackend.security.entity.DefaultCurrentUser;
import io.rapa.shooongbackend.security.service.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private final MemberService memberService;
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String extractedToken = extractToken(request);

        if(extractedToken != null && tokenProvider.validate(extractedToken)){
            Long foundedUserId = tokenProvider.parseJwtToUserId(extractedToken);
            DefaultCurrentUser defaultCurrentUser = memberService.loadUserDetailsFromMember(foundedUserId);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    defaultCurrentUser,
                    null,
                    defaultCurrentUser.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        filterChain.doFilter(request,response);

    }
    public String extractToken(HttpServletRequest httpServletRequest){
        String bearerToken = httpServletRequest.getHeader(
                HttpHeaders.AUTHORIZATION
        );
        if(bearerToken != null && bearerToken.startsWith("Bearer")){
            return bearerToken;
        }
        return null;
    }
}
