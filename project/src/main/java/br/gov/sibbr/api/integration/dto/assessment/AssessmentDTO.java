package br.gov.sibbr.api.integration.dto.assessment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssessmentDTO {

    private Long id;
    private Long txn;
    private String scientificName;
    private String canonicalName;

}
