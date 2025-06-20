package diary.wep.mongle.service;

import diary.wep.mongle.entity.EmotionDiary;
import diary.wep.mongle.entity.Emotions;
import diary.wep.mongle.entity.Users;
import diary.wep.mongle.repository.DiaryRepository;
import diary.wep.mongle.repository.EmotionRepository;
import diary.wep.mongle.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;
    private final EmotionRepository emotionRepository;

    public void saveDiary(String content, String responseText, String emotionName, LocalDate date, Long userId) {
        Users user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));

        Emotions emotion = emotionRepository.findByEmotionName(emotionName)
                .orElseThrow(() -> new IllegalArgumentException("감정 없음: " + emotionName));

        EmotionDiary diary = EmotionDiary.builder()
                .diaryDate(date)
                .content(content)
                .responseText(responseText)
                .build();

        diary.setUser(user);
        diary.setEmotion(emotion);

        diaryRepository.save(diary);
    }

    public List<EmotionDiary> findByUserIdAndMonth(Long userId, String month) {
        YearMonth ym = YearMonth.parse(month);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();
        return diaryRepository.findAllByUserUserIdAndDiaryDateBetween(userId, start, end);
    }

    public EmotionDiary findByIdAndUserId(Long diaryId, Long userId) {
        return diaryRepository.findByDiaryIdAndUserUserId(diaryId, userId)
                .orElseThrow(() -> new RuntimeException("일기를 찾을 수 없습니다."));
    }

}
