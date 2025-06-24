package diary.wep.mongle.controller;

import diary.wep.mongle.dto.EmotionDiaryDTO;
import diary.wep.mongle.entity.EmotionDiary;
import diary.wep.mongle.entity.Emotions;
import diary.wep.mongle.entity.Users;
import diary.wep.mongle.repository.DiaryRepository;
import diary.wep.mongle.repository.EmotionRepository;
import diary.wep.mongle.repository.UserRepository;
import diary.wep.mongle.service.DiaryService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;
    private final EmotionRepository emotionRepository;
    private final DiaryService diaryService;

    @GetMapping("/write")
    public String redirectToDiary(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login-page";
        String nickname = (String) session.getAttribute("nickname");

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        model.addAttribute("nickname", user.getNickname());
        return "diary";
    }


    @PostMapping("/api/diary")
    public String saveDiary(@RequestParam("title") String title,
                            @RequestParam("content") String content,
                            @RequestParam("emotion") String emotionName, // 추가
                            HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login-page";
        String nickname = (String) session.getAttribute("nickname");

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        Emotions emotion = emotionRepository.findByEmotionName(emotionName.toUpperCase()) // 추가
                .orElseThrow(() -> new IllegalArgumentException("해당 감정 없음: " + emotionName));

        EmotionDiary diary = EmotionDiary.builder()
                .diaryDate(LocalDate.now())
                .title(title)
                .content(content)
                .user(user)
                .emotion(emotion)
                .build();

        diaryRepository.save(diary);
        return "redirect:/diarylist";
    }


    @GetMapping("/api/diary")
    public ResponseEntity<List<EmotionDiaryDTO>> getDiariesByMonth(
            @RequestParam(value = "month", required = false) String month,
            HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        List<EmotionDiary> diaries = (month == null || month.isEmpty())
                ? diaryRepository.findAllByUserUserId(userId)
                : diaryService.findByUserIdAndMonth(userId, month);

        List<EmotionDiaryDTO> dtoList = diaries.stream()
                .map(EmotionDiaryDTO::new)
                .toList();

        return ResponseEntity.ok(dtoList);
    }



    @GetMapping("/api/diary/{id}")
    public ResponseEntity<EmotionDiary> getDiaryDetail(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        EmotionDiary diary = diaryService.findByIdAndUserId(id, userId);
        return ResponseEntity.ok(diary);
    }

    @GetMapping("/diarylist")
    public String diaryList(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String nickname = (String) session.getAttribute("nickname");

        if (userId == null) return "redirect:/login-page";

        List<EmotionDiary> diaryList = diaryRepository.findAllByUserUserId(userId);
        model.addAttribute("nickname", nickname);
        model.addAttribute("diaryList", diaryList);
        return "diarylist";
    }


    @GetMapping("/diary/{id}")
    public String diaryDetail(@PathVariable Long id, Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        EmotionDiary diary = diaryRepository.findByDiaryIdAndUserUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("일기 없음"));
        model.addAttribute("diary", diary);
        return "diary-detail";
    }

    @GetMapping("/diary/{id}/edit")
    public String editDiaryForm(@PathVariable Long id, HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        EmotionDiary diary = diaryRepository.findByDiaryIdAndUserUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("일기 없음"));
        model.addAttribute("diary", diary);
        return "diary-edit";
    }

    @PostMapping("/diary/{id}/edit")
    public String updateDiary(@PathVariable Long id,
                              @RequestParam String title,
                              @RequestParam String content,
                              HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        EmotionDiary diary = diaryRepository.findByDiaryIdAndUserUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("일기 없음"));

        diary.setTitle(title);
        diary.setContent(content);
        diaryRepository.save(diary);

        return "redirect:/diary/" + id;
    }

    @PostMapping("/diary/{id}/delete")
    public String deleteDiary(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        EmotionDiary diary = diaryRepository.findByDiaryIdAndUserUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("일기 없음"));

        diaryRepository.delete(diary);
        return "redirect:/diarylist";
    }

}
