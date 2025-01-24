package konkuk.kuit.durimong.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UserInfoReq {
    @Schema(description = "변경할 이름", example = "엘리자베스")
    private String newUserName;

    @Schema(description = "변경할 몽 이름", example = "쿠쿠몽")
    private String newMongName;
}
