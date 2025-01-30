package konkuk.kuit.durimong.domain.column.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "article_category")
public class ColumnCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @jakarta.persistence.Column(name = "category_id")
    private Long categoryId;

    @jakarta.persistence.Column(nullable = false)
    private String name;

    @jakarta.persistence.Column(nullable = false)
    private String detail;

    @jakarta.persistence.Column(nullable = false)
    private String image;

}
