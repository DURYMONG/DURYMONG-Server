package konkuk.kuit.durimong.domain.column.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "article")
public class Column {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @jakarta.persistence.Column(name = "article_id")
    private Long columnId;

    @jakarta.persistence.Column(nullable = false)
    private String title;

    @jakarta.persistence.Column(nullable = false)
    private String subtitle;

    @jakarta.persistence.Column(nullable = true)
    private String image;

    @jakarta.persistence.Column(nullable = false, length = 90000)
    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private ColumnCategory category;
}
