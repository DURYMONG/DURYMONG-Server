package konkuk.kuit.durimong.domain.user.dto.request.signup;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEmailReq {
    @Schema(description = "이메일", example = "example@naver.com")
    private String email;
}
