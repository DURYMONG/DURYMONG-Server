package konkuk.kuit.durimong.domain.test.repository;

import konkuk.kuit.durimong.domain.test.entity.TestContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestContentRepository extends JpaRepository<TestContent, Long> {
}
