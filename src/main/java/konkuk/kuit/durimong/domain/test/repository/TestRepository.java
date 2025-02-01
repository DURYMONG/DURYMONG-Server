package konkuk.kuit.durimong.domain.test.repository;

import konkuk.kuit.durimong.domain.test.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Long> {
}
