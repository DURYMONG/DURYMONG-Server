package konkuk.kuit.durimong.domain.column.repository;

import konkuk.kuit.durimong.domain.column.entity.ColumnCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<ColumnCategory, Long> {

}
