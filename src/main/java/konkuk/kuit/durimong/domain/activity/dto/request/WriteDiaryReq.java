package konkuk.kuit.durimong.domain.activity.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class WriteDiaryReq {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Schema(description = "날짜", example = "2024-11-28")
    private LocalDate date;

    @Schema(description = "일기 내용", example = "오늘은 30분동안 공원을 걸었다..")
    private String content;
}
