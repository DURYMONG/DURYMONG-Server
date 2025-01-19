package konkuk.kuit.durimong.domain.user.dto.request.signup;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPasswordReq {
    @Schema(description = "비밀번호", example = "durymong123")
    private String password;
}
