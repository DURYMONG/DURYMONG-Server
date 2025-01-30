package konkuk.kuit.durimong.domain.chatbot.service;

import jakarta.transaction.Transactional;
import konkuk.kuit.durimong.domain.chatbot.dto.response.ChatBotRes;
import konkuk.kuit.durimong.domain.chatbot.entity.ChatBot;
import konkuk.kuit.durimong.domain.chatbot.repository.ChatBotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ChatBotService {
    private final ChatBotRepository chatBotRepository;

    private final RestTemplate restTemplate;

    public List<ChatBotRes> getChatBots() {
        List<ChatBot> chatBots = chatBotRepository.findAll();
        List<ChatBotRes> chatBotResList = new ArrayList<>();
        for (ChatBot chatBot : chatBots) {
            ChatBotRes chatBotRes = new ChatBotRes(chatBot.getName(),
                    chatBot.getMbti(),
                    chatBot.getAccent(),
                    chatBot.getNickname(),
                    chatBot.getSlogan(),
                    chatBot.getImage());
            chatBotResList.add(chatBotRes);
        }
        return chatBotResList;
    }

}
