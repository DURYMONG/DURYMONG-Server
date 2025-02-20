package konkuk.kuit.durimong.domain.chatbot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import konkuk.kuit.durimong.domain.chatbot.dto.request.ChatBotPredictReq;
import konkuk.kuit.durimong.domain.chatbot.dto.request.SaveChattingReq;
import konkuk.kuit.durimong.domain.chatbot.dto.response.*;
import konkuk.kuit.durimong.domain.chatbot.service.ChatBotService;
import konkuk.kuit.durimong.global.annotation.CustomExceptionDescription;
import konkuk.kuit.durimong.global.annotation.UserId;
import konkuk.kuit.durimong.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static konkuk.kuit.durimong.global.config.swagger.SwaggerResponseDescription.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("chatbots")
public class ChatBotController {
    private final ChatBotService chatBotService;

    @Operation(summary = "채팅봇 조회", description = "유저가 채팅봇을 선택하는 화면입니다.")
    @Tag(name = "Chat With Bot", description = "채팅봇 상담 관련 API")
    @CustomExceptionDescription(CHAT_BOT)
    @GetMapping()
    public SuccessResponse<List<ChatBotRes>> getChatBots() {
        return SuccessResponse.ok(chatBotService.getChatBots());
    }

    @Operation(summary = "채팅봇 상담 시작", description = "채팅봇이 먼저 말을 건네며 상담을 시작합니다.")
    @Tag(name = "Chat With Bot", description = "채팅봇 상담 관련 API")
    @CustomExceptionDescription(CHAT_START)
    @GetMapping("chat-start/{chatBotId}")
    public SuccessResponse<ChatBotChattingRes> startChat(
            @UserId @Parameter(hidden = true) Long userId, @PathVariable Long chatBotId) {
        return SuccessResponse.ok(chatBotService.getChatBotGreeting(chatBotId,userId));
    }

    @Operation(summary = "채팅봇 질환 추측", description = "유저가 선택한 증상을 기반으로 심적 질환을 추측합니다.")
    @Tag(name = "Chat With Bot", description = "채팅봇 상담 관련 API")
    @CustomExceptionDescription(CHAT_START)
    @GetMapping("prediction")
    public SuccessResponse<ChatBotPredictRes> botPrediction(
           @Validated ChatBotPredictReq req){
        return SuccessResponse.ok(chatBotService.analyzeMentalHealth(req));
    }

    @Operation(summary = "채팅봇 테스트 추천", description = "유저에게 테스트를 추천합니다.")
    @Tag(name = "Chat With Bot", description = "채팅봇 상담 관련 API")
    @CustomExceptionDescription(CHATBOT_RECOMMEND_TEST)
    @GetMapping("test-recommendation/{chatBotId}/{chatSessionId}")
    public SuccessResponse<ChatBotRecommendTestRes> testRecommendation(
            @PathVariable Long chatBotId,@PathVariable Long chatSessionId,
            @UserId @Parameter(hidden = true) Long userId){
        return SuccessResponse.ok(chatBotService.recommendTest(chatBotId,chatSessionId,userId));
    }

    @Operation(summary = "채팅봇 일기 추천", description = "유저에게 일기를 작성할 것을 권유합니다.")
    @Tag(name = "Chat With Bot", description = "채팅봇 상담 관련 API")
    @CustomExceptionDescription(CHATBOT_RECOMMEND_DIARY)
    @GetMapping("diary-recommendation/{chatBotId}/{chatSessionId}")
    public SuccessResponse<ChatBotRecommendDiaryRes> diaryRecommendation(
            @PathVariable Long chatBotId, @PathVariable Long chatSessionId)
    {
        return SuccessResponse.ok(chatBotService.recommendDiary(chatBotId,chatSessionId));
    }

    @Operation(summary = "채팅 저장하기", description = "채팅 내역을 저장하고, 대화를 종료합니다.")
    @Tag(name = "Chat With Bot", description = "채팅봇 상담 관련 API")
    @CustomExceptionDescription(CHATTING_END)
    @GetMapping("end-chat")
    public SuccessResponse<SaveChattingRes> endChat(SaveChattingReq req,
                                                    @UserId @Parameter(hidden = true) Long userId){
        return SuccessResponse.ok(chatBotService.saveChatting(req,userId));
    }


}
