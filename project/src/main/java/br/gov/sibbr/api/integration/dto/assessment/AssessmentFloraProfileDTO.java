package br.gov.sibbr.api.integration.dto.assessment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssessmentFloraProfileDTO {

    private String contributor;
    private String language;
    private String title;
    private String potentialEconomicValue;

}
