package konkuk.kuit.durimong.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserUnRegisterRes {
    @Schema(description = "유저 닉네임", example = "엘리자베스")
    private String id;

    @Schema(description = "몽과 함께한 시간", example = "123")
    private int withMongDate;

    @Schema(description = "몽 이미지", example = "image.png")
    private String mongImage;

}
