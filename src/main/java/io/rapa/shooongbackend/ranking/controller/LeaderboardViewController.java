package io.rapa.shooongbackend.ranking.controller;

import io.rapa.shooongbackend.ranking.dto.RankingResponse;
import io.rapa.shooongbackend.ranking.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class LeaderboardViewController {
    private final RankingService rankingService;

    @GetMapping("/leaderboard")
    public String leaderboard(
            @RequestParam(required = false) Integer limit,
            Model model
    ) {
        List<RankingResponse> rankings = rankingService.getRankings(null, limit);

        model.addAttribute("rankings", rankings);
        model.addAttribute("latestResult", rankings.isEmpty() ? null : rankings.get(0));
        model.addAttribute("limit", limit == null ? 10 : limit);

        return "index";
    }
}
