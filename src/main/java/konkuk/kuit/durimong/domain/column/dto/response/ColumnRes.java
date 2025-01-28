package konkuk.kuit.durimong.domain.column.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Schema(description = "개별 칼럼 조회 Response DTO")
public class ColumnRes {
    @Schema(description = "카테고리 이름", example = "불면증")
    private String categoryName;

    @Schema(description = "칼럼 제목", example = "수면장애에 대한 이야기")
    private String title;

    @Schema(description = "칼럼 소제목", example = "잠 못 드는 하루하루, 수면장애")
    private String subtitle;

    @Schema(description = "칼럼 내용 (최대 3000자 제한)", example = "잠드는데 어려움을 겪거나...")
    private String content;

    @Schema(description = "칼럼 이미지", example = "https://durimong.com/column/1/image.jpg")
    private String image;

}
