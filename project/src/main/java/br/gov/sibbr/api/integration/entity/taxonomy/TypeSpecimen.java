package br.gov.sibbr.api.integration.entity.taxonomy;

import br.gov.sibbr.api.integration.converter.LocalDateTimeConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(schema = "taxonomy", name = "type_specimen")
public class TypeSpecimen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String catalogNumber;
    private String collectionCode;
    private String locality;
    private String recordedBy;
    private String source;
    private String typeStatus;

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
