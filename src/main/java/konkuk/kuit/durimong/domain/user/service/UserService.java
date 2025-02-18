package konkuk.kuit.durimong.domain.user.service;

import jakarta.transaction.Transactional;
import konkuk.kuit.durimong.domain.auth.service.RedisService;
import konkuk.kuit.durimong.domain.chatbot.entity.ChatBot;
import konkuk.kuit.durimong.domain.chatbot.entity.ChatHistory;
import konkuk.kuit.durimong.domain.chatbot.repository.ChatBotRepository;
import konkuk.kuit.durimong.domain.chatbot.repository.ChatHistoryRepository;
import konkuk.kuit.durimong.domain.mong.entity.Mong;
import konkuk.kuit.durimong.domain.mong.entity.MongQuestion;
import konkuk.kuit.durimong.domain.mong.repository.MongQuestionRepository;
import konkuk.kuit.durimong.domain.mong.repository.MongRepository;
import konkuk.kuit.durimong.domain.user.dto.request.*;
import konkuk.kuit.durimong.domain.user.dto.request.signup.UserSignUpReq;
import konkuk.kuit.durimong.domain.user.dto.response.*;
import konkuk.kuit.durimong.domain.user.entity.User;
import konkuk.kuit.durimong.domain.user.entity.UserMongConversation;
import konkuk.kuit.durimong.domain.user.repository.UserMongConversationRepository;
import konkuk.kuit.durimong.domain.user.repository.UserRepository;
import konkuk.kuit.durimong.global.annotation.UserId;
import konkuk.kuit.durimong.global.exception.CustomException;
import konkuk.kuit.durimong.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static konkuk.kuit.durimong.domain.user.dto.response.UserDailyBotChatChoiceRes.ChatBotChoiceDto;
import static konkuk.kuit.durimong.domain.user.dto.response.UserDailyBotChatRes.ChatHistoryDto;
import static konkuk.kuit.durimong.global.exception.ErrorCode.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private static final String AUTH_CODE_PREFIX = "AuthCode";
    private final MailService mailService;
    private final RedisService redisService;
    private final UserRepository userRepository;
    private final MongRepository mongRepository;
    private final MongQuestionRepository mongAnswerRepository;
    private final UserMongConversationRepository userMongConversationRepository;
    private final JwtProvider jwtProvider;
    private final MongQuestionRepository mongQuestionRepository;
    private final ChatHistoryRepository chatHistoryRepository;
    private final ChatBotRepository chatBotRepository;
    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;

    public String validateId(String id) {
        if(userRepository.existsById(id)) {
            throw new CustomException(USER_DUPLICATE_ID);
        }
        return "사용 가능한 아이디입니다.";
    }
    public String validateEmail(String email) {
        if (!isValidEmail(email)) {
            throw new CustomException(INVALID_EMAIL_FORMAT);
        }

        if(userRepository.existsByEmail(email)) {
            throw new CustomException(USER_DUPLICATE_EMAIL);
        }
        return "사용 가능한 이메일입니다.";
    }
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        return email.matches(emailRegex);
    }
    public String validatePassword(String password) {
        if (password.length() < 6 || password.length() > 10) {
            throw new CustomException(USER_PASSWORD_SHORT);
        }

        if (!password.matches(".*[0-9].*")) {
            throw new CustomException(USER_PASSWORD_NONUM);
        }

        if (!password.matches(".*[a-zA-Z].*")) {
            throw new CustomException(USER_PASSWORD_ENGLISH);
        }

        return "사용 가능한 비밀번호입니다.";
    }
    private String createCode() {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new CustomException(NO_SUCH_ALGORITHM);
        }
    }
    public String sendCodeToEmail(String toEmail){
        String title = "Durymong 이메일 인증 번호";
        String authcode = this.createCode();
        mailService.sendEmail(toEmail, title, authcode);
        redisService.setValues(AUTH_CODE_PREFIX + toEmail,
                authcode,
                Duration.ofMillis(authCodeExpirationMillis));
        return "인증번호가 전송되었습니다.";
    }

    public String verifyCode(String email, String authCode){
        String redisAuthCode = redisService.getValues(AUTH_CODE_PREFIX + email);
        boolean authResult = redisService.checkExistsValue(redisAuthCode) && redisAuthCode.equals(authCode);
        if(!authResult) {
            throw new CustomException(USER_EMAIL_VERIFY_FAILED);
        }
        return "인증이 완료되었습니다.";
    }

    public String register(UserSignUpReq req) {
        if(!userRepository.existsById(req.getId())){
            throw new CustomException(USER_DUPLICATE_ID);
        }
        User user = User.create(req.getId(), req.getPassword(), req.getEmail(),req.getName(),req.getEmail());
        userRepository.save(user);
        return "회원가입이 완료되었습니다.";
    }

    public UserHomeRes homePage(@UserId Long userId) {
        LocalDate todayDate = LocalDate.now();
        LocalDateTime today = LocalDateTime.now();

        User user = userRepository.findByUserId(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        Mong findMong = mongRepository.findByUser(user).orElseThrow(() -> new CustomException(MONG_NOT_FOUND));
        String mongImage = findMong.getMongImage().getImageUrl();
        String mongName = findMong.getName();
        LocalDateTime createdAt = findMong.getCreatedAt();
        int dateWithMong = getDateWithMong(today, createdAt);

        String mongQuestion = getDailyQuestion();
        MongQuestion question = mongQuestionRepository.findMongQuestionByDate(todayDate.getDayOfMonth())
                .orElseThrow(() -> new CustomException(QUESTION_NOT_EXISTS));

        UserMongConversation conversation = userMongConversationRepository.findByUserAndQuestion(user, question);
        if (conversation != null) {
            return new UserHomeRes(todayDate, dateWithMong, mongName, mongImage, mongQuestion, conversation.getUserAnswer());
        } else {
            return new UserHomeRes(todayDate, dateWithMong, mongName, mongImage, mongQuestion);
        }
    }


    private int getDateWithMong(LocalDateTime today, LocalDateTime createdAt){
        return (int) Duration.between(createdAt, today).toDays() + 1;
    }

    private String getDailyQuestion(){
        List<MongQuestion> mongAnswers = mongAnswerRepository.findAll();
        if(mongAnswers.isEmpty()){
            throw new CustomException(QUESTION_NOT_EXISTS);
        }
        LocalDate today = LocalDate.now();
        int date = today.getDayOfMonth();
        return mongAnswerRepository.findQuestionByDate(date).orElseThrow(
                () -> new CustomException(QUESTION_NOT_EXISTS)
        );

    }


    public String editUserInfo(UserInfoReq req, Long userId){
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        if(req.getNewUserID().equals(user.getId())){
            throw new CustomException(USER_SAME_NAME);
        }
        user.setId(req.getNewUserID());
        userRepository.save(user);
        Mong mong = mongRepository.findByUser(user).orElseThrow(() -> new CustomException(MONG_NOT_FOUND));
        if(req.getNewMongName().equals(mong.getName())){
            throw new CustomException(MONG_SAME_NAME);
        }
        mong.setName(req.getNewMongName());
        mongRepository.save(mong);
        return "회원 정보 수정이 완료되었습니다.";
    }

    public String editUserPassword(UserEditPasswordReq req, Long userId){
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        if(!req.getNowPassword().equals(user.getPassword())){
            throw new CustomException(USER_NOT_MATCH_PASSWORD);
        }
        if(req.getNewPassword().equals(req.getNowPassword())){
            throw new CustomException(USER_SAME_PASSWORD);
        }

        if (req.getNewPassword().length() < 6 || req.getNewPassword().length() > 10) {
            throw new CustomException(USER_PASSWORD_SHORT);
        }

        if (!req.getNewPassword().matches(".*[0-9].*")) {
            throw new CustomException(USER_PASSWORD_NONUM);
        }

        if (!req.getNewPassword().matches(".*[a-zA-Z].*")) {
            throw new CustomException(USER_PASSWORD_ENGLISH);
        }
        user.setPassword(req.getNewPassword());
        userRepository.save(user);
        return "비밀번호 수정이 완료되었습니다.";
    }

    public UserUnRegisterRes showUnRegister(Long userId){
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        Mong mong = mongRepository.findByUser(user).orElseThrow(() -> new CustomException(MONG_NOT_FOUND));
        LocalDateTime createdAt = mong.getCreatedAt();
        LocalDateTime today = LocalDateTime.now();
        int dateWithMong = getDateWithMong(today,createdAt);
        return new UserUnRegisterRes(mong.getName(),user.getId(),dateWithMong,mong.getMongImage().getImageUrl());
    }

    public String unRegister(String accessToken,Long userId){
        invalidateTokens(accessToken,userId);
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        userRepository.delete(user);
        userRepository.flush();
        return "회원 탈퇴가 완료되었습니다," ;
    }

    public String userAnswer(UserMongConversationReq req, Long userId){
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        int date = LocalDate.now().getDayOfMonth();
        MongQuestion question = mongQuestionRepository.findMongQuestionByDate(date).orElseThrow(() -> new CustomException(QUESTION_NOT_EXISTS));
        UserMongConversation conv = UserMongConversation.create(req.getUserAnswer(),user,question,question.getQuestion());
        userMongConversationRepository.save(conv);
        return "답변 생성이 완료되었습니다.";
    }

    public String editAnswer(UserEditAnswerReq req, Long userId){
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        UserMongConversation conversation = userMongConversationRepository.findByCreatedAtAndUser(LocalDate.now(), user).orElseThrow(
                () -> new CustomException(CONVERSATION_NOT_EXISTS));
        conversation.setUserAnswer(req.getNewAnswer());
        userMongConversationRepository.save(conversation);
        return "답변 수정이 완료되었습니다.";
    }

    private void invalidateTokens(String accessToken, Long userId) {
        if (accessToken != null) {
            long accessTokenExpirationMillis = jwtProvider.getClaims(accessToken).getExpiration().getTime() - System.currentTimeMillis();
            if (accessTokenExpirationMillis > 0) {
                redisService.setValues("BLACKLIST:" + accessToken, "logout", Duration.ofMillis(accessTokenExpirationMillis));
            }
        }

        if (jwtProvider.checkTokenExists(String.valueOf(userId))) {
            jwtProvider.invalidateToken(userId);
        }
    }

    public List<UserChatHistoryRes> showChatHistory(Long userId){
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        return userMongConversationRepository.findAllByUser(user);
    }

    public String deleteChat(UserDeleteChatReq req){
        UserMongConversation conversation = userMongConversationRepository.findByUserMongConversationId(req.getConversationId()).orElseThrow(
                () -> new CustomException(CONVERSATION_NOT_EXISTS)
        );
        userMongConversationRepository.delete(conversation);
        userMongConversationRepository.flush();
        return "선택하신 대화가 삭제되었습니다.";
    }

    public NotificationSettingFormRes notificationSettingForm(Long userId){
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        Mong mong = mongRepository.findByUser(user).orElseThrow(() -> new CustomException(MONG_NOT_FOUND));
        return new NotificationSettingFormRes(mong.getName(),user.getId());
    }

    public UserDailyChatRes userDailyChat(Long userId, UserDailyChatReq req){
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        if(req.getTargetDate().isAfter(LocalDate.now())){
            throw new CustomException(DATE_IS_FUTURE);
        }
        UserMongConversation chat = userMongConversationRepository.findByCreatedAtAndUser(req.getTargetDate(), user).orElseThrow(
                () -> new CustomException(CONVERSATION_NOT_EXISTS)
        );
        return new UserDailyChatRes(req.getTargetDate(),chat.getMongQuestion(),chat.getUserAnswer());
    }

    public String deleteHistory(Long userId){
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        if(userMongConversationRepository.findAllByUser(user).isEmpty() && chatHistoryRepository.findAllByUser(user).isEmpty()){
            throw new CustomException(RECORD_IS_EMPTY);
        }
        userMongConversationRepository.deleteAllByUser(user);
        userMongConversationRepository.flush();
        chatHistoryRepository.deleteAllByUser(user);
        chatHistoryRepository.flush();
        return "기록 지우기가 완료되었습니다.";
    }

    public UserDailyBotChatChoiceRes showBotChatHistory(UserDailyBotChatChoiceReq req){
        LocalDate targetDate = req.getTargetDate();
        if(targetDate.isAfter(LocalDate.now())){
            throw new CustomException(DATE_IS_FUTURE);
        }
        int day = targetDate.getDayOfMonth();
        int month = targetDate.getMonthValue();
        String targetDateStr = month + "월 " + day + "일의 기록";
        List<ChatBot> chatBots = chatHistoryRepository.findChatBotsByDate(targetDate);
        if(chatBots.isEmpty()){
            throw new CustomException(BOT_CHAT_NOT_EXISTS);
        }
        List<ChatBotChoiceDto> chatBotChoiceDtos = new ArrayList<>();
        for (ChatBot chatBot : chatBots) {
            chatBotChoiceDtos.add(new ChatBotChoiceDto(chatBot.getChatBotId(),
                    chatBot.getImage(),
                    chatBot.getName()+"와의 대화보기"));
        }
        return new UserDailyBotChatChoiceRes(targetDateStr, chatBotChoiceDtos);

    }

    public UserDailyBotChatRes userDailyBotChat( UserDailyBotChatReq req, Long userId){
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        ChatBot chatBot = chatBotRepository.findById(req.getChatBotId()).orElseThrow(
                () -> new CustomException(CHATBOT_NOT_FOUND));
        LocalDate targetDate = req.getTargetDate();
        if(targetDate.isAfter(LocalDate.now())){
            throw new CustomException(DATE_IS_FUTURE);
        }
        List<ChatHistory> chatHistories = chatHistoryRepository.findChatHistoryByUserAndChatBotAndDate(user, chatBot, targetDate);
        List<ChatHistoryDto> chatHistoryDtos = new ArrayList<>();
        for (ChatHistory chatHistory : chatHistories) {
            ChatHistoryDto chatHistoryDto = getChatHistoryDto(chatHistory);
            chatHistoryDtos.add(chatHistoryDto);
        }
        String targetDateStr = targetDate.getMonthValue() + "월 " + targetDate.getDayOfMonth() + "일의 상담";
        return new UserDailyBotChatRes(targetDateStr, chatBot.getImage(), chatHistoryDtos);


    }

    private ChatHistoryDto getChatHistoryDto(ChatHistory chatHistory) {
        LocalDateTime createdAt = chatHistory.getCreatedAt();
        String startTime;
        if(createdAt.getMinute() < 10){
            startTime = createdAt.getHour() + ":0" + createdAt.getMinute();
        }
        else{
            startTime = createdAt.getHour() + ":" + createdAt.getMinute();
        }

        return new ChatHistoryDto(
                startTime,
                chatHistory.getSymptomsHistory() != null ? chatHistory.getSymptomsHistory().getBotGreeting() : null,
                chatHistory.getSymptomsHistory() != null ? chatHistory.getSymptomsHistory().getSymptoms() : null,
                chatHistory.getSymptomsHistory() != null ? chatHistory.getSymptomsHistory().getAdditionalSymptom() : null,
                chatHistory.getPredictionHistory() != null ? chatHistory.getPredictionHistory().getBotMessage() : null,
                chatHistory.getTestHistory() != null ? chatHistory.getTestHistory().getBotMessage() : null,
                chatHistory.getTestHistory() != null ? chatHistory.getTestHistory().getRecommendedTests() : null,
                chatHistory.getDiaryHistory() != null ? chatHistory.getDiaryHistory().getBotMessage() : null,
                chatHistory.getBotMessage()
        );
    }

    public void setPushEnabled(Long userId){
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        if(user.isPushEnabled()){
            user.setPushEnabled(false);
            userRepository.save(user);
            return;
        }
        user.setPushEnabled(true);
        userRepository.save(user);

    }


}
