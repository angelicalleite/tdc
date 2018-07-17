package br.gov.sibbr.api.integration.entity.quality;

import br.gov.sibbr.api.integration.converter.LocalDateTimeConverter;
import br.gov.sibbr.api.integration.entity.taxonomy.Taxonomy;
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
@Table(schema = "quality", name = "inconsistent")
public class Inconsistent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reason;
    private String fields;
    private String type;

    @ManyToOne
    @JoinColumn(name = "txn", nullable = false)
    private Taxonomy txn;

    @LastModifiedDate
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime updatedAt;

    @CreatedDate
    @Convert(converter = LocalDateTimeConverter.class)
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
