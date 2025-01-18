package konkuk.kuit.durimong.global.response.result;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class ParameterData {
    private String key;
    private String value;
    private String reason;
}
