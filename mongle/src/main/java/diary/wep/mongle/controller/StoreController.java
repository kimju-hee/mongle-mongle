package diary.wep.mongle.controller;

import diary.wep.mongle.repository.ItemRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class StoreController {

    private final ItemRepository itemRepository;

    @GetMapping("/store")
    public String store(Model model) {
        model.addAttribute("items", itemRepository.findAll());
        return "store";
    }
}

