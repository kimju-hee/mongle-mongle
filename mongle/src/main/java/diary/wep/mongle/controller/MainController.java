package diary.wep.mongle.controller;

import diary.wep.mongle.entity.Users;
import diary.wep.mongle.repository.UserRepository;
import diary.wep.mongle.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping("/main")
    public String main(Model model, OAuth2AuthenticationToken authentication, HttpSession session) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        OAuth2User oAuth2User = authentication.getPrincipal();
        String providerId = oAuth2User.getName();

        Users user = userRepository.findByProviderId(providerId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));

        session.setAttribute("userId", user.getUserId());
        session.setAttribute("nickname", user.getNickname());

        model.addAttribute("nickname", user.getNickname());
        return "diary";
    }


    @GetMapping("/api/user/me")
    public String mypage(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login-page";

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));

        model.addAttribute("email", user.getEmail() != null ? user.getEmail() : "카카오 로그인");
        model.addAttribute("nickname", user.getNickname());
        return "mypage";
    }


    @PostMapping("/api/user/update")
    public String updateNickname(@RequestParam("nickname") String newNickname, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            userService.updateNickname(userId, newNickname);
            session.setAttribute("nickname", newNickname);
        }
        return "redirect:/api/user/me";
    }


    @GetMapping("/login-page")
    public String loginPage() {
        return "login";
    }
}
