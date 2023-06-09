package ru.practicum.mainsvc.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainsvc.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
