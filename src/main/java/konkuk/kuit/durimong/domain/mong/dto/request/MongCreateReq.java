package konkuk.kuit.durimong.domain.mong.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MongCreateReq {
    @Schema(description = "몽 이름", example = "두리몽")
    private String mongName;
}
