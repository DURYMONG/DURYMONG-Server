package konkuk.kuit.durimong.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserChatHistoryRes {
    @Schema(description = "몽 이미지" , example = "image")
    private String mongImage;

    @Schema(description = "UserChatHistoryDto")
    private List<UserChatHistoryDto> userChatHistory;


}
