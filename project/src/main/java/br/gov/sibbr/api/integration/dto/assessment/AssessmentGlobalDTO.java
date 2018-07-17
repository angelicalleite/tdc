package br.gov.sibbr.api.integration.dto.assessment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssessmentGlobalDTO {

    private Long taxonId;
    private String code;
    private String category;
    private String scientificName;
    private Integer year;

}
