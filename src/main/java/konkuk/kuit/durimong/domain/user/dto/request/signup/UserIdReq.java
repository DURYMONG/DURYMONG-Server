package konkuk.kuit.durimong.domain.user.dto.request.signup;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserIdReq {
    @Schema(description = "아이디",example = "durymong")
    private String id;
}
