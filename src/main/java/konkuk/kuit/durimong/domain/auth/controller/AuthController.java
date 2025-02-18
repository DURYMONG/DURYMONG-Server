package konkuk.kuit.durimong.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import konkuk.kuit.durimong.domain.auth.service.AuthService;
import konkuk.kuit.durimong.domain.auth.dto.request.ReIssueTokenReq;
import konkuk.kuit.durimong.domain.auth.dto.request.UserLoginReq;
import konkuk.kuit.durimong.domain.auth.dto.response.ReIssueTokenRes;
import konkuk.kuit.durimong.domain.auth.dto.response.UserTokenRes;
import konkuk.kuit.durimong.global.annotation.CustomExceptionDescription;
import konkuk.kuit.durimong.global.annotation.UserId;
import konkuk.kuit.durimong.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static konkuk.kuit.durimong.global.config.swagger.SwaggerResponseDescription.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("user")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "로그인", description = "유저가 로그인을 합니다.")
    @CustomExceptionDescription(USER_LOGIN)
    @Tag(name = "Login", description = "로그인 관련 API")
    @PostMapping("login")
    public SuccessResponse<UserTokenRes> login(
            @Validated @RequestBody UserLoginReq req){
        return SuccessResponse.ok(authService.login(req));
    }

    @Operation(summary = "토큰 재발급", description = "토큰 유효기간 만료 시 호출되는 API입니다.")
    @CustomExceptionDescription(REISSUE_TOKEN)
    @Tag(name = "Login", description = "로그인 관련 API")
    @PostMapping("newtokens")
    public SuccessResponse<ReIssueTokenRes> reIssueTokens(
            @Validated @RequestBody ReIssueTokenReq req
    ){
        return SuccessResponse.ok(authService.reissueToken(req));
    }

    @Operation(summary = "로그아웃", description = "유저가 로그아웃을 합니다.")
    @CustomExceptionDescription(USER_LOGOUT)
    @Tag(name = "Login", description = "로그인 관련 API")
    @PostMapping("logout")
    public SuccessResponse<String> logout(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization",required = false) String token,
            @Parameter(hidden = true) @UserId Long userId){

        String accessToken = token.replace("Bearer ", "");
        return SuccessResponse.ok(authService.logout(accessToken,userId));
    }
}
