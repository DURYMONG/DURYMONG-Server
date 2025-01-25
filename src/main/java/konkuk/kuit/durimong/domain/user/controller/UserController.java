package konkuk.kuit.durimong.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import konkuk.kuit.durimong.domain.user.dto.request.UserEditAnswerReq;
import konkuk.kuit.durimong.domain.user.dto.request.UserEditPasswordReq;
import konkuk.kuit.durimong.domain.user.dto.request.UserInfoReq;
import konkuk.kuit.durimong.domain.user.dto.request.UserMongConversationReq;
import konkuk.kuit.durimong.domain.user.dto.request.login.ReIssueTokenReq;
import konkuk.kuit.durimong.domain.user.dto.request.login.UserLoginReq;
import konkuk.kuit.durimong.domain.user.dto.request.signup.*;
import konkuk.kuit.durimong.domain.user.dto.response.ReIssueTokenRes;
import konkuk.kuit.durimong.domain.user.dto.response.UserHomeRes;
import konkuk.kuit.durimong.domain.user.dto.response.UserTokenRes;
import konkuk.kuit.durimong.domain.user.dto.response.UserUnRegisterRes;
import konkuk.kuit.durimong.domain.user.service.UserService;
import konkuk.kuit.durimong.global.annotation.CustomExceptionDescription;
import konkuk.kuit.durimong.global.annotation.UserId;
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
    @Tag(name = "Sign Up", description = "회원가입 관련 API")
    @GetMapping("userid")
    public SuccessResponse<String> getId(UserIdReq req){
        return SuccessResponse.ok(userService.validateId(req.getId()));
    }

    @Operation(summary = "이메일 중복검사", description = "이메일 중복여부를 확인합니다.")
    @CustomExceptionDescription(USER_EMAIL)
    @Tag(name = "Sign Up", description = "회원가입 관련 API")
    @GetMapping("email")
    public SuccessResponse<String> getEmail(UserEmailReq req){
        return SuccessResponse.ok(userService.validateEmail(req.getEmail()));
    }

    @Operation(summary = "비밀번호 유효성 검사", description = "비밀번호의 유효성(길이,영문 + 숫자)을 검사합니다.")
    @CustomExceptionDescription(USER_PASSWORD)
    @Tag(name = "Sign Up", description = "회원가입 관련 API")
    @GetMapping("password")
    public SuccessResponse<String> getPassword(UserPasswordReq req){
        return SuccessResponse.ok(userService.validatePassword(req.getPassword()));
    }

    @Operation(summary = "이메일 인증번호 전송",description = "유저의 이메일로 인증번호를 전송합니다.")
    @Tag(name = "Sign Up", description = "회원가입 관련 API")
    @PostMapping("email-requests")
    public SuccessResponse<String> requestEmail(UserEmailReq req){
        return SuccessResponse.ok(userService.sendCodeToEmail(req.getEmail()));
    }

    @Operation(summary = "이메일 인증번호 확인", description = "유저에게 전송한 인증번호와 유저가 입력한 인증번호 일치 여부를 확인합니다.")
    @CustomExceptionDescription(USER_EMAIL_VERIFICATION)
    @Tag(name = "Sign Up", description = "회원가입 관련 API")
    @GetMapping("email-verifications")
    public SuccessResponse<String> emailVerification(UserEmailVerifyReq req){
        return SuccessResponse.ok(userService.verifyCode(req.getEmail(),req.getAuthCode()));
    }

    @Operation(summary = "회원가입",description = "유저가 회원가입을 합니다.")
    @CustomExceptionDescription(USER_SIGNUP)
    @Tag(name = "Sign Up", description = "회원가입 관련 API")
    @PostMapping("signup")
    public SuccessResponse<String> signup(
            @Validated @RequestBody UserSignUpReq req){
        return SuccessResponse.ok(userService.register(req));
    }

    @Operation(summary = "로그인", description = "유저가 로그인을 합니다.")
    @CustomExceptionDescription(USER_LOGIN)
    @Tag(name = "Login", description = "로그인 관련 API")
    @PostMapping("login")
    public SuccessResponse<UserTokenRes> login(
            @Validated @RequestBody UserLoginReq req){
        return SuccessResponse.ok(userService.login(req));
    }

    @Operation(summary = "토큰 재발급", description = "토큰 유효기간 만료 시 호출되는 API입니다.")
    @CustomExceptionDescription(REISSUE_TOKEN)
    @Tag(name = "Login", description = "로그인 관련 API")
    @PostMapping("newtokens")
    public SuccessResponse<ReIssueTokenRes> reIssueTokens(
            @Validated @RequestBody ReIssueTokenReq req
    ){
        return SuccessResponse.ok(userService.reissueToken(req));
    }

    @Operation(summary = "홈 화면", description = "홈 화면을 조회합니다.")
    @CustomExceptionDescription(USER_HOME)
    @GetMapping("home")
    public SuccessResponse<UserHomeRes> getHomePage(
            @Parameter(hidden = true) @UserId Long userId){
        return SuccessResponse.ok(userService.homePage(userId));
    }

    @Operation(summary = "로그아웃", description = "유저가 로그아웃을 합니다.")
    @CustomExceptionDescription(USER_LOGOUT)
    @Tag(name = "Login", description = "로그인 관련 API")
    @PostMapping("logout")
    public SuccessResponse<String> logout(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization",required = false) String token,
            @Parameter(hidden = true) @UserId Long userId){

        String accessToken = token.replace("Bearer ", "");
        return SuccessResponse.ok(userService.logout(accessToken,userId));
    }

    @Operation(summary = "회원 정보 수정", description = "유저의 이름과 몽의 이름을 수정합니다.")
    @CustomExceptionDescription(USER_EDIT)
    @Tag(name = "Edit Info", description = "정보 수정 관련 API")
    @PostMapping("info-modification")
    public SuccessResponse<String> modifyUserInfo(
            @Parameter(hidden = true) @UserId Long userId,
            @Validated @RequestBody UserInfoReq req){
        return SuccessResponse.ok(userService.editUserInfo(req,userId));
    }

    @Operation(summary = "비밀번호 수정", description = "비밀번호를 수정합니다.")
    @Tag(name = "Edit Info", description = "정보 수정 관련 API")
    @CustomExceptionDescription(USER_EDIT_PWD)
    @PostMapping("password-modification")
    public SuccessResponse<String> modifyPassword(
            @Validated @RequestBody UserEditPasswordReq req,
            @Parameter(hidden = true) @UserId Long userId
    ){
        return SuccessResponse.ok(userService.editUserPassword(req,userId));
    }

    @Operation(summary = "회원 탈퇴 화면", description = "회원 탈퇴를 할 때 표시되는 화면에 대한 API입니다.")
    @CustomExceptionDescription(USER_UNREGISTER)
    @Tag(name = "User UnRegister", description = "회원 탈퇴 관련 API")
    @GetMapping("user-elimination")
    public SuccessResponse<UserUnRegisterRes> userElimination(
            @Parameter(hidden = true) @UserId Long userId){
        return SuccessResponse.ok(userService.showUnRegister(userId));
    }

    @Operation(summary = "회원 탈퇴", description = "유저가 회원 탈퇴를 합니다.")
    @Tag(name = "User UnRegister", description = "회원 탈퇴 관련 API")
    @CustomExceptionDescription(USER_ELIMINATE)
    @PostMapping("user-elimination")
    public SuccessResponse<String> unregister(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization",required = false) String token,
            @Parameter(hidden = true) @UserId Long userId){

        String accessToken = token.replace("Bearer ", "");
        return SuccessResponse.ok(userService.unRegister(accessToken,userId));
    }

    @Operation(summary = "몽과의 대화", description = "유저가 몽의 질문에 답변을 작성합니다.")
    @Tag(name = "Mong Conversation", description = "몽과의 대화 관련 API")
    @CustomExceptionDescription(USER_ANSWER)
    @PostMapping("mong-conversation")
    public SuccessResponse<String> mongConversation(@RequestBody @Validated UserMongConversationReq req,
                                                    @Parameter(hidden = true) @UserId Long userId){
        return SuccessResponse.ok(userService.userAnswer(req,userId));
    }

    @Operation(summary = "답변 다시하기", description = "유저가 몽의 질문에 했던 답변을 수정합니다.")
    @Tag(name = "Mong Conversation", description = "몽과의 대화 관련 API")
    @CustomExceptionDescription(USER_EDIT_ANSWER)
    @PostMapping("answer-modification")
    public SuccessResponse<String> answerModification(@RequestBody @Validated UserEditAnswerReq req,
                                                      @Parameter(hidden = true) @UserId Long userId){
        return SuccessResponse.ok(userService.editAnswer(req,userId));
    }

}
