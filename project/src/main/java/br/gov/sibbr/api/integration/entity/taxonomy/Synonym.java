package br.gov.sibbr.api.integration.entity.taxonomy;

import br.gov.sibbr.api.integration.converter.LocalDateTimeConverter;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(schema = "taxonomy", name = "synonym")
public class Synonym {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String relationship;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Taxonomy synonym;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Taxonomy accepted;

    @LastModifiedDate
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime updatedAt;

    @CreatedDate
    @Convert(converter = LocalDateTimeConverter.class)
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
