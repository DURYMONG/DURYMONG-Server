package konkuk.kuit.durimong.domain.test.repository;

import konkuk.kuit.durimong.domain.test.entity.Test;
import konkuk.kuit.durimong.domain.test.entity.TestQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestQuestionRepository extends JpaRepository<TestQuestion, Long> {
    List<TestQuestion> findByTest(Test test);
}
