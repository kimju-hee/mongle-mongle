package diary.wep.mongle.controller;

import diary.wep.mongle.entity.Users;
import diary.wep.mongle.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UserRepository userRepository;

    @GetMapping("/main")
    public String main(Model model, OAuth2AuthenticationToken authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        OAuth2User oAuth2User = authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String nickname = "익명";
        try {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            if (kakaoAccount != null) {
                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                if (profile != null && profile.get("nickname") != null) {
                    nickname = profile.get("nickname").toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("nickname", nickname);
        return "diary";
    }

    @GetMapping("/mypage")
    public String mypage(Model model, OAuth2AuthenticationToken authentication) {
        if (authentication == null) return "redirect:/login-page";

        OAuth2User oAuth2User = authentication.getPrincipal();
        Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String email = (String) kakaoAccount.get("email");
        String nickname = (String) profile.get("nickname");

        model.addAttribute("email", email != null ? email : "카카오 로그인");
        model.addAttribute("nickname", nickname);

        return "mypage";
    }


    @GetMapping("/login-page")
    public String loginPage() {
        return "login";
    }
}
