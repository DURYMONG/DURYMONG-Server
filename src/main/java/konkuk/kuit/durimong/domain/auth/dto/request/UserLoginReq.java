package konkuk.kuit.durimong.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UserLoginReq {
    @Schema(description = "아이디", example ="durymong")
    private String id;

    @Schema(description = "비밀번호", example = "durymong12")
    private String password;
}
