package konkuk.kuit.durimong.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import konkuk.kuit.durimong.domain.user.dto.request.login.ReIssueTokenReq;
import konkuk.kuit.durimong.domain.user.dto.request.login.UserLoginReq;
import konkuk.kuit.durimong.domain.user.dto.request.signup.*;
import konkuk.kuit.durimong.domain.user.dto.response.ReIssueTokenRes;
import konkuk.kuit.durimong.domain.user.dto.response.UserTokenRes;
import konkuk.kuit.durimong.domain.user.service.UserService;
import konkuk.kuit.durimong.global.annotation.CustomExceptionDescription;
import konkuk.kuit.durimong.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static konkuk.kuit.durimong.global.config.swagger.SwaggerResponseDescription.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    @Operation(summary = "아이디 중복검사", description = "아이디 중복여부를 확인합니다.")
    @CustomExceptionDescription(USER_ID)
    @GetMapping("userid")
    public SuccessResponse<String> getId(UserIdReq req){
        return SuccessResponse.ok(userService.validateId(req.getId()));
    }

    @Operation(summary = "이메일 중복검사", description = "이메일 중복여부를 확인합니다.")
    @CustomExceptionDescription(USER_EMAIL)
    @GetMapping("email")
    public SuccessResponse<String> getEmail(UserEmailReq req){
        return SuccessResponse.ok(userService.validateEmail(req.getEmail()));
    }

    @Operation(summary = "비밀번호 유효성 검사", description = "비밀번호의 유효성(길이,영문 + 숫자)을 검사합니다.")
    @CustomExceptionDescription(USER_PASSWORD)
    @GetMapping("password")
    public SuccessResponse<String> getPassword(UserPasswordReq req){
        return SuccessResponse.ok(userService.validatePassword(req.getPassword()));
    }

    @Operation(summary = "이메일 인증번호 전송",description = "유저의 이메일로 인증번호를 전송합니다.")
    @PostMapping("email-requests")
    public SuccessResponse<String> requestEmail(UserEmailReq req){
        return SuccessResponse.ok(userService.sendCodeToEmail(req.getEmail()));
    }

    @Operation(summary = "이메일 인증번호 확인", description = "유저에게 전송한 인증번호와 유저가 입력한 인증번호 일치 여부를 확인합니다.")
    @CustomExceptionDescription(USER_EMAIL_VERIFICATION)
    @GetMapping("email-verifications")
    public SuccessResponse<String> emailVerification(EmailVerifyReq req){
        return SuccessResponse.ok(userService.verifyCode(req.getEmail(),req.getAuthCode()));
    }

    @Operation(summary = "회원가입",description = "유저가 회원가입을 합니다.")
    @CustomExceptionDescription(USER_SIGNUP)
    @PostMapping("signup")
    public SuccessResponse<String> signup(
            @Validated @RequestBody UserSignUpReq req){
        return SuccessResponse.ok(userService.register(req));
    }

    @Operation(summary = "로그인", description = "유저가 로그인을 합니다.")
    @CustomExceptionDescription(USER_LOGIN)
    @PostMapping("login")
    public SuccessResponse<UserTokenRes> login(
            @Validated @RequestBody UserLoginReq req){
        return SuccessResponse.ok(userService.login(req));
    }

    @Operation(summary = "토큰 재발급", description = "토큰 유효기간 만료 시 호출되는 API입니다.")
    @CustomExceptionDescription(REISSUE_TOKEN)
    @PostMapping("newtokens")
    public SuccessResponse<ReIssueTokenRes> reIssueTokens(
            @Validated @RequestBody ReIssueTokenReq req
    ){
        return SuccessResponse.ok(userService.reissueToken(req));
    }
}
