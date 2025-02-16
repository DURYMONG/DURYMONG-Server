package konkuk.kuit.durimong.domain.mong.entity;

import konkuk.kuit.durimong.global.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

import static konkuk.kuit.durimong.global.exception.ErrorCode.MONG_COLOR_NOT_EXISTS;

@Getter
@AllArgsConstructor
public enum MongColor {
    PURPLE("purple"),
    YELLOW("yellow"),
    BLUE("blue");

    private final String code;

    public static MongColor from(String color) {
        return Arrays.stream(values())
                .filter(mongColor -> mongColor.getCode().equalsIgnoreCase(color))
                .findFirst()
                .orElseThrow(() -> new CustomException(MONG_COLOR_NOT_EXISTS));
    }
}
