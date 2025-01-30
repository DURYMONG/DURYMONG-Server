package konkuk.kuit.durimong.domain.column.repository;

import konkuk.kuit.durimong.domain.column.entity.Column;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ColumnRepository extends JpaRepository<Column, Long> {
    Optional<Column> findByCategory_CategoryId(Long categoryId);

    // keyword에 해당하는 title -> subtitle -> content검색 (대소문자 구분 X)
    @Query("SELECT c FROM Column c " +
            "WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.subtitle) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Column> searchByKeyword(@Param("keyword") String keyword);

}
