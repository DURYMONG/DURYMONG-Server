package konkuk.kuit.durimong.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import konkuk.kuit.durimong.domain.user.dto.request.signup.UserEmailReq;
import konkuk.kuit.durimong.domain.user.dto.request.signup.UserIdReq;
import konkuk.kuit.durimong.domain.user.dto.request.signup.UserPasswordReq;
import konkuk.kuit.durimong.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

    @Operation(summary = "아이디 중복검사", description = "아이디 중복여부를 확인합니다.")
    @PostMapping("userid")
    public String getId(UserIdReq req){
        return userService.validateId(req.getId());
    }

    @Operation(summary = "이메일 중복검사", description = "이메일 중복여부를 확인합니다.")
    @PostMapping("email")
    public String getEmail(UserEmailReq req){
        return userService.validateEmail(req.getEmail());
    }

    @Operation(summary = "비밀번호 유효성 검사", description = "비밀번호의 유효성(길이,대소문자,특수문자 여부)을 검사합니다.")
    @PostMapping("password")
    public String getPassword(UserPasswordReq req){
        return userService.validatePassword(req.getPassword());
    }

}
