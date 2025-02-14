package konkuk.kuit.durimong.domain.test.repository;

import konkuk.kuit.durimong.domain.test.dto.response.SubmitTestRes;
import konkuk.kuit.durimong.domain.test.entity.ScoreDistribution;
import konkuk.kuit.durimong.domain.test.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScoreDistributionRepository extends JpaRepository<ScoreDistribution, Long> {
    List<ScoreDistribution> findByTest(Test test);
}
