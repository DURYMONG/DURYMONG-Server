package konkuk.kuit.durimong.domain.activity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import konkuk.kuit.durimong.domain.activity.dto.response.ActivityBoxDescriptionRes;
import konkuk.kuit.durimong.domain.activity.dto.response.ActivityDescriptionRes;
import konkuk.kuit.durimong.domain.activity.dto.response.ActivityTestListRes;
import konkuk.kuit.durimong.domain.activity.service.ActivityService;
import konkuk.kuit.durimong.domain.test.entity.Test;
import konkuk.kuit.durimong.global.annotation.CustomExceptionDescription;
import konkuk.kuit.durimong.global.annotation.UserId;
import konkuk.kuit.durimong.global.config.swagger.SwaggerResponseDescription;
import konkuk.kuit.durimong.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static konkuk.kuit.durimong.global.config.swagger.SwaggerResponseDescription.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("activities")
public class ActivityController {
    private final ActivityService activityService;

    @GetMapping
    @Operation(summary = "활동/테스트 리스트 조회", description = "JWT 토큰에서 사용자 ID를 추출하여 활동 및 테스트 리스트를 조회합니다.")
    @CustomExceptionDescription(ACTIVITY_TEST_LIST)
    public SuccessResponse<ActivityTestListRes> viewActivityTestList(
            @Parameter(hidden = true) @UserId Long userId) {
        log.info("Extracted userId from JWT: {}", userId);  // userId 확인 로그 추가

        return SuccessResponse.ok(activityService.getActivityTestList(userId));
    }

    @GetMapping("/{activityId}")
    @Operation(summary = "활동 내용 조회", description = "박스 존재 여부에 따라 형식을 나누어, 각 활동의 내용을 조회합니다.")
    @CustomExceptionDescription(ACTIVITY_EXIST)
    public SuccessResponse<?> viewActivity(@PathVariable Long activityId) {

        return SuccessResponse.ok(activityService.getActivityDetails(activityId));
    }
}
