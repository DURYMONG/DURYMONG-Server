package konkuk.kuit.durimong.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UserEditPasswordReq {
    @Schema(description = "현재 비밀번호", example = "durymong123")
    private String nowPassword;

    @Schema(description = "새로운 비밀번호", example = "durymong1234")
    private String newPassword;
}
