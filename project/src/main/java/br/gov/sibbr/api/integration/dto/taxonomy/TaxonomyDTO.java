package br.gov.sibbr.api.integration.dto.taxonomy;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TaxonomyDTO {

    private Long txn;
    private Long taxonId;

    private Long parentTxn;
    private Long originalTxn;

    private String scientificName;
    private String parentName;
    private String author;

    private String status;
    private String nomeclaturalStatus;

    private String rank;
    private String kingdom;

    private LocalDateTime modifedDate;

}
