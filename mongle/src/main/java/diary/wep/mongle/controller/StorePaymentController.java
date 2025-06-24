package diary.wep.mongle.controller;

import diary.wep.mongle.dto.KakaoApproveResponse;
import diary.wep.mongle.service.KakaoPayService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class StorePaymentController {

    private final KakaoPayService kakaoPayService;

    @PostMapping("/pay/ready")
    public String kakaoPayReady(@RequestParam("itemId") Long itemId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String redirectUrl = kakaoPayService.kakaoPayReady(itemId, userId);
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/pay/success")
    public String kakaoPaySuccess(@RequestParam("pg_token") String pgToken,
                                  @RequestParam("itemId") Long itemId,
                                  HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        KakaoApproveResponse result = kakaoPayService.approvePayment(pgToken, itemId, userId);
        return "redirect:/store"; // 결제 완료 후 이동할 페이지
    }

    @GetMapping("/pay/cancel")
    public String kakaoPayCancel() {
        return "redirect:/store?cancel=true";
    }

    @GetMapping("/pay/fail")
    public String kakaoPayFail() {
        return "redirect:/store?fail=true";
    }
}
