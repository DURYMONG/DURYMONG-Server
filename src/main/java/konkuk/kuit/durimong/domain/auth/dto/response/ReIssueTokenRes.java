package konkuk.kuit.durimong.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReIssueTokenRes {
    @Schema(description = "새 Access Token")
    private String newAccessToken;

    @Schema(description = "새 Refresh Token")
    private String newRefreshToken;
}
