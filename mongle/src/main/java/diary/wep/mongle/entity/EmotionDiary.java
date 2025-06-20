package diary.wep.mongle.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "EmotionDiary")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmotionDiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long diaryId;

    @Column(name = "diary_date")
    private LocalDate diaryDate;

    @Column(name = "content")
    private String content;

    @Column(name = "response_text")
    private String responseText;

    @Column(name = "title")
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emotion_id", nullable = false)
    private Emotions emotion;



    @Builder
    public EmotionDiary(LocalDate diaryDate, String content, String responseText, String title, Users user, Emotions emotion) {
        this.diaryDate = diaryDate;
        this.content = content;
        this.responseText = responseText;
        this.title = title;
        this.user =  user;
        this.emotion = emotion;
    }
}
