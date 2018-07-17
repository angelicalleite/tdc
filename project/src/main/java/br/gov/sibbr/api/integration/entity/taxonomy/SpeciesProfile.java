package br.gov.sibbr.api.integration.entity.taxonomy;

import br.gov.sibbr.api.integration.converter.LocalDateTimeConverter;
import br.gov.sibbr.api.integration.entity.ecology.Habitat;
import br.gov.sibbr.api.integration.entity.ecology.LifeForm;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(schema = "taxonomy", name = "specie_profiles")
public class SpeciesProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String habitat;
    private String lifeForm;

    @ManyToMany
    @JoinTable(name = "ecology_habitat", schema = "taxonomy",
            joinColumns = {@JoinColumn(name = "specie_id")},
            inverseJoinColumns = {@JoinColumn(name = "habitat_id")})
    private Set<Habitat> habitats;

    @ManyToMany
    @JoinTable(name = "ecology_lifeform", schema = "taxonomy",
            joinColumns = {@JoinColumn(name = "specie_id")},
            inverseJoinColumns = {@JoinColumn(name = "lifeform_id")})
    private Set<LifeForm> lifeForms;

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
