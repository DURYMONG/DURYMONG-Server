package konkuk.kuit.durimong.domain.user.dto.request.signup;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailVerifyReq {
    @Email
    @Schema(description = "이메일", example = "dlwjddus1112@naver.com")
    private String email;
    @Schema(description = "인증번호", example = "000000")
    private String authCode;
}
