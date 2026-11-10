package com.example.main;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginForm {

    private final UserService userService;
    public LoginForm(UserService userService) {
        this.userService = userService;
    }

    //Login
    @GetMapping({"/", "/login"})
    public String loginForm() {
        return "LoginForm";
    }

    @PostMapping("/login")
    public String submitLogin(@RequestParam String username,
                              @RequestParam String password,
                              Model model) {
        model.addAttribute("username", username);
        if (username.isBlank() || password.isBlank()) {
            model.addAttribute("error", "សូមបញ្ចូលឈ្មោះគណនី និងលេខសម្ងាត់! (Please enter username and password)");
            return "LoginForm";
        }
        if (!userService.authenticate(username, password)) {
            model.addAttribute("error", "ឈ្មោះគណនី ឬលេខសម្ងាត់មិនត្រឹមត្រូវទេ! (Invalid username or password)");
            return "LoginForm";
        }
        model.addAttribute("name", username.trim());
        return "dashboard/Dashboard";
    }

    //Register
    @GetMapping("/register")
    public String registerForm() {
        return "RegisterForm";
    }

    @PostMapping("/register")
    public String submitRegister(@RequestParam String username,
                                 @RequestParam(required = false, defaultValue = "") String email,
                                 @RequestParam String password,
                                 @RequestParam String confirmPassword,
                                 Model model) {
        model.addAttribute("username", username);
        model.addAttribute("email", email);

        // ១. ពិនិត្យប្រអប់ទទេ
        if (username.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            model.addAttribute("error", "សូមបំពេញគ្រប់ប្រអប់ដែលចាំបាច់! (Please fill in all required fields)");
            return "RegisterForm";
        }
        // ២. ពិនិត្យមើលលេខសម្ងាត់ទាំងពីរថាតើដូចគ្នាឬអត់
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "លេខសម្ងាត់ទាំងពីរមិនដូចគ្នាទេ! (Passwords do not match)");
            return "RegisterForm";
        }
        // ៣. ពិនិត្យប្រវែងលេខសម្ងាត់
        if (password.length() < 4) {
            model.addAttribute("error", "លេខសម្ងាត់ត្រូវមានយ៉ាងហោចណាស់ ៤ តួអក្សរឡើងទៅ! (Password must be at least 4 characters)");
            return "RegisterForm";
        }
        // ៤. ចុះឈ្មោះ និងពិនិត្យថាស្ទួន Username ឬអត់
        boolean created = userService.register(username, email, password);
        if (!created) {
            model.addAttribute("error", "ឈ្មោះគណនី '" + username + "' នេះមានគេប្រើរួចហើយ! (Username already exists)");
            return "RegisterForm";
        }
        // ចុះឈ្មោះបានជោគជ័យ -> បញ្ជូនទៅកាន់ផ្ទាំង Login វិញ ព្រមទាំងបំពេញ Username ស្រេច
        model.addAttribute("success", "ចុះឈ្មោះបានជោគជ័យ! សូមចូលប្រើប្រាស់គណនីរបស់អ្នក (Registration successful! Please login).");
        model.addAttribute("username", username.trim());
        return "LoginForm";
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        model.addAttribute("success", "អ្នកបានចាកចេញដោយជោគជ័យ (You have logged out successfully).");
        return "LoginForm";
    }

    @RequestMapping("/welcome")
    public String welcomePage(Model md) {
        md.addAttribute("name", "Student / User");
        return "welcome";
    }
}
