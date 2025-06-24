package diary.wep.mongle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class KakaoReadyResponse {
    @JsonProperty("tid")
    private String tid;

    @JsonProperty("next_redirect_pc_url")
    private String nextRedirectPcUrl;
}
