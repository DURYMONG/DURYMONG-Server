package konkuk.kuit.durimong.domain.test.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(description = "테스트 완료 RequestDTO")
public class SubmitTestReq {
    @Schema(description = "유저의 응답 리스트")
    private List<UserResponseDTO> responseList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "유저의 응답 리스트 DTO")
    public static class UserResponseDTO {
        @Schema(description = "문항 번호", example = "1")
        private Integer number;

        @Schema(description = "유저의 선택", example = "1")
        private Integer choice;
    }
}
