package diary.wep.mongle.service;

import diary.wep.mongle.dto.SignupRequestDto;
import diary.wep.mongle.entity.Users;
import diary.wep.mongle.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(SignupRequestDto signupRequestDto) {
        if (userRepository.existsByEmail(signupRequestDto.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());

        Users user = Users.builder()
                .email(signupRequestDto.getEmail())
                .password(encodedPassword)
                .nickname(signupRequestDto.getNickname())
                .build();

        userRepository.save(user);
    }

    @Transactional
    public void updateNickname(Long userId, String newNickname) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));
        user.setNickname(newNickname);
    }


    public boolean isNicknameTaken(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public boolean isEmailTaken(String email) {
        return userRepository.existsByEmail(email);
    }
}
