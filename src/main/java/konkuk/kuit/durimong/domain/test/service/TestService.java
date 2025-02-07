package konkuk.kuit.durimong.domain.test.service;

import konkuk.kuit.durimong.domain.test.Enum.TestResponseOption;
import konkuk.kuit.durimong.domain.test.dto.response.TestDescriptionRes;
import konkuk.kuit.durimong.domain.test.entity.Test;
import konkuk.kuit.durimong.domain.test.entity.UserTest;
import konkuk.kuit.durimong.domain.test.repository.ScoreDistributionRepository;
import konkuk.kuit.durimong.domain.test.repository.TestContentRepository;
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

import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class TestService {
    private final TestRepository testRepository;
    private final UserTestRepository userTestRepository;
    private final TestContentRepository testContentRepository;
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

        // test엔터티의 minNumber와 maxNumber가 각각 같은지 확인

        return new TestDescriptionRes(
                testId, test.getName(), test.getEnglishName(),
                test.getContent(), test.getMinNumber(),TestResponseOption.getMinResponse(testId.intValue()),
                test.getMaxNumber(), TestResponseOption.getMaxResponse(testId.intValue()),test.getCriticalScore(),
                test.getCountOfQuestions(), test.getRequiredTime(), lastTestInfo);
    }

}
