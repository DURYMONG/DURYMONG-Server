package konkuk.kuit.durimong.domain.column.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(description = "카테고리 목록 Response DTO")
public class CategoryRes {

    @Schema(description = "카테고리 목록")
    private List<CategoryDTO> categories;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "단일 카테고리 DTO")
    public static class CategoryDTO {
        @Schema(description = "카테고리 id", example = "1")
        private Long categoryId;

        @Schema(description = "카테고리 이름", example = "수면장애")
        private String name;

        @Schema(description = "카테고리 이미지", example = "https://durimong.com/category/image.jpg")
        private String image;
    }

}
