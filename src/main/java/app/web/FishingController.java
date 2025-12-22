package app.web;

import app.fish.property.FishProperties.FishDetails;
import app.fish.service.FishService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@Controller
@AllArgsConstructor
public class FishingController {

    private final FishService fishService;

    @GetMapping("/pick-random-fish")
    public String pickFish(HttpSession session) {
        FishDetails fish = fishService.getRandomFish();
        session.setAttribute("randomFish", fish);
        return "redirect:/fishing-spot";
    }

    @GetMapping("/fishing-spot")
    public String fishingSpot(HttpSession session, Model model) {
        FishDetails fish = (FishDetails) session.getAttribute("randomFish");

        if (fish == null) {
            return "redirect:/home";
        }

        model.addAttribute("fish", fish);
        return "fishing-spot";
    }

    @PostMapping("/catch-fish")
    public String takeOut(HttpSession session) {
        UUID fishermanId = (UUID) session.getAttribute("user_id");
        FishDetails fish = (FishDetails) session.getAttribute("randomFish");

        if (fish != null) {
            fishService.catchFish(fishermanId, fish);
            session.removeAttribute("randomFish");
        }

        return "redirect:/home";
    }
}
