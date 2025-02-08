package konkuk.kuit.durimong.domain.test.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import konkuk.kuit.durimong.domain.test.dto.response.DoTestRes;
import konkuk.kuit.durimong.domain.test.dto.response.TestDescriptionRes;
import konkuk.kuit.durimong.domain.test.service.TestService;
import konkuk.kuit.durimong.global.annotation.CustomExceptionDescription;
import konkuk.kuit.durimong.global.annotation.UserId;
import konkuk.kuit.durimong.global.config.swagger.SwaggerResponseDescription;
import konkuk.kuit.durimong.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static konkuk.kuit.durimong.global.config.swagger.SwaggerResponseDescription.TEST_DESCRIPTION;
import static konkuk.kuit.durimong.global.config.swagger.SwaggerResponseDescription.TEST_START;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("tests")
public class TestController {
    private final TestService testService;

    @GetMapping("/{testId}")
    @Operation(summary = "테스트 상세정보 조회", description = "testId에 해당하는 테스트 정보와 유저의 직전 검사 기록을 확인합니다.")
    @CustomExceptionDescription(TEST_DESCRIPTION)
    public SuccessResponse<TestDescriptionRes> viewTestInfo(@PathVariable Long testId, @Parameter(hidden = true) @UserId Long userId) {

        return SuccessResponse.ok(testService.getTestDescription(testId, userId));
    }

    @PostMapping("/{testId}")
    @Operation(summary = "테스트 검사 시작", description = "testId의 테스트를 시작합니다, 모든 테스트 문항들이 반환됩니다")
    @CustomExceptionDescription(TEST_START)
    public SuccessResponse<DoTestRes> startTest(@PathVariable Long testId) {

        return SuccessResponse.ok(testService.getTest(testId));
    }
}
