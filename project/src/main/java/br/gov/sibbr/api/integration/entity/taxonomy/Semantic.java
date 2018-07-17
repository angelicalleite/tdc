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
@Table(schema = "taxonomy", name = "semantic")
public class Semantic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String scientificName;
    private String type;
    private String year;
    private String genusOrAbove;
    private String specificEpithet;
    private String infraSpecificEpithet;
    private String authorship;
    private String sensu;
    private String canonicalName;
    private String canonicalNameWithMarker;
    private String canonicalNameComplete;
    private String rankMarker;
    private String bracketAuthorship;

    private Boolean parsed;
    private Boolean authorsParsed;

    private String status;

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
