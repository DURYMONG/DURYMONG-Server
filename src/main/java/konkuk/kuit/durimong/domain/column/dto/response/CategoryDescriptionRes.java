package konkuk.kuit.durimong.domain.column.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "카테고리 상세설명 Response DTO")
public class CategoryDescriptionRes {
    @Schema(description = "카테고리 id", example = "1")
    private Long categoryId;

    @Schema(description = "카테고리 상세설명", example = "수면장애란...")
    private String description;
}
