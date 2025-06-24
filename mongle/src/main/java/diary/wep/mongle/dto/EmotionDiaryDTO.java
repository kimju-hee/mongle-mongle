package diary.wep.mongle.dto;

import diary.wep.mongle.entity.EmotionDiary;

import java.time.LocalDate;

public class EmotionDiaryDTO {

    private Long diaryId;
    private String title;
    private String content;
    private LocalDate diaryDate;
    private String emotionName;
    private String nickname;

    public EmotionDiaryDTO(EmotionDiary diary) {
        this.diaryId = diary.getDiaryId();
        this.title = diary.getTitle();
        this.content = diary.getContent();
        this.diaryDate = diary.getDiaryDate();
        this.emotionName = diary.getEmotion().getEmotionName();
        this.nickname = diary.getUser().getNickname();
    }

    public Long getDiaryId() {
        return diaryId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDate getDiaryDate() {
        return diaryDate;
    }

    public String getEmotionName() {
        return emotionName;
    }

    public String getNickname() {
        return nickname;
    }
}
