package ua.kr.accounting.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.kr.accounting.models.User;
import ua.kr.accounting.models.enums.Role;
import ua.kr.accounting.repositories.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) return false;
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.ROLE_USER);

        userRepository.save(user);
        return true;
    }
}
