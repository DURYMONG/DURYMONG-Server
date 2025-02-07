package konkuk.kuit.durimong.domain.chatbot.service;

import jakarta.transaction.Transactional;
import konkuk.kuit.durimong.domain.chatbot.dto.request.ChatBotChattingReq;
import konkuk.kuit.durimong.domain.chatbot.dto.request.ChatBotPredictReq;
import konkuk.kuit.durimong.domain.chatbot.dto.request.ChatBotRecommendDiaryReq;
import konkuk.kuit.durimong.domain.chatbot.dto.request.ChatBotRecommendTestReq;
import konkuk.kuit.durimong.domain.chatbot.dto.response.*;
import konkuk.kuit.durimong.domain.chatbot.entity.ChatBot;
import konkuk.kuit.durimong.domain.chatbot.repository.ChatBotRepository;
import konkuk.kuit.durimong.domain.column.entity.ColumnCategory;
import konkuk.kuit.durimong.domain.column.repository.CategoryRepository;
import konkuk.kuit.durimong.domain.test.repository.TestRepository;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static konkuk.kuit.durimong.global.exception.ErrorCode.*;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ChatBotService {
    private final ChatBotRepository chatBotRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TestRepository testRepository;
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

        String systemPrompt = createGreetingPrompt(chatBot, userName);

        String userPrompt = String.format("%s에게 인사를 건네고 증상을 물어봐.", userName);

        String responseMessage = getChatGptResponse(systemPrompt, userPrompt);

        return new ChatBotChattingRes(chatBot.getImage(), responseMessage);
    }

    private String createGreetingPrompt(ChatBot chatBot, String userName) {
        String systemPrompt = String.format(
                "당신은 %s라는 이름의 가상 챗봇입니다. 당신의 성격은 %s입니다. "
                        + "사용자에게 처음 말을 걸 때 %s 말투로 질문을 해야 합니다. "
                        + "사용자의 이름은 %s입니다. 사용자에게 증상을 물어보세요.",
                chatBot.getName(), chatBot.getSlogan(), chatBot.getAccent(), userName
        );
        return systemPrompt;
    }


    private String getChatGptResponse(String systemPrompt, String userPrompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "gpt-3.5-turbo");

        JSONArray messages = new JSONArray();
        messages.put(new JSONObject().put("role", "system").put("content", systemPrompt));
        messages.put(new JSONObject().put("role", "user").put("content", userPrompt));

        requestBody.put("messages", messages);
        requestBody.put("temperature", 0.7);

        HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(openAiApiUrl, request, String.class);

        return extractMessageFromResponse(response.getBody());
    }


    private String extractMessageFromResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray choices = jsonResponse.getJSONArray("choices");
            return choices.getJSONObject(0).getJSONObject("message").getString("content");
        } catch (JSONException e) {
            throw new CustomException(CHATBOT_PARSE_ERROR);
        }
    }
    public ChatBotPredictRes analyzeMentalHealth(ChatBotPredictReq req) {
        ChatBot chatBot = chatBotRepository.findById(req.getChatBotId())
                .orElseThrow(() -> new CustomException(CHATBOT_NOT_FOUND));

        if (req.getSymptoms() == null && req.getAdditionalSymptoms() == null) {
            throw new CustomException(CHATBOT_SYMPOMS_EMPTY);
        }

        List<String> selectedSymptoms = req.getSymptoms();
        String additionalSymptoms = req.getAdditionalSymptoms();

        String allSymptoms = String.join(", ", selectedSymptoms);
        if (additionalSymptoms != null && !additionalSymptoms.isEmpty()) {
            allSymptoms += ", " + additionalSymptoms;
        }

        String systemPrompt = createPredictionPrompt(chatBot);
        String gptResponse = getChatGptResponse(systemPrompt, allSymptoms);

        List<String> predictedDisorders = extractPredictedDisorders(gptResponse);
        if (predictedDisorders.isEmpty()) {
            throw new CustomException(CHATBOT_PREDICT_ERROR);
        }

        List<Long> categoryIds = categoryRepository.findByNameIn(predictedDisorders).stream()
                .map(ColumnCategory::getCategoryId)
                .toList();

        String finalMessage = appendRecommendationMessage(gptResponse, chatBot.getAccent());

        return new ChatBotPredictRes(finalMessage, chatBot.getImage(), categoryIds);
    }

    private List<String> extractPredictedDisorders(String gptResponse) {
        List<String> validDisorders = List.of("불면증", "우울증", "공황장애", "조울증", "대인기피증", "폭식증");
        List<String> detectedDisorders = new ArrayList<>();

        for (String disorder : validDisorders) {
            if (gptResponse.contains(disorder)) {
                detectedDisorders.add(disorder);
            }
        }
        return detectedDisorders;
    }

    private String createPredictionPrompt(ChatBot chatBot) {
        String toneInstruction = chatBot.getAccent().equals("반말")
                ? "반말을 무조건 사용해야 해. 존댓말을 절대 사용하지 마. '요'를 절대 사용하면 안 돼. 모든 문장을 반말로 끝내."
                : "무조건 존댓말을 사용해야 합니다. 절대 반말을 사용하지 마세요.";

        return String.format(
                "당신은 %s라는 이름의 챗봇입니다. 당신의 성격은 %s이며, %s 말투를 사용해야 합니다. "
                        + "%s "
                        + "사용자가 입력한 증상을 바탕으로 심적 질환을 예측하세요. "
                        + "반드시 불면증, 우울증, 공황장애, 조울증, 대인기피증, 폭식증 중 하나만 선택해야 합니다. ",
                chatBot.getName(), chatBot.getSlogan(), chatBot.getAccent(), toneInstruction
        );
    }

    private String appendRecommendationMessage(String message, String accent) {
        String recommendation = accent.equals("반말")
                ? " 관련 정보를 확인하거나, 활동을 추천해줄게!"
                : " 관련 정보를 확인하거나, 활동을 추천해드릴게요.";

        return message + recommendation;
    }

    public ChatBotRecommendTestRes recommendTest(ChatBotRecommendTestReq req, Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        ChatBot bot = chatBotRepository.findById(req.getChatBotId()).orElseThrow(() -> new CustomException(CHATBOT_NOT_FOUND));
        List<TestListDto> tests = testRepository.findTestIdAndName();
        return new ChatBotRecommendTestRes(makeRecommendMessage(user,bot),bot.getImage(),tests);
    }

    public String makeRecommendMessage(User user, ChatBot bot){
        if(bot.getAccent().equals("반말")){
            return user.getNickname() +"을 위한 테스트도 준비해봤어!";
        }
        return user.getNickname()+ "님을 위한 테스트를 준비해봤어요.";

    }

    public ChatBotRecommendDiaryRes recommendDiary(ChatBotRecommendDiaryReq req){
        ChatBot bot = chatBotRepository.findById(req.getChatBotId()).orElseThrow(() -> new CustomException(CHATBOT_NOT_FOUND));
        if(bot.getAccent().equals("반말")){
            return new ChatBotRecommendDiaryRes(bot.getImage(),"오늘 하루는 어땠어? 하루 기록을 남겨봐");
        }
        return new ChatBotRecommendDiaryRes(bot.getImage(),"오늘 하루는 어떠셨나요? 하루 기록을 남겨보세요.");
    }




}
