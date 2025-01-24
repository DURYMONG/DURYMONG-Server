package konkuk.kuit.durimong.domain.mong.repository;

import konkuk.kuit.durimong.domain.mong.entity.MongQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface MongQuestionRepository extends JpaRepository<MongQuestion, Long> {
    List<MongQuestion> findAll();
    Optional<MongQuestion> findByQuestion(String question);

    @Query("SELECT m.question FROM MongQuestion m WHERE m.mongQuestionId = :date")
    Optional<String> findQuestionByDate(int date);

    @Query("SELECT m FROM MongQuestion m WHERE m.mongQuestionId = :date")
    Optional<MongQuestion> findMongQuestionByDate(int date);




}
