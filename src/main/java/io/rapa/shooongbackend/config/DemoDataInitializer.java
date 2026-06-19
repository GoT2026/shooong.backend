package io.rapa.shooongbackend.config;

import io.rapa.shooongbackend.member.entity.Members;
import io.rapa.shooongbackend.member.repository.MemberRepository;
import io.rapa.shooongbackend.order.entity.Orders;
import io.rapa.shooongbackend.order.repository.OrderRepository;
import io.rapa.shooongbackend.ranking.util.LeaderboardScores;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
        name = "shooong.demo-data.enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class DemoDataInitializer implements CommandLineRunner {
    private static final String DEMO_LOGIN_ID_PREFIX = "shooong_demo_";

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        normalizeLeaderboardScores();

        if (memberRepository.existsByLoginId(DEMO_LOGIN_ID_PREFIX + "01")) {
            return;
        }

        createLeaderboardSample("pilot01", DEMO_LOGIN_ID_PREFIX + "01", 64L, 4.2);
        createLeaderboardSample("pilot02", DEMO_LOGIN_ID_PREFIX + "02", 72L, 6.8);
        createLeaderboardSample("pilot03", DEMO_LOGIN_ID_PREFIX + "03", 89L, 5.4);
        createLeaderboardSample("pilot04", DEMO_LOGIN_ID_PREFIX + "04", 101L, 9.1);
        createLeaderboardSample("pilot05", DEMO_LOGIN_ID_PREFIX + "05", 118L, 7.6);
        createLeaderboardSample("pilot06", DEMO_LOGIN_ID_PREFIX + "06", 135L, 12.4);
        createLeaderboardSample("pilot07", DEMO_LOGIN_ID_PREFIX + "07", 156L, 10.8);
        createLeaderboardSample("pilot08", DEMO_LOGIN_ID_PREFIX + "08", 180L, 15.2);
    }

    private void createLeaderboardSample(
            String name,
            String loginId,
            Long totalFlightTime,
            Double averageTilt
    ) {
        Members member = memberRepository.save(
                Members.builder()
                        .name(name)
                        .loginId(loginId)
                        .password(passwordEncoder.encode("demo1234"))
                        .build()
        );

        double totalScore = LeaderboardScores.angleScore(averageTilt)
                + LeaderboardScores.timeScore(totalFlightTime);

        Orders order = Orders.builder()
                .member(member)
                .build();
        order.updateLeaderboardScore(
                totalScore,
                totalFlightTime,
                averageTilt
        );

        orderRepository.save(order);
    }

    private void normalizeLeaderboardScores() {
        orderRepository.findByScoreIsNotNull()
                .forEach(order -> order.updateLeaderboardScore(
                        LeaderboardScores.angleScore(order.getAverageTilt())
                                + LeaderboardScores.timeScore(order.getTotalFlightTime()),
                        order.getTotalFlightTime(),
                        order.getAverageTilt()
                ));
    }
}
