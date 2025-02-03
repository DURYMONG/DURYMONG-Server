package konkuk.kuit.durimong.domain.activity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import konkuk.kuit.durimong.domain.activity.dto.request.CheckActivityReq;
import konkuk.kuit.durimong.domain.activity.dto.request.WriteDiaryReq;
import konkuk.kuit.durimong.domain.activity.dto.response.*;
import konkuk.kuit.durimong.domain.activity.service.ActivityService;
import konkuk.kuit.durimong.domain.test.entity.Test;
import konkuk.kuit.durimong.global.annotation.CustomExceptionDescription;
import konkuk.kuit.durimong.global.annotation.UserId;
import konkuk.kuit.durimong.global.config.swagger.SwaggerResponseDescription;
import konkuk.kuit.durimong.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

    @PostMapping("/user-records")
    @Operation(summary = "활동 완료 체크", description = "완료한 활동에 대해 체크표시 합니다.")
    @CustomExceptionDescription(ACTIVITY_EXIST)
    public SuccessResponse<CheckActivityRes> checkActivity(
            @Validated @RequestBody CheckActivityReq req, @UserId Long userId) {
        log.info("Extracted userId from JWT: {}", userId);  // userId 확인 로그 추가

        return SuccessResponse.ok(activityService.makeUserRecord(req, userId));
    }

    @DeleteMapping("/user-records/{userRecordId}")
    @Operation(summary = "활동 체크 취소", description = "활동 완료체크를 취소합니다")
    @CustomExceptionDescription(USER_RECORD)
    public ResponseEntity<Void> deleteActivity(
            @PathVariable Long userRecordId, @UserId Long userId) {
        log.info("Extracted userId from JWT: {}", userId);
        log.info("Deleting user record with ID: {}", userRecordId);

        activityService.deleteUserRecord(userRecordId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/records")
    @Operation(summary = "월별 성장 일지 조회", description = "월별 성장일지를 조회합니다.")
    @CustomExceptionDescription(USER_RECORD_DATE)
    public SuccessResponse<ActivityRecordRes> viewMonthActivityRecord(
            @RequestParam @Parameter(description = "년도", example = "2024") int year, @Parameter(description = "월", example = "11") int month, @Parameter(hidden = true) @UserId Long userId) {

        return SuccessResponse.ok(activityService.getMonthActivityRecord(year, month, userId));
    }

    @GetMapping("/records/{date}")
    @Operation(summary = "일별 성장 일지 조회", description = "일별 성장일지를 조회합니다.")
    @CustomExceptionDescription(USER_RECORD_DAY)
    public SuccessResponse<ActivityDayRecordRes> viewDayActivityRecord(@PathVariable LocalDate date, @Parameter(hidden = true) @UserId Long userId) {

        return SuccessResponse.ok(activityService.getDayActivityRecord(date, userId));
    }

    @GetMapping("/records/{date}/diaries")
    @Operation(summary = "하루 일기 조회", description = "하루 일기를 조회합니다.")
    @CustomExceptionDescription(Diary)
    public SuccessResponse<DiaryRes> viewDayDiary(@PathVariable LocalDate date, @Parameter(hidden = true) @UserId Long userId) {

        return SuccessResponse.ok(activityService.getDiary(date, userId));
    }

    @PostMapping("/records/diaries")
    @Operation(summary = "일기 작성", description = "당일에 일기를 작성합니다.")
    @CustomExceptionDescription(Diary)
    public SuccessResponse<DiaryRes> writeDayDiary(
            @Validated @RequestBody WriteDiaryReq req, @Parameter(hidden = true) @UserId Long userId) {

        return SuccessResponse.ok(activityService.writeDiary(req,userId));
    }

}
