package konkuk.kuit.durimong.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ReIssueTokenReq {
    @Schema(description = "Refresh Token")
    private String refreshToken;
}
