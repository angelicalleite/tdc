package br.gov.sibbr.api.integration.dto.assessment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssessmentRegionDTO {

    private Long id;
    private String scientificName;
    private String description;
    private String localy;
    private Integer year;
    private String grupo;
    private String category;
    private String criteria;
    private Long officialId;

    private String code;
    private String official;

}
