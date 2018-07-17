package br.gov.sibbr.api.integration.entity.taxonomy;

import br.gov.sibbr.api.integration.converter.LocalDateTimeConverter;
import br.gov.sibbr.api.integration.entity.ecology.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(schema = "taxonomy", name = "distribuition")
public class Distribuition {

    //todo: estrategia normalização vegetationType, phytogeographicDomain, epicotinentalDomain
    // analise de distribuição para contexto especificos de flora e fauna

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String locality;
    private String environment;
    private String establishmentMeans;

    private Boolean endemicBrazil;

    @Lob
    @Type(type = "text")
    private String countryCode;

    @Lob
    @Type(type = "text")
    private String vegetationType;

    @Lob
    @Type(type = "text")
    private String domain; //fauna:epicotinentalDomain flora:phytogeographicDomain

    @Lob
    @Type(type = "text")
    private String marineDomain;

    @ManyToMany
    @JoinTable(name = "ecology_biome", schema = "taxonomy",
            joinColumns = {@JoinColumn(name = "distribuition_id")},
            inverseJoinColumns = {@JoinColumn(name = "biome_id")})
    private Set<Biome> biomes;

    @ManyToMany
    @JoinTable(name = "ecology_biomemarine", schema = "taxonomy",
            joinColumns = {@JoinColumn(name = "distribuition_id")},
            inverseJoinColumns = {@JoinColumn(name = "biomemarine_id")})
    private Set<BiomeMarine> biomeMarines;

    @ManyToMany
    @JoinTable(name = "ecology_vegetationtype", schema = "taxonomy",
            joinColumns = {@JoinColumn(name = "distribuition_id")},
            inverseJoinColumns = {@JoinColumn(name = "vegetationtype_id")})
    private Set<VegetationType> vegetationTypes;

    @ManyToOne
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