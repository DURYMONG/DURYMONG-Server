package konkuk.kuit.durimong.domain.chatbot.controller;

import io.swagger.v3.oas.annotations.Operation;
import konkuk.kuit.durimong.domain.chatbot.dto.response.ChatBotRes;
import konkuk.kuit.durimong.domain.chatbot.service.ChatBotService;
import konkuk.kuit.durimong.global.annotation.CustomExceptionDescription;
import konkuk.kuit.durimong.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static konkuk.kuit.durimong.global.config.swagger.SwaggerResponseDescription.CHAT_BOT;

@RequiredArgsConstructor
@RestController
@RequestMapping("chatbots")
public class ChatBotController {
    private final ChatBotService chatBotService;

    @Operation(summary = "채팅봇 조회", description = "유저가 채팅봇을 선택하는 화면입니다.")
    @CustomExceptionDescription(CHAT_BOT)
    @GetMapping()
    public SuccessResponse<List<ChatBotRes>> getChatBots() {
        return SuccessResponse.ok(chatBotService.getChatBots());
    }

}
