package diary.wep.mongle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class KakaoApproveResponse {

    @JsonProperty("aid")
    private String aid;

    @JsonProperty("tid")
    private String tid;

    @JsonProperty("partner_order_id")
    private String orderId;

    @JsonProperty("partner_user_id")
    private String userId;

    @JsonProperty("item_name")
    private String itemName;

    @JsonProperty("amount")
    private Amount amount;

    @Getter
    public static class Amount {
        @JsonProperty("total")
        private Integer total;
    }
}
