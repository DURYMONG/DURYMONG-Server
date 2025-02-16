package konkuk.kuit.durimong.domain.mong.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class MongCreateReq {
    @Schema(description = "몽 이름", example = "두리몽")
    private String mongName;

    @Schema(description = "몽 타입", example = "tree")
    private String mongType;

    @Schema(description = "캐릭터 색상", example = "yellow")
    private String mongColor;

}
