package diary.wep.mongle.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "email", nullable = true, unique = true)
    private String email;

    @Column(name = "password") // 소셜 로그인은 비밀번호가 없을 수 있으므로 nullable 허용
    private String password;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "provider") // ex) "local", "kakao"
    private String provider;

    @Column(name = "provider_id", nullable = false) // 새 필드 추가
    private String providerId;

    @Builder
    public Users(String email, String password, String nickname, String provider, String providerId) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.provider = provider;
        this.providerId = providerId;
    }

}

