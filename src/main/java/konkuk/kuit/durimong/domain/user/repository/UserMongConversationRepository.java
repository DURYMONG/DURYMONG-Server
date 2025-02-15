package konkuk.kuit.durimong.domain.user.repository;


import konkuk.kuit.durimong.domain.mong.entity.MongQuestion;
import konkuk.kuit.durimong.domain.user.dto.response.UserChatHistoryRes;
import konkuk.kuit.durimong.domain.user.entity.User;
import konkuk.kuit.durimong.domain.user.entity.UserMongConversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserMongConversationRepository extends JpaRepository<UserMongConversation, Long> {
    UserMongConversation save(UserMongConversation conversation);
    UserMongConversation findByUserAndQuestion(User user, MongQuestion mongQuestion);
    Optional<UserMongConversation> findByCreatedAtAndUser(LocalDate createdAt, User user);
    @Query("SELECT new konkuk.kuit.durimong.domain.user.dto.response.UserChatHistoryRes(c.userMongConversationId,c.createdAt, c.mongQuestion, c.userAnswer) " +
            "FROM UserMongConversation c " +
            "WHERE c.user = :user " +
            "ORDER BY c.createdAt")
    List<UserChatHistoryRes> findAllByUser(User user);
    Optional<UserMongConversation> findByUserMongConversationId(Long conversationId);
    void deleteAllByUser(User user);
}
