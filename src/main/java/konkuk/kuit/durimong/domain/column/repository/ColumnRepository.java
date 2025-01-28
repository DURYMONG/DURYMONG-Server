package konkuk.kuit.durimong.domain.column.repository;

import konkuk.kuit.durimong.domain.column.entity.Column;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ColumnRepository extends JpaRepository<Column, Long> {
    Optional<Column> findByCategory_CategoryId(Long categoryId);
}
