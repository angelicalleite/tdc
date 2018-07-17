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
@Table(schema = "taxonomy", name = "rank")
public class Rank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    private String similarName;

    @Column(nullable = false)
    private Long directParent;

    @Column(nullable = false)
    private Long requeridParent;

    @LastModifiedDate
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime updatedAt;

    @CreatedDate
    @Convert(converter = LocalDateTimeConverter.class)
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
