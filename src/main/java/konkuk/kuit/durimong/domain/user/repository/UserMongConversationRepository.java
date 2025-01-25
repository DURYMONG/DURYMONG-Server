package konkuk.kuit.durimong.domain.user.repository;


import konkuk.kuit.durimong.domain.mong.entity.MongQuestion;
import konkuk.kuit.durimong.domain.user.entity.User;
import konkuk.kuit.durimong.domain.user.entity.UserMongConversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface UserMongConversationRepository extends JpaRepository<UserMongConversation, Long> {
    UserMongConversation save(UserMongConversation conversation);
    Optional<UserMongConversation> findByUserAndQuestion(User user, MongQuestion mongQuestion);
    Optional<UserMongConversation> findByCreatedAtAndUser(LocalDate createdAt, User user);
}
