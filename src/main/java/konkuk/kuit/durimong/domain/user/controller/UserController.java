package konkuk.kuit.durimong.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import konkuk.kuit.durimong.domain.user.dto.request.signup.*;
import konkuk.kuit.durimong.domain.user.service.UserService;
import konkuk.kuit.durimong.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    @Operation(summary = "아이디 중복검사", description = "아이디 중복여부를 확인합니다.")
    @PostMapping("userid")
    public SuccessResponse<String> getId(UserIdReq req){
        return SuccessResponse.ok(userService.validateId(req.getId()));
    }

    @Operation(summary = "이메일 중복검사", description = "이메일 중복여부를 확인합니다.")
    @PostMapping("email")
    public SuccessResponse<String> getEmail(UserEmailReq req){
        return SuccessResponse.ok(userService.validateEmail(req.getEmail()));
    }

    @Operation(summary = "비밀번호 유효성 검사", description = "비밀번호의 유효성(길이,영문 + 숫자)을 검사합니다.")
    @PostMapping("password")
    public SuccessResponse<String> getPassword(UserPasswordReq req){
        return SuccessResponse.ok(userService.validatePassword(req.getPassword()));
    }

    @Operation(summary = "이메일 인증번호 전송",description = "유저의 이메일로 인증번호를 전송합니다.")
    @PostMapping("email-requests")
    public SuccessResponse<String> requestEmail(UserEmailReq req){
        return SuccessResponse.ok(userService.sendCodeToEmail(req.getEmail()));
    }

    @Operation(summary = "이메일 인증번호 확인", description = "유저에게 전송한 인증번호와 유저가 입력한 인증번호 일치 여부를 확인합니다.")
    @GetMapping("email-verifications")
    public SuccessResponse<String> emailVerification(EmailVerifyReq req){
        return SuccessResponse.ok(userService.verifyCode(req.getEmail(),req.getAuthCode()));
    }

    @Operation(summary = "회원가입",description = "유저가 회원가입을 합니다.")
    @PostMapping("signup")
    public SuccessResponse<String> signup(
            @Validated @RequestBody UserSignUpReq req){
        return SuccessResponse.ok(userService.register(req));
    }
    
}
