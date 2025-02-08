package konkuk.kuit.durimong.domain.test.service;

import konkuk.kuit.durimong.domain.test.Enum.TestResponseOption;
import konkuk.kuit.durimong.domain.test.dto.response.DoTestRes;
import konkuk.kuit.durimong.domain.test.dto.response.TestDescriptionRes;
import konkuk.kuit.durimong.domain.test.entity.Test;
import konkuk.kuit.durimong.domain.test.entity.TestQuestion;
import konkuk.kuit.durimong.domain.test.entity.UserTest;
import konkuk.kuit.durimong.domain.test.repository.ScoreDistributionRepository;
import konkuk.kuit.durimong.domain.test.repository.TestQuestionRepository;
import konkuk.kuit.durimong.domain.test.repository.TestRepository;
import konkuk.kuit.durimong.domain.test.repository.UserTestRepository;
import konkuk.kuit.durimong.domain.user.entity.User;
import konkuk.kuit.durimong.domain.user.repository.UserRepository;
import konkuk.kuit.durimong.global.exception.CustomException;
import konkuk.kuit.durimong.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class TestService {
    private final TestRepository testRepository;
    private final UserTestRepository userTestRepository;
    private final TestQuestionRepository testQuestionRepository;
    private final ScoreDistributionRepository scoreDistributionRepository;
    private final UserRepository userRepository;

    // 테스트 상세정보 조회
    public TestDescriptionRes getTestDescription(Long testId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String nickname = user.getNickname();

        Test test = testRepository.findById(testId)
                        .orElseThrow(() -> new CustomException(ErrorCode.TEST_NOT_FOUND));

        // userTest에서 직전 검사 결과 가져오기 (직전 날짜, 직전 점수)
        Optional<UserTest> userTestOptional = userTestRepository.findFirstByUserAndTestOrderByCreatedAtDesc(user, test);

        TestDescriptionRes.LastTestDTO lastTestInfo = userTestOptional
                .map(userTest -> new TestDescriptionRes.LastTestDTO(userTest.getCreatedAt(), nickname, userTest.getScore()))
                .orElse(new TestDescriptionRes.LastTestDTO(null, nickname, null));

        return new TestDescriptionRes(
                testId, test.getName(), test.getEnglishName(),
                test.getContent(), test.getMinNumber(),TestResponseOption.getMinResponse(testId.intValue()),
                test.getMaxNumber(), TestResponseOption.getMaxResponse(testId.intValue()),test.getCriticalScore(),
                test.getCountOfQuestions(), test.getRequiredTime(), lastTestInfo);
    }


    // 테스트 검사 시작
    public DoTestRes getTest(Long testId) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new CustomException(ErrorCode.TEST_NOT_FOUND));

        String testName = test.getName();

        int numberOfQuestions = test.getCountOfQuestions();
        int numberOfOptions = test.getMaxNumber();

        // 테스트의 선택지 개수가 같은 지 확인
        if(! TestResponseOption.isEqualResponseOption(testId.intValue(), numberOfOptions)) {
            throw new CustomException(ErrorCode.TEST_OPTION_COUNT_NOT_EQUALS);
        }

        List<TestQuestion> testQuestionList = testQuestionRepository.findByTest(test);

        // 등록된 문항 개수가 다를 때
        if(testQuestionList.size() != numberOfQuestions) {
            throw new CustomException(ErrorCode.TEST_QUESTION_COUNT_NOT_EQUALS);
        }

        List<DoTestRes.QuestionListDTO> questionList = testQuestionList.stream()
                .map(testQuestion -> new DoTestRes.QuestionListDTO(testQuestion.getNumber(),testQuestion.getQuestion()))
                .toList();

        return new DoTestRes(testId, testName, numberOfQuestions, numberOfOptions, questionList);
    }

}
