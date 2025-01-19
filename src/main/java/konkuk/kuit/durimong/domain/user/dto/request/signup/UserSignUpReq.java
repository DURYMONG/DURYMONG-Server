package konkuk.kuit.durimong.domain.user.dto.request.signup;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class UserSignUpReq {
    @Schema(description = "아이디",example = "durymong")
    private String id;

    @Email
    @Schema(description = "이메일",example = "dlwjddus1112@naver.com")
    private String email;

    @Schema(description = "비밀번호", example = "durymong123")
    private String password;

    @Schema(description = "이름",example = "이정연")
    private String name;

    @Schema(description = "닉네임",example = "이정연")
    private String nickname;
}
