package diary.wep.mongle.service;

import diary.wep.mongle.dto.KakaoApproveResponse;
import diary.wep.mongle.dto.KakaoReadyResponse;
import diary.wep.mongle.entity.Items;
import diary.wep.mongle.entity.Payments;
import diary.wep.mongle.entity.Users;
import diary.wep.mongle.repository.ItemRepository;
import diary.wep.mongle.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoPayService {

    private final ItemRepository itemRepository;
    private final PaymentRepository paymentRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    private static final String KAKAO_HOST = "https://kapi.kakao.com";
    private static final Map<String, String> tidStore = new HashMap<>();

    @Value("${kakao.secret-key}")
    private String secretKey;

    @Value("${kakao.admin-key}")
    private String adminKey;

    public String kakaoPayReady(Long itemId, Long userId) {
        Items item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("아이템 없음"));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + adminKey);
        headers.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", "TC0ONETIME");
        params.add("partner_order_id", "ORDER2025");
        params.add("partner_user_id", userId.toString());
        params.add("item_name", item.getItemName());
        params.add("quantity", "1");
        params.add("total_amount", item.getItemPrice().toString());
        params.add("vat_amount", "0");
        params.add("tax_free_amount", "0");
        params.add("approval_url", "http://localhost:8080/pay/success?itemId=" + itemId);
        params.add("cancel_url", "http://localhost:8080/pay/cancel");
        params.add("fail_url", "http://localhost:8080/pay/fail");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<KakaoReadyResponse> response = restTemplate.postForEntity(
                KAKAO_HOST + "/v1/payment/ready", request, KakaoReadyResponse.class
        );

        tidStore.put(userId.toString(), response.getBody().getTid());
        return response.getBody().getNextRedirectPcUrl();
    }

    public KakaoApproveResponse approvePayment(String pgToken, Long itemId, Long userId) {
        String tid = tidStore.get(userId.toString());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + adminKey);
        headers.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", "TC0ONETIME");
        params.add("tid", tid);
        params.add("partner_order_id", "ORDER2025");
        params.add("partner_user_id", userId.toString());
        params.add("pg_token", pgToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<KakaoApproveResponse> response = restTemplate.postForEntity(
                KAKAO_HOST + "/v1/payment/approve", request, KakaoApproveResponse.class
        );

        Payments payment = Payments.builder()
                .status("DONE")
                .method("KAKAO")
                .amount(response.getBody().getAmount().getTotal())
                .kakaoTid(tid)
                .paidAt(LocalDate.now())
                .user(Users.builder().userId(userId).build())
                .item(itemRepository.findById(itemId).orElseThrow())
                .build();


        paymentRepository.save(payment);
        return response.getBody();
    }
}
