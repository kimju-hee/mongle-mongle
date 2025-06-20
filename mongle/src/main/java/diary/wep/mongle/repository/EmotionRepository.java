package diary.wep.mongle.repository;

import diary.wep.mongle.entity.Emotions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmotionRepository extends JpaRepository<Emotions, Long> {
    Optional<Emotions> findByEmotionName(String emotionName);
}