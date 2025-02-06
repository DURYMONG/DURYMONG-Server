package konkuk.kuit.durimong.domain.test.Enum;

import konkuk.kuit.durimong.global.exception.CustomException;
import konkuk.kuit.durimong.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

@Getter
@AllArgsConstructor
public enum TestResponseOption {
    // 스트레스 검사
    STRESS_SCORE_ONE(3, 1, "그렇지 않다"),
    STRESS_SCORE_TWO(3, 2, "보통이다"),
    STRESS_SCORE_THREE(3, 3, "매우 그렇다");
    // 우울증 검사

    private final int testId;
    private final int score;
    private final String response;

    public static String getMinResponse(int testId) {
        return Arrays.stream(values())
                .filter(option -> option.getTestId() == testId)
                .min(Comparator.comparingInt(TestResponseOption::getScore))
                .map(TestResponseOption::getResponse)
                .orElseThrow(() -> new CustomException(ErrorCode.TEST_MIN_RESPONSE_NOT_EXISTS));
    }

    public static String getMaxResponse(int testId) {
        return Arrays.stream(values())
                .filter(option -> option.getTestId() == testId)
                .max(Comparator.comparingInt(TestResponseOption::getScore))
                .map(TestResponseOption::getResponse)
                .orElseThrow(() -> new CustomException(ErrorCode.TEST_MAX_RESPONSE_NOT_EXISTS));
    }
}
