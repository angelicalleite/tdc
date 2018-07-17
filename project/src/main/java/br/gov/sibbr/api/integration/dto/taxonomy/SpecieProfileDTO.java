package br.gov.sibbr.api.integration.dto.taxonomy;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SpecieProfileDTO {

    private Long taxonId;
    private String habitat;
    private String lifeForm;

}
