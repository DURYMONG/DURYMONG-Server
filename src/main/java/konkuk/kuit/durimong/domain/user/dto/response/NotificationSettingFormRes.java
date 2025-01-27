package konkuk.kuit.durimong.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationSettingFormRes {
    @Schema(description = "몽 이름", example = "두리몽")
    private String mongName;

    @Schema(description = "유저 닉네임", example = "엘리자베스")
    private String userNickname;
}
