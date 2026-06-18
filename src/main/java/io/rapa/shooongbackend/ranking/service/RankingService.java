package io.rapa.shooongbackend.ranking.service;

import io.rapa.shooongbackend.order.repository.OrderRepository;
import io.rapa.shooongbackend.ranking.dto.RankingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RankingService {
    private static final int DEFAULT_LIMIT = 10;
    private static final int MAX_LIMIT = 100;

    private final OrderRepository orderRepository;

    public List<RankingResponse> getRankings(Long courseId, Integer limit) {
        int resolvedLimit = resolveLimit(limit);
        AtomicLong rank = new AtomicLong(1);

        return orderRepository.findByScoreIsNotNullOrderByScoreDescTotalFlightTimeAsc(
                        PageRequest.of(0, resolvedLimit)
                ).stream()
                .map(order -> RankingResponse.of(rank.getAndIncrement(), order))
                .toList();
    }

    private int resolveLimit(Integer limit) {
        if (limit == null || limit <= 0) {
            return DEFAULT_LIMIT;
        }
        return Math.min(limit, MAX_LIMIT);
    }
}
