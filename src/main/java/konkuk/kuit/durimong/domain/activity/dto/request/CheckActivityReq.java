package konkuk.kuit.durimong.domain.activity.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CheckActivityReq {
    @Schema(description = "활동 id", example = "1")
    private Long activityId;
}
