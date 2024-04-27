package edu.mirea.candy_shop.service;

import edu.mirea.candy_shop.dao.entity.CustomerEntity;
import edu.mirea.candy_shop.dao.repository.CustomerRepository;
import edu.mirea.candy_shop.dto.JwtAuthenticationResponse;
import edu.mirea.candy_shop.dto.Role;
import edu.mirea.candy_shop.dto.requests.SignInRequest;
import edu.mirea.candy_shop.dto.requests.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final CustomerRepository customerRepository;
    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationResponse signup(SignUpRequest request) {
        CustomerEntity customer = CustomerEntity.builder()
                .customerName(request.customerName())
                .customerSurname(request.customerSurname())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.ROLE_USER)
                .build();

        customer = customerService.save(customer);
        var jwt = jwtService.generateToken(customer);
        return new JwtAuthenticationResponse(jwt);
    }


    public JwtAuthenticationResponse signin(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        var user = customerRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

}
