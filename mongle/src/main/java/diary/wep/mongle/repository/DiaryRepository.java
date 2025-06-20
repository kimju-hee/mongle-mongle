package diary.wep.mongle.repository;

import diary.wep.mongle.entity.EmotionDiary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<EmotionDiary, Long> {

}
