package konkuk.kuit.durimong.domain.column.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "카테고리 상세설명 Response DTO")
public class CategoryDetailRes {
    @Schema(description = "카테고리 상세설명", example = "수면장애란...")
    private String detail;

}
