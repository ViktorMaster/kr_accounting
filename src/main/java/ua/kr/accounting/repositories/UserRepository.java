package ua.kr.accounting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.kr.accounting.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
