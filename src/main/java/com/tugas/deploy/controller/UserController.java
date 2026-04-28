package com.tugas.deploy.controller;

import com.tugas.deploy.model.User;
import com.tugas.deploy.repository.UserRepository; // Import repository
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository; // Inject repository

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "20230140099";

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        if (session.getAttribute("loggedIn") != null) {
            return "redirect:/home";
        }
        return "login";
    }

    @PostMapping("/login")
    public String loginProcess(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {
        if (USERNAME.equals(username) && PASSWORD.equals(password)) {
            session.setAttribute("loggedIn", true);
            session.setAttribute("nim", PASSWORD);
            return "redirect:/home";
        } else {
            model.addAttribute("error", "Username atau password salah!");
            return "login";
        }
    }

    @GetMapping("/home")
    public String homePage(HttpSession session, Model model) {
        if (session.getAttribute("loggedIn") == null) {
            return "redirect:/login";
        }

        // Ambil data langsung dari database MySQL
        List<User> mahasiswaList = userRepository.findAll();

        model.addAttribute("nim", session.getAttribute("nim"));
        model.addAttribute("mahasiswaList", mahasiswaList);
        return "home";
    }

    @GetMapping("/form")
    public String formPage(HttpSession session) {
        if (session.getAttribute("loggedIn") == null) {
            return "redirect:/login";
        }
        return "form";
    }

    @PostMapping("/form")
    public String formProcess(@RequestParam String nama,
                              @RequestParam String nim,
                              @RequestParam String jenisKelamin,
                              HttpSession session) {
        if (session.getAttribute("loggedIn") == null) {
            return "redirect:/login";
        }

        // Simpan ke database MySQL
        User user = new User(nama, nim, jenisKelamin);
        userRepository.save(user);

        return "redirect:/home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}