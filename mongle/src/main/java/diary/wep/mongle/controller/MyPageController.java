package diary.wep.mongle.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import diary.wep.mongle.entity.Payments;
import diary.wep.mongle.entity.EmotionDiary;
import diary.wep.mongle.repository.DiaryRepository;
import diary.wep.mongle.repository.PaymentRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MyPageController {

    private final PaymentRepository paymentRepository;
    private final DiaryRepository diaryRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @GetMapping("/mypayments")
    public String getUserPayments(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        String nickname = (String) session.getAttribute("nickname");

        List<Payments> payments = paymentRepository.findAllWithItemByUserId(userId);
        model.addAttribute("payments", payments);
        model.addAttribute("nickname", nickname);
        return "mypayments";
    }
    @GetMapping("/emotion/report")
    public String getEmotionReport(HttpSession session,
                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                   Model model) throws Exception {
        Long userId = (Long) session.getAttribute("userId");
        String nickname = (String) session.getAttribute("nickname");
        model.addAttribute("nickname", nickname);

        List<EmotionDiary> diaries;

        if (startDate != null && endDate != null) {
            diaries = diaryRepository.findAllByUserUserIdAndDiaryDateBetween(userId, startDate, endDate);
        } else {
            diaries = diaryRepository.findAllByUserUserId(userId);
        }

        Map<String, Long> emotionStats = diaries.stream()
                .collect(Collectors.groupingBy(d -> d.getEmotion().getEmotionName(), Collectors.counting()));

        Map<String, Long> sortedStats = emotionStats.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        model.addAttribute("emotionStatsJson", objectMapper.writeValueAsString(sortedStats));
        return "emotionReport";
    }

}
