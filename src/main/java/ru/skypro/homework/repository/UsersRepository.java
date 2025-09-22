package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.entity.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
}
