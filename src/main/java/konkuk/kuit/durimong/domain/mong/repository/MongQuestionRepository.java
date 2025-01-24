package konkuk.kuit.durimong.domain.mong.repository;

import konkuk.kuit.durimong.domain.mong.entity.MongQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MongQuestionRepository extends JpaRepository<MongQuestion, Long> {
    List<MongQuestion> findAll();
}
