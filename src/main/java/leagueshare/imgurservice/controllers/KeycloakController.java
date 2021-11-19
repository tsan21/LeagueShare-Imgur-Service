package leagueshare.imgurservice.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/keycloak")
public class KeycloakController {

    @GetMapping("/user")
    public String userPage() {
        return "User page";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "Admin page";
    }

    @GetMapping("/test")
    public String test() {
        return "test test";
    }
}
