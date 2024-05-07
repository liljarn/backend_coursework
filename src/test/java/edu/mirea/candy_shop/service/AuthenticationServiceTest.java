package edu.mirea.candy_shop.service;

import edu.mirea.candy_shop.dao.entity.CustomerEntity;
import edu.mirea.candy_shop.dao.repository.CustomerRepository;
import edu.mirea.candy_shop.dto.Role;
import edu.mirea.candy_shop.dto.requests.SignInRequest;
import edu.mirea.candy_shop.dto.requests.SignUpRequest;
import edu.mirea.candy_shop.dto.response.JwtAuthenticationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AuthenticationServiceTest {
    @InjectMocks
    private AuthenticationService authenticationService;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerService customerService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void signup_SuccessfullyRegistered() {
        SignUpRequest signUpRequest = new SignUpRequest("John", "Doe", "john@example.com", "password");
        CustomerEntity savedCustomer = new CustomerEntity();
        savedCustomer.setEmail(signUpRequest.email());
        when(jwtService.generateToken(any())).thenReturn("dummyToken");
        when(customerService.save(any())).thenReturn(savedCustomer);
        JwtAuthenticationResponse response = authenticationService.signup(signUpRequest);
        assertThat(response).isNotNull();
        assertThat(response.token()).isNotEmpty();
    }

    @Test
    void signin_SuccessfullyAuthenticated() {
        SignInRequest signInRequest = new SignInRequest("john@example.com", "password");
        CustomerEntity user = new CustomerEntity();
        user.setEmail(signInRequest.email());
        user.setPassword("password");
        when(jwtService.generateToken(user)).thenReturn("dummyToken");
        when(customerRepository.findByEmail(signInRequest.email())).thenReturn(java.util.Optional.of(user));
        JwtAuthenticationResponse response = authenticationService.signin(signInRequest);
        assertThat(response).isNotNull();
        assertThat(response.token()).isNotEmpty();
    }

    @Test
    void signin_InvalidEmailOrPassword_ThrowsException() {
        SignInRequest signInRequest = new SignInRequest("john@example.com", "password");
        when(customerRepository.findByEmail(signInRequest.email())).thenReturn(java.util.Optional.empty());
        assertThatThrownBy(() -> authenticationService.signin(signInRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void isAuthorized_TokenNotExpired_ReturnsTrue() {
        String token = "dummyToken";
        when(jwtService.isTokenExpired(token)).thenReturn(false);
        assertThat(authenticationService.isAuthorized(token)).isTrue();
    }

    @Test
    void isAdmin_UserIsAdmin_ReturnsTrue() {
        String token = "dummyToken";
        CustomerEntity adminUser = new CustomerEntity();
        adminUser.setEmail("admin@example.com");
        adminUser.setRole(Role.ROLE_ADMIN);
        when(jwtService.extractUserName(token)).thenReturn(adminUser.getEmail());
        when(customerRepository.findByEmail(adminUser.getEmail())).thenReturn(java.util.Optional.of(adminUser));
        assertThat(authenticationService.isAdmin(token)).isTrue();
    }

    @Test
    void isAdmin_UserIsNotAdmin_ReturnsFalse() {
        String token = "dummyToken";
        CustomerEntity regularUser = new CustomerEntity();
        regularUser.setEmail("user@example.com");
        regularUser.setRole(Role.ROLE_USER);
        when(jwtService.extractUserName(token)).thenReturn(regularUser.getEmail());
        when(customerRepository.findByEmail(regularUser.getEmail())).thenReturn(java.util.Optional.of(regularUser));
        assertThat(authenticationService.isAdmin(token)).isFalse();
    }
}
