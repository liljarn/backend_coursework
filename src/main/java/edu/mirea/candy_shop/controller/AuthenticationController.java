package edu.mirea.candy_shop.controller;

import edu.mirea.candy_shop.dto.requests.SignInRequest;
import edu.mirea.candy_shop.dto.requests.SignUpRequest;
import edu.mirea.candy_shop.dto.response.JwtAuthenticationResponse;
import edu.mirea.candy_shop.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public JwtAuthenticationResponse signup(@RequestBody SignUpRequest request) {
        return authenticationService.signup(request);
    }

    @PostMapping("/signin")
    public JwtAuthenticationResponse signin(@RequestBody SignInRequest request) {
        return authenticationService.signin(request);
    }

    @GetMapping
    public boolean isAuthorized(@RequestHeader("Token") String token) {
        return authenticationService.isAuthorized(token);
    }

    @GetMapping("/isadmin")
    public boolean isAdmin(@RequestHeader("Token") String token) {
        return authenticationService.isAdmin(token);
    }
}
