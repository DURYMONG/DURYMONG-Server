package konkuk.kuit.durimong.domain.user.repository;


import konkuk.kuit.durimong.domain.user.entity.UserMongConversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMongConversationRepository extends JpaRepository<UserMongConversation, Long> {
}
