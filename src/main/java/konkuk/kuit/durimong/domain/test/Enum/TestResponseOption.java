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
    // 우울증 검사
    DEPRESSION_SCORE_ONE(1, 1, "그렇지 않다"),
    DEPRESSION_SCORE_TWO(1, 2, "가끔 그렇다"),
    DEPRESSION_SCORE_THREE(1, 3, "종종 그렇다"),
    DEPRESSION_SCORE_FOUR(1, 4, "매우 그렇다"),
    // 스트레스 검사
    STRESS_SCORE_ONE(3, 1, "그렇지 않다"),
    STRESS_SCORE_TWO(3, 2, "보통이다"),
    STRESS_SCORE_THREE(3, 3, "매우 그렇다");


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

    public static boolean isEqualResponseOption(int testId, int score) {
        long count = Arrays.stream(values())
                .filter(option -> option.getTestId() == testId)
                .count();

        return count == score;
    }

    public static boolean isValidChoice(int testId, int choice) {
        return Arrays.stream(values())
                .anyMatch(option -> option.getTestId() == testId && option.getScore() == choice);
    }
}
