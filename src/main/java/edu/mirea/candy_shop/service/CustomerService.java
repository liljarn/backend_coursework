package edu.mirea.candy_shop.service;

import edu.mirea.candy_shop.dao.entity.CustomerEntity;
import edu.mirea.candy_shop.dao.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public UserDetailsService userDetailsService() {
        return username -> customerRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public CustomerEntity save(CustomerEntity newUser) {
        return customerRepository.save(newUser);
    }
}
