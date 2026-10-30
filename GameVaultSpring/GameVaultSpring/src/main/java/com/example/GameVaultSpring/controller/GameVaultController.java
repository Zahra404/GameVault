package com.example.GameVaultSpring.controller;

import com.example.GameVaultSpring.entity.Gamevault;
import com.example.GameVaultSpring.entity.User;
import com.example.GameVaultSpring.repository.GameVaultRepo;
import com.example.GameVaultSpring.repository.UserRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/gameVault")
public class GameVaultController {

    private final GameVaultRepo gameVaultRepo;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public GameVaultController(GameVaultRepo gameVaultRepo, UserRepo userRepo) {
        this.gameVaultRepo = gameVaultRepo;
        this.userRepo = userRepo;
    }

    @GetMapping("/home")
    public String index() {
        return "catalogue";
    }

    @GetMapping("/Adiboo")
    public String adibooPage() {
        return "Adiboo";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        String normalizedEmail = formatEmail(email);
        User user = userRepo.findByEmailIgnoreCase(normalizedEmail).orElse(null);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            model.addAttribute("error", "Invalid email or password.");
            return "login";
        }

        session.setAttribute("userId", user.getId());
        session.setAttribute("userEmail", user.getEmail());
        return "redirect:/gameVault/home";
    }

    @GetMapping("/register")
    public String registrationPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String email,
                           @RequestParam String password,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        String normalizedEmail = formatEmail(email);
        if (!normalizedEmail.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            model.addAttribute("error", "Enter a valid email address.");
            return "register";
        }
        if (password == null || password.length() < 8) {
            model.addAttribute("error", "Password must be at least 8 characters.");
            return "register";
        }
        if (userRepo.findByEmailIgnoreCase(normalizedEmail).isPresent()) {
            model.addAttribute("error", "An account with that email already exists.");
            return "register";
        }

        User user = new User();
        user.setEmail(normalizedEmail);
        user.setPassword(passwordEncoder.encode(password));
        userRepo.save(user);
        redirectAttributes.addFlashAttribute("successMessage", "Account created. Please log in.");
        return "redirect:/gameVault/login";
    }

    private String formatEmail(String email) {
        return email.trim().toLowerCase();
    }

    @GetMapping
    public List<Gamevault> getAllGames() {
        return gameVaultRepo.findAll();
    }

    @PostMapping("/newGame")
    @ResponseStatus(HttpStatus.CREATED)
    public Gamevault createNewGame(@RequestBody Gamevault gamevault) {
        return gameVaultRepo.save(gamevault);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGame(@PathVariable Integer id) {
        if (!gameVaultRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
        }
        gameVaultRepo.deleteById(id);
    }

}
