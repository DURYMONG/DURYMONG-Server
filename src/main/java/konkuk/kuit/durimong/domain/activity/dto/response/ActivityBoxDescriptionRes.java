package konkuk.kuit.durimong.domain.activity.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(description = "박스 형식 활동 설명화면 Response DTO")
public class ActivityBoxDescriptionRes {
    @Schema(description = "활동이름", example = "침구정리하기")
    private String activityName;

    @Schema(description = "활동 박스 목록")
    private List<ActivityBoxDTO> activityList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "단일 활동 박스 DTO")
    public static class ActivityBoxDTO {
        @Schema(description = "활동 박스 이름", example = "1")
        private String boxName;

        @Schema(description = "내용", example = "수면장애")
        private String content;

        @Schema(description = "효과", example = "수면장애")
        private String effect;

        @Schema(description = "카테고리 이미지", example = "https://durimong.com/category/image.jpg")
        private String image;
    }
}
