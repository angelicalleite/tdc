package br.gov.sibbr.api.integration.entity.taxonomy;

import br.gov.sibbr.api.integration.converter.LocalDateTimeConverter;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(schema = "taxonomy", name = "publication")
public class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Informações contida somente reflora
    private String namePublishedIn;
    private String namePublishedInYear;

    @Lob
    @Type(type = "text")
    private String reference;

    @Lob
    @Type(type = "text")
    private String bibliographicCitation;

    @OneToOne
    @JoinColumn(name = "txn")
    private Taxonomy txn;

    @LastModifiedDate
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime updatedAt;

    @CreatedDate
    @Convert(converter = LocalDateTimeConverter.class)
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
