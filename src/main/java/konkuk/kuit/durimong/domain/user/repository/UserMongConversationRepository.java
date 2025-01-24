package konkuk.kuit.durimong.domain.user.repository;


import konkuk.kuit.durimong.domain.mong.entity.MongQuestion;
import konkuk.kuit.durimong.domain.user.entity.User;
import konkuk.kuit.durimong.domain.user.entity.UserMongConversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMongConversationRepository extends JpaRepository<UserMongConversation, Long> {
    UserMongConversation save(UserMongConversation conversation);
    UserMongConversation findByQuestion(MongQuestion mongQuestion);
    UserMongConversation findByUserAndQuestion(User user, MongQuestion mongQuestion);
}
