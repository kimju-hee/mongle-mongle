package diary.wep.mongle.repository;

import diary.wep.mongle.entity.EmotionDiary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<EmotionDiary, Long> {
    List<EmotionDiary> findAllByUserUserIdAndDiaryDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
    List<EmotionDiary> findAllByUser_UserIdAndDiaryDateBetween(Long userId, LocalDate start, LocalDate end);
    Optional<EmotionDiary> findByDiaryIdAndUserUserId(Long diaryId, Long userId);
    List<EmotionDiary> findAllByUserUserId(Long userId);

}
