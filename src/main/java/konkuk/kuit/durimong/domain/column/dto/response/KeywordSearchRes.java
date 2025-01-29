package konkuk.kuit.durimong.domain.column.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(description = "키워드 검색 Response DTO")
public class KeywordSearchRes {
    @Schema(description = "검색 결과 칼럼 목록")
    private List<ColumnDTO> columns;
    // DTO 변경하기
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "단일 칼럼 DTO")
    public static class ColumnDTO {
        @Schema(description = "카테고리 id", example = "1")
        private Long categoryId;

        @Schema(description = "카테고리 이름", example = "불면증")
        private String categoryName;

        @Schema(description = "미리보기(~25자)", example = "잠드는 데 어려움을 겪거나, 잠을 유지하는데...")
        private String preview;
    }
}
