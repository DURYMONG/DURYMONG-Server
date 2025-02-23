package konkuk.kuit.durimong.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import konkuk.kuit.durimong.domain.user.dto.request.*;
import konkuk.kuit.durimong.domain.user.dto.request.signup.UserEmailReq;
import konkuk.kuit.durimong.domain.user.dto.request.signup.UserSignUpReq;
import konkuk.kuit.durimong.domain.user.dto.response.*;
import konkuk.kuit.durimong.domain.user.service.FcmService;
import konkuk.kuit.durimong.domain.user.service.NotificationService;
import konkuk.kuit.durimong.domain.user.service.UserService;
import konkuk.kuit.durimong.global.annotation.CustomExceptionDescription;
import konkuk.kuit.durimong.global.annotation.UserId;
import konkuk.kuit.durimong.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static konkuk.kuit.durimong.global.config.swagger.SwaggerResponseDescription.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("users")
public class UserController {
    private final UserService userService;
    private final FcmService fcmService;
    private final NotificationService notificationService;

    @Operation(summary = "아이디 중복검사", description = "아이디 중복여부를 확인합니다.")
    @CustomExceptionDescription(USER_ID)
    @Tag(name = "Sign Up", description = "회원가입 관련 API")
    @GetMapping("userid/{userId}")
    public SuccessResponse<String> getId(@PathVariable String userId){
        return SuccessResponse.ok(userService.validateId(userId));
    }

    @Operation(summary = "이메일 중복검사", description = "이메일 중복여부를 확인합니다.")
    @CustomExceptionDescription(USER_EMAIL)
    @Tag(name = "Sign Up", description = "회원가입 관련 API")
    @GetMapping("email/{email}")
    public SuccessResponse<String> getEmail(@PathVariable String email){
        return SuccessResponse.ok(userService.validateEmail(email));
    }

