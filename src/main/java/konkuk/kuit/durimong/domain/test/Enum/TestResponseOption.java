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
    DEPRESSION_SCORE_ONE(1, 0, "그렇지 않다"),
    DEPRESSION_SCORE_TWO(1, 1, "가끔 그렇다"),
    DEPRESSION_SCORE_THREE(1, 2, "종종 그렇다"),
    DEPRESSION_SCORE_FOUR(1, 3, "항상 그렇다"),
    // 외상 후 스트레스 검사
    PTSD_SCORE_ONE(2, 0, "전혀 아니다"),
    PTSD_SCORE_TWO(2, 1, "거의 아니다"),
    PTSD_SCORE_THREE(2, 2, "보통이다"),
    PTSD_SCORE_FOUR(2, 3, "가끔 그렇다"),
    PTSD_SCORE_FIVE(2, 4, "매우 그렇다"),
    // 스트레스 수치 검사
    STRESS_SCORE_ZERO(3, 0, "그렇지 않다"),
    STRESS_SCORE_ONE(3, 1, "가끔 그렇다"),
    STRESS_SCORE_TWO(3, 2, "종종 그렇다"),
    STRESS_SCORE_THREE(3, 3, "항상 그렇다"),
    //조울증 검사
    MDQ_SCORE_ONE(4, 0, "없다"),
    MDQ_SCORE_TWO(4, 1, "가끔 있다"),
    MDQ_SCORE_THREE(4, 2, "자주 있었다"),
    //범 불안 장애 검사
    GAD_SCORE_ONE(5,0,"그렇지 않다"),
    GAD_SCORE_TWO(5,1,"종종 그렇다"),
    GAD_SCORE_THREE(5,2,"항상 그렇다");

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

    public static boolean isEqualResponseOption(int testId, int numberOfOptions) {
        long count = Arrays.stream(values())
                .filter(option -> option.getTestId() == testId)
                .count();

        return count == numberOfOptions;
    }

    public static boolean isValidChoice(int testId, int choice) {
        return Arrays.stream(values())
                .anyMatch(option -> option.getTestId() == testId && option.getScore() == choice);
    }
}
