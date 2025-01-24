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
@Schema(description = "칼럼 조회 Response DTO")
public class ColumnRes {
    @Schema(description = "칼럼 id", example = "1")
    private Long id;

    @Schema(description = "칼럼 제목", example = "수면장애에 대한 이야기")
    private String title;

    @Schema(description = "칼럼 소제목", example = "잠 못 드는 하루하루, 수면장애")
    private String subtitle;

    @Schema(description = "칼럼 내용", example = "수면장애에 대한 이야기")
    private String content;

    @Schema(description = "칼럼 이미지", example = "https://durimong.com/column/1/image.jpg")
    private String contentImage;

}
