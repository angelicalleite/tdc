package br.gov.sibbr.api.integration.entity.assessment;

import br.gov.sibbr.api.integration.converter.LocalDateTimeConverter;
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

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(schema = "assessment", name = "economicValue")
public class EconomicValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String potentialEconomicValue;

    @Lob
    @Type(type = "text")
    private String details;

    @ManyToOne
    @JoinColumn(name = "assessment_id", nullable = false)
    private Assessment assessment;

    @LastModifiedDate
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime updatedAt;

    @CreatedDate
    @Convert(converter = LocalDateTimeConverter.class)
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
