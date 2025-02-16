package konkuk.kuit.durimong.domain.mong.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MongGrowthRes {
    @Schema(description = "몽 이미지", example = "image")
    private String mongImage;

    @Schema(description = "몽 메시지", example = "안녕하세요! 잘 부탁해요!")
    private String mongMessage;
}
