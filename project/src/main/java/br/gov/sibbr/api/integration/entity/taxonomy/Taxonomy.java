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
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(schema = "taxonomy", name = "taxonomy")
public class Taxonomy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long txn;
    private Long parentTxn;
    private String scientificName;
    private String status;
    private String nomeclaturalStatus;

    @ManyToOne
    private Group group;

    @ManyToOne
    private Rank rank;

    @ManyToOne
    private Kingdom kingdom;

    @ManyToOne
    private Author author;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private List<Reference> reference;

    @LastModifiedDate
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime updatedAt;

    @CreatedDate
    @Convert(converter = LocalDateTimeConverter.class)
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
