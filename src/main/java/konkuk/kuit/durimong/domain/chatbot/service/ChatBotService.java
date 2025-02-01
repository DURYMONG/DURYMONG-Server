package konkuk.kuit.durimong.domain.chatbot.service;

import jakarta.transaction.Transactional;
import konkuk.kuit.durimong.domain.chatbot.dto.request.ChatBotChattingReq;
import konkuk.kuit.durimong.domain.chatbot.dto.response.ChatBotChattingRes;
import konkuk.kuit.durimong.domain.chatbot.dto.response.ChatBotRes;
import konkuk.kuit.durimong.domain.chatbot.entity.ChatBot;
import konkuk.kuit.durimong.domain.chatbot.repository.ChatBotRepository;
import konkuk.kuit.durimong.domain.user.entity.User;
import konkuk.kuit.durimong.domain.user.repository.UserRepository;
import konkuk.kuit.durimong.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static konkuk.kuit.durimong.global.exception.ErrorCode.CHATBOT_NOT_FOUND;
import static konkuk.kuit.durimong.global.exception.ErrorCode.USER_NOT_FOUND;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ChatBotService {
    private final ChatBotRepository chatBotRepository;
    private final UserRepository userRepository;

    @Value("${spring.openai.api.url}")
    private String openAiApiUrl;

    @Value("${spring.openai.api.key}")
    private String openAiApiKey;

    private final RestTemplate restTemplate;

    public List<ChatBotRes> getChatBots() {
        List<ChatBot> chatBots = chatBotRepository.findAll();
        List<ChatBotRes> chatBotResList = new ArrayList<>();
        for (ChatBot chatBot : chatBots) {
            ChatBotRes chatBotRes = new ChatBotRes(
                    chatBot.getChatBotId(),
                    chatBot.getName(),
                    chatBot.getMbti(),
                    chatBot.getAccent(),
                    chatBot.getNickname(),
                    chatBot.getSlogan(),
                    chatBot.getImage());
            chatBotResList.add(chatBotRes);
        }
        return chatBotResList;
    }
    
    public ChatBotChattingRes getChatBotGreeting(ChatBotChattingReq req, Long userId) {
        ChatBot chatBot = chatBotRepository.findById(req.getChatBotId())
                .orElseThrow(() -> new CustomException(CHATBOT_NOT_FOUND));
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        String userName = user.getName();

        String systemPrompt = String.format(
                "당신은 %s라는 이름의 가상 챗봇입니다. 당신의 성격은 %s입니다. "
                        + "사용자에게 처음 말을 걸 때 %s 말투로 질문을 해야 합니다. "
                        + "사용자의 이름은 %s입니다. 사용자에게 증상을 물어보세요.",
                chatBot.getName(), chatBot.getSlogan(), chatBot.getAccent(), userName
        );

        String userPrompt = String.format("%s에게 인사를 건네고 증상을 물어봐.", userName);

        String responseMessage = getChatGptResponse(systemPrompt, userPrompt);

        return new ChatBotChattingRes(chatBot.getImage(), responseMessage);
    }


    private String getChatGptResponse(String systemPrompt, String userPrompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);

        String requestBody = String.format(
                "{ \"model\": \"gpt-3.5-turbo\", \"messages\": ["
                        + "{ \"role\": \"system\", \"content\": \"%s\" },"
                        + "{ \"role\": \"user\", \"content\": \"%s\" }],"
                        + "\"temperature\": 0.7 }",
                systemPrompt, userPrompt
        );

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        String response = restTemplate.postForObject(openAiApiUrl, request, String.class);

        return extractMessageFromResponse(response);
    }

    private String extractMessageFromResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray choices = jsonResponse.getJSONArray("choices");
            return choices.getJSONObject(0).getJSONObject("message").getString("content");
        } catch (JSONException e) {
            log.error("❌ ChatGPT 응답 파싱 오류", e);
            return "기본 메시지를 생성하는 중 오류가 발생했습니다.";
        }
    }


}
