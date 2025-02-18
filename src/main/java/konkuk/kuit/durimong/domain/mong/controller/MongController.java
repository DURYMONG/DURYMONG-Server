package konkuk.kuit.durimong.domain.mong.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import konkuk.kuit.durimong.domain.mong.dto.request.MongCreateReq;
import konkuk.kuit.durimong.domain.mong.dto.request.MongNameReq;
import konkuk.kuit.durimong.domain.mong.dto.response.MongGrowthRes;
import konkuk.kuit.durimong.domain.mong.service.MongService;
import konkuk.kuit.durimong.domain.user.repository.UserRepository;
import konkuk.kuit.durimong.global.annotation.CustomExceptionDescription;
import konkuk.kuit.durimong.global.annotation.UserId;
import konkuk.kuit.durimong.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static konkuk.kuit.durimong.global.config.swagger.SwaggerResponseDescription.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("mongs")
public class MongController {
    private final MongService mongService;
    private final UserRepository userRepository;

    @Operation(summary = "몽 이름 검사",description = "몽의 이름이 길이 제한에 맞는지 검사합니다.")
    @CustomExceptionDescription(MONG_NAME)
    @Tag(name = "Mong Creation", description = "몽 생성 관련 API")
    @GetMapping("name")
    public SuccessResponse<String> setMongName(
            @Validated MongNameReq req){
        return SuccessResponse.ok(mongService.validateName(req));
    }

    @Operation(summary = "몽 생성", description = "몽을 생성합니다.")
    @CustomExceptionDescription(MONG_CREATE)
    @Tag(name = "Mong Creation", description = "몽 생성 관련 API")
    @PostMapping("creation")
    public SuccessResponse<String> createMong(
            @Validated @RequestBody MongCreateReq req,
            @Parameter(hidden = true) @UserId Long userId){
        return SuccessResponse.ok(mongService.createMong(req,userId));
    }

    @Operation(summary = "몽 키우기", description = "사용자의 활동 기록을 조회하여 몽을 성장시킵니다.")
    @Tag(name ="Mong Growth", description = "두리몽 키우기 API")
    @CustomExceptionDescription(MONG_GROWTH)
    @PostMapping("growth")
    public SuccessResponse<MongGrowthRes> growMong(@Parameter(hidden = true) @UserId Long userId){
        return SuccessResponse.ok(mongService.growMong(userId));
    }


}
