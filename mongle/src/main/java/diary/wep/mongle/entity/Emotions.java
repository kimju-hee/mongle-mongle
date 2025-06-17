package diary.wep.mongle.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Emotions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Emotions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emotion_id")
    private Long emotionId;

    @Column(name = "emotion_name")
    private String emotionName;

}
