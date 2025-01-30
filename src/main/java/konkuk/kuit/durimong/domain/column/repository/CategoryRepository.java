package konkuk.kuit.durimong.domain.column.repository;

import konkuk.kuit.durimong.domain.column.entity.ColumnCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<ColumnCategory, Long> {
   // boolean findByName(String name);
}
