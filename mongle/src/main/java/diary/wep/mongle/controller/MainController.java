package diary.wep.mongle.controller;

import diary.wep.mongle.entity.Users;
import diary.wep.mongle.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UserRepository userRepository;

    @GetMapping("/main")
    public String main(Model model, @AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails) {
        String email = userDetails.getUsername();
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일 없음: " + email));

        model.addAttribute("email", user.getEmail());
        model.addAttribute("nickname", user.getNickname());

        return "diary";
    }

    @GetMapping("/login-page")
    public String loginPage() {
        return "login";
    }
}
