package konkuk.kuit.durimong.domain.mong.controller;

import io.swagger.v3.oas.annotations.Operation;
import konkuk.kuit.durimong.domain.mong.dto.request.MongCreateReq;
import konkuk.kuit.durimong.domain.mong.service.MongService;
import konkuk.kuit.durimong.global.annotation.CustomExceptionDescription;
import konkuk.kuit.durimong.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static konkuk.kuit.durimong.global.config.swagger.SwaggerResponseDescription.MONG_NAME;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("mongs")
public class MongController {
    private final MongService mongService;

    @Operation(summary = "몽 이름 검사",description = "몽의 이름이 길이 제한에 맞는지 검사합니다.")
    @CustomExceptionDescription(MONG_NAME)
    @GetMapping("name")
    public SuccessResponse<String> setMongName(
            @Validated MongCreateReq req){
        return SuccessResponse.ok(mongService.validateName(req));
    }
}
