package br.gov.sibbr.api.integration.dto.taxonomy;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SynonymDTO {

    private Long taxonId;
    private Long acceptedTxn;
    private String relationship;

}
