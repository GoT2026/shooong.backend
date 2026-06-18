package io.rapa.shooongbackend.ranking.controller;

import io.rapa.shooongbackend.common.constant.SuccessCode;
import io.rapa.shooongbackend.common.dto.ApiResult;
import io.rapa.shooongbackend.ranking.dto.RankingResponse;
import io.rapa.shooongbackend.ranking.service.RankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(
        name = "Ranking",
        description = "랭킹 관련 API"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rankings")
public class RankingController {
    private final RankingService rankingService;

    @Operation(
            summary = "랭킹 조회",
            description = "점수가 계산된 주문 기준으로 랭킹을 조회하는 API. 현재 courseId는 호환성을 위해 받지만 코스 도메인이 없어 필터링에는 사용하지 않습니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(name = "courseId", description = "코스 ID. 현재 미사용"),
                    @Parameter(name = "limit", description = "조회 개수. 기본 10, 최대 100")
            }
    )
    @ApiResponse(
            responseCode = "200",
            description = "랭킹 조회 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = @ExampleObject(
                            """
                            {
                                "statusCode": "200 OK",
                                "message": "랭킹이 조회되었습니다.",
                                "data": [
                                    {
                                        "rank": 1,
                                        "orderId": 1,
                                        "userName": "테스트유저",
                                        "angleScore": 48.1,
                                        "timeScore": 49.4,
                                        "totalScore": 97.5,
                                        "totalFlightTime": 83,
                                        "averageTilt": 5.2,
                                        "orderStatus": "COMPLETED"
                                    }
                                ]
                            }
                            """
                    )
            )
    )
    @GetMapping
    public ResponseEntity<ApiResult<List<RankingResponse>>> getRankings(
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) Integer limit
    ) {
        return ApiResult.data(
                SuccessCode.RANKING_RETRIEVE,
                rankingService.getRankings(courseId, limit)
        );
    }
}
