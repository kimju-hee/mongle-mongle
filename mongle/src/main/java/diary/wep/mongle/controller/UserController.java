package diary.wep.mongle.controller;

import diary.wep.mongle.dto.SignupRequestDto;
import diary.wep.mongle.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public String signupPage() {
        return "signup"; // templates/signup.html 또는 static/signup.html
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute SignupRequestDto requestDto) {
        userService.signup(requestDto);
        return "login";
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<?> checkNickname(@RequestParam String nickname) {
        boolean isDuplicate = userService.isNicknameTaken(nickname);
        return ResponseEntity.ok(!isDuplicate);
    }

    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        boolean isDuplicate = userService.isEmailTaken(email);
        return ResponseEntity.ok(!isDuplicate); // 중복이 없으면 true 반환
    }


    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    
}
