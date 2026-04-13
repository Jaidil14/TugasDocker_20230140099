package com.tugas.deploy.controller;

import com.tugas.deploy.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * UserController - menangani semua routing dan logika aplikasi:
 * - Login / Logout
 * - Halaman Home (menampilkan daftar mahasiswa)
 * - Form input mahasiswa (data temporary, tidak disimpan ke database)
 */
@Controller
public class UserController {

    // =====================================================
    // Credentials login (username = admin, password = NIM)
    // Ganti NIM_ANDA dengan NIM kamu
    // =====================================================
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "20230140099";

    // =====================================================
    // Storage temporary untuk data mahasiswa (in-memory)
    // Data akan hilang saat server di-restart
    // =====================================================
    private final List<User> mahasiswaList = new ArrayList<>();

    // ─────────────────────────────────────────────────────
    // GET /  →  redirect ke /login
    // ─────────────────────────────────────────────────────
    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    // ─────────────────────────────────────────────────────
    // GET /login  →  tampilkan halaman login
    // ─────────────────────────────────────────────────────
    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        // Jika sudah login, langsung ke home
        if (session.getAttribute("loggedIn") != null) {
            return "redirect:/home";
        }
        return "login";
    }

    // ─────────────────────────────────────────────────────
    // POST /login  →  proses login
    // ─────────────────────────────────────────────────────
    @PostMapping("/login")
    public String loginProcess(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {
        if (USERNAME.equals(username) && PASSWORD.equals(password)) {
            // Login berhasil → simpan session
            session.setAttribute("loggedIn", true);
            session.setAttribute("nim", PASSWORD); // NIM dipakai untuk judul di home
            return "redirect:/home";
        } else {
            // Login gagal → kembali ke halaman login dengan pesan error
            model.addAttribute("error", "Username atau password salah!");
            return "login";
        }
    }

    // ─────────────────────────────────────────────────────
    // GET /home  →  tampilkan halaman home
    // ─────────────────────────────────────────────────────
    @GetMapping("/home")
    public String homePage(HttpSession session, Model model) {
        // Cek apakah sudah login
        if (session.getAttribute("loggedIn") == null) {
            return "redirect:/login";
        }

        // Kirim data ke template
        model.addAttribute("nim", session.getAttribute("nim"));
        model.addAttribute("mahasiswaList", mahasiswaList);
        return "home";
    }

    // ─────────────────────────────────────────────────────
    // GET /form  →  tampilkan form input mahasiswa
    // ─────────────────────────────────────────────────────
    @GetMapping("/form")
    public String formPage(HttpSession session) {
        if (session.getAttribute("loggedIn") == null) {
            return "redirect:/login";
        }
        return "form";
    }

    // ─────────────────────────────────────────────────────
    // POST /form  →  proses input data mahasiswa
    // ─────────────────────────────────────────────────────
    @PostMapping("/form")
    public String formProcess(@RequestParam String nama,
                              @RequestParam String nim,
                              @RequestParam String jenisKelamin,
                              HttpSession session) {
        if (session.getAttribute("loggedIn") == null) {
            return "redirect:/login";
        }

        // Simpan data mahasiswa ke list (temporary / in-memory)
        User user = new User(nama, nim, jenisKelamin);
        mahasiswaList.add(user);

        // Redirect ke home setelah submit
        return "redirect:/home";
    }

    // ─────────────────────────────────────────────────────
    // GET /logout  →  hapus session dan redirect ke login
    // ─────────────────────────────────────────────────────
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}