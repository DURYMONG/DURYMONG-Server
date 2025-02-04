package konkuk.kuit.durimong.domain.activity.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class WriteDiaryReq {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Schema(description = "날짜", example = "2025-02-04")
    private LocalDate date;

    @NotBlank(message = "내용을 작성하세요")
    @Size(max = 100, message = "일기 내용은 100자를 초과할 수 없습니다.")
    @Schema(description = "일기 내용", example = "영원한건 절대 없어 결국에 넌 변했지 이유도 없어 진심이 없어 사랑같은 소리 따윈 집어쳐 오늘 밤은 삐딱하게, 내버려둬 결국에 난 혼자였지 아무도 없어 다 의미 없어 ")
    private String content;
}