    @Operation(summary = "비밀번호 유효성 검사", description = "비밀번호의 유효성(길이,영문 + 숫자)을 검사합니다.")
    @CustomExceptionDescription(USER_PASSWORD)
    @Tag(name = "Sign Up", description = "회원가입 관련 API")
    @GetMapping("password/{password}")
    public SuccessResponse<String> getPassword(@PathVariable String password){
        return SuccessResponse.ok(userService.validatePassword(password));
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
    @GetMapping("email-verification/{email}/{authCode}")
    public SuccessResponse<String> emailVerification(@PathVariable String email, @PathVariable String authCode){
        return SuccessResponse.ok(userService.verifyCode(email, authCode));
    }

    @Operation(summary = "회원가입",description = "유저가 회원가입을 합니다.")
    @CustomExceptionDescription(USER_SIGNUP)
    @Tag(name = "Sign Up", description = "회원가입 관련 API")
    @PostMapping("signup")
    public SuccessResponse<String> signup(
            @Validated @RequestBody UserSignUpReq req){
        return SuccessResponse.ok(userService.register(req));
    }

    @Operation(summary = "홈 화면", description = "홈 화면을 조회합니다.")
    @CustomExceptionDescription(USER_HOME)
    @GetMapping("home")
    public SuccessResponse<UserHomeRes> getHomePage(
            @Parameter(hidden = true) @UserId Long userId){
        return SuccessResponse.ok(userService.homePage(userId));
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
    @Tag(name = "Chat with Mong", description = "몽과의 대화 관련 API")
    @CustomExceptionDescription(USER_ANSWER)
    @PostMapping("answer-mong")
    public SuccessResponse<String> mongConversation(@RequestBody @Validated UserMongConversationReq req,
                                                    @Parameter(hidden = true) @UserId Long userId){
        return SuccessResponse.ok(userService.userAnswer(req,userId));
    }

    @Operation(summary = "답변 다시하기", description = "유저가 몽의 질문에 했던 답변을 수정합니다.")
    @Tag(name = "Chat with Mong", description = "몽과의 대화 관련 API")
    @CustomExceptionDescription(USER_EDIT_ANSWER)
    @PostMapping("answer-modification")
    public SuccessResponse<String> answerModification(@RequestBody @Validated UserEditAnswerReq req,
                                                      @Parameter(hidden = true) @UserId Long userId){
        return SuccessResponse.ok(userService.editAnswer(req,userId));
    }

    @Operation(summary = "몽과의 대화 기록 보기", description = "몽과의 대화 기록을 조회합니다.")
    @Tag(name = "Chat with Mong", description = "몽과의 대화 관련 API")
    @CustomExceptionDescription(USER_CHAT_HISTORY)
    @GetMapping("mong-chat-history")
    public SuccessResponse<UserChatHistoryRes> showChatHistory(
            @Parameter(hidden = true) @UserId Long userId){
        return SuccessResponse.ok(userService.showChatHistory(userId));
    }

    @Operation(summary = "몽과의 대화 기록 삭제", description = "몽과의 대화 기록을 삭제합니다.")
    @Tag(name = "Chat with Mong", description = "몽과의 대화 관련 API")
    @CustomExceptionDescription(USER_DELETE_CHAT)
    @PostMapping("mong-chat-elimination")
    public SuccessResponse<String> deleteChat(@RequestBody @Validated UserDeleteChatReq req){
        return SuccessResponse.ok(userService.deleteChat(req));
    }

    @Operation(summary = "알림 설정 화면",description = "알림 설정 화면 접속 시 호출되는 API입니다.")
    @Tag(name = "User Notification", description = "푸시알림 설정 관련 API")
    @CustomExceptionDescription(USER_NOTIFICATION)
    @GetMapping("notification")
    public SuccessResponse<NotificationSettingFormRes> notificationSettingForm(
            @Parameter(hidden = true) @UserId Long userId
    ){
        return SuccessResponse.ok(userService.notificationSettingForm(userId));
    }

    @Operation(summary = "일별 몽 대화기록 조회", description = "원하는 날짜의 몽과의 대화 내역을 조회합니다.")
    @Tag(name = "User Record", description = "유저 기록 조회 관련 API")
    @CustomExceptionDescription(USER_DAILY_CHAT)
    @GetMapping("daily-mong-chat/{targetDate}")
    public SuccessResponse<UserDailyChatRes> dailyChat(
            @PathVariable LocalDate targetDate,
            @Parameter(hidden = true) @UserId Long userId){
        return SuccessResponse.ok(userService.userDailyChat(userId,targetDate));
    }

    @Operation(summary = "기록 지우기", description = "유저와 몽의 대화 기록, 채팅봇 상담내역을 삭제합니다.")
    @Tag(name = "User Record", description = "유저 기록 조회 관련 API")
    @CustomExceptionDescription(USER_DELETE_HISTORY)
    @PostMapping("history-deletion")
    public SuccessResponse<String> deleteHistory(
            @Parameter(hidden = true) @UserId Long userId){
        return SuccessResponse.ok(userService.deleteHistory(userId));
    }

    @Operation(summary = "채팅봇 상담내역 선택", description = "유저가 해당 날짜의 어떤 채팅봇과의 상담 내역을 조회할 지 선택합니다.")
    @Tag(name = "User Record", description = "유저 기록 조회 관련 API")
    @CustomExceptionDescription(DAILY_BOT_CHAT_CHOICE)
    @GetMapping("daily-bot-chat-choice/{targetDate}")
    public SuccessResponse<UserDailyBotChatChoiceRes> dailyBotChatChoice(@PathVariable LocalDate targetDate){
        return SuccessResponse.ok(userService.showBotChatHistory(targetDate));
    }

    @Operation(summary = "채팅봇 상담내역 조회", description = "유저가 해당 날짜의 채팅봇 상담 내역을 조회합니다.")
    @Tag(name = "User Record", description = "유저 기록 조회 관련 API")
    @CustomExceptionDescription(DAILY_BOT_CHAT)
    @GetMapping("daily-bot-chat/{targetDate}/{chatBotId}")
    public SuccessResponse<UserDailyBotChatRes> getDailyBotChat(@PathVariable LocalDate targetDate, @PathVariable Long chatBotId,
                                                                @Parameter(hidden = true) @UserId Long userId){
        return SuccessResponse.ok(userService.userDailyBotChat(targetDate, chatBotId, userId));
    }

    @Operation(summary = "FCM 토큰 저장", description = "클라이언트로부터 전달받은 FCM 토큰을 저장합니다.")
    @Tag(name = "User Notification", description = "푸시알림 설정 관련 API")
    @CustomExceptionDescription(SAVE_FCM_TOKEN)
    @PostMapping("fcm-token")
    public SuccessResponse<Void> updateFcmToken(@Parameter(hidden = true) @UserId Long userId,@RequestParam String fcmToken){
        fcmService.updateFcmToken(userId,fcmToken);
        return SuccessResponse.ok(null);
    }

    @Operation(summary = "푸시알림 설정", description = "푸시알림 수신 여부를 설정합니다.")
    @Tag(name = "User Notification", description = "푸시알림 설정 관련 API")
    @CustomExceptionDescription(SET_PUSH)
    @PostMapping("setting-notification")
    public SuccessResponse<Void> setPush(@Parameter(hidden = true) @UserId Long userId){
        userService.setPushEnabled(userId);
        return SuccessResponse.ok(null);
    }

    @Operation(summary = "테스트용 수동 푸시알림", description = "테스트용입니다.")
    @Tag(name = "User Notification", description = "푸시알림 설정 관련 API")
    @PostMapping("/test-push")
    public SuccessResponse<Void> triggerPushManually() {
        notificationService.sendPushNotificationsToInactiveUsers();
        return SuccessResponse.ok(null);
    }

}
