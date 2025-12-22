package app.web;

import app.fish.service.FishService;
import app.fisherman.model.Fisherman;
import app.fisherman.service.FishermanService;
import app.web.dto.LoginRequest;
import app.web.dto.RegisterRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@AllArgsConstructor
public class IndexController {

    private final FishermanService fishermanService;
    private final FishService fishService;

    @GetMapping
    public String getIndexPage() {

        return "index";
    }

    @GetMapping("/register")
    public ModelAndView getRegisterPage() {

        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject("registerRequest", new RegisterRequest());

        return modelAndView;
    }

    @GetMapping("/login")
    public ModelAndView getLoginPage() {

        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("loginRequest", new LoginRequest());

        return modelAndView;
    }

    @PostMapping("/register")
    public String register(@Valid RegisterRequest registerRequest, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "register";
        }

        boolean isSaved = fishermanService.register(registerRequest);

        if (!isSaved) {
            redirectAttributes.addFlashAttribute("registerRequest", registerRequest);
            redirectAttributes.addFlashAttribute("isExists", true);
            return "redirect:register";
        }

        return "redirect:/login";
    }

    @PostMapping("/login")
    public String login(@Valid LoginRequest loginRequest, BindingResult bindingResult, HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "login";
        }

        Fisherman player = fishermanService.login(loginRequest);
        session.setAttribute("user_id", player.getId());

        return "redirect:/home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {

        UUID fishermanId = (UUID) session.getAttribute("user_id");

        Fisherman fisherman = fishermanService.getById(fishermanId);

        model.addAttribute("myFishes", fisherman.getFishes());
        model.addAttribute("fishesForSale", fishService.getAllFishesForSale());
        model.addAttribute("user", fisherman);

        return "home";
    }

    @PostMapping("/fish/{id}/sale")
    public String putForSale(@PathVariable UUID id, HttpSession session) {
        UUID fishermanId = (UUID) session.getAttribute("user_id");
        fishService.putForSale(id, fishermanId);
        return "redirect:/home";
    }

    @PostMapping("/fish/{id}/buy")
    public String buyFish(@PathVariable UUID id, HttpSession session) {
        UUID buyerId = (UUID) session.getAttribute("user_id");
        fishService.buyFish(id, buyerId);
        return "redirect:/home";
    }

}
