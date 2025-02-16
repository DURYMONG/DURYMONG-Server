package konkuk.kuit.durimong.domain.mong.entity;

import konkuk.kuit.durimong.global.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

import static konkuk.kuit.durimong.global.exception.ErrorCode.MONG_TYPE_NOT_EXISTS;

@Getter
@AllArgsConstructor
public enum MongType {

    TREE("tree", "/images/mong/tree/"),
    SEEDLING("seedling", "/images/mong/seedling/"),
    STAR("star", "/images/mong/star/"),
    CLOUD("cloud", "/images/mong/cloud/");

    private final String type;
    private final String basePath;

    public String getImagePath(MongColor color, int level) {
        return basePath + color.getCode() + "_" + level + ".png";
        // ex) /images/mong/tree/purple_1.png
    }

    public static MongType from(String type) {
        return Arrays.stream(values())
                .filter(mongType -> mongType.getType().equalsIgnoreCase(type))
                .findFirst()
                .orElseThrow(() -> new CustomException(MONG_TYPE_NOT_EXISTS));
    }
}
