package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.entity.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * метод ля поиска коментариев конкретного объявления, с фильтрацией по времени создания
     * @param adsId - ID нужного объявления
     */
    List<Comment> findByAdvertisementIdOrderByCreatedAtDesc(Long adsId);

    /**
     * метод ля поиска конкретного коментария, к определенному объявлению
     * @param id - ID комментария
     * @param adsId - ID объявления
     */
    Optional<Comment> findByIdAndAdvertisementId(Long id, Long adsId);
}
