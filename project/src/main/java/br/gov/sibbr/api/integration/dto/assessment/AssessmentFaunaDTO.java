package br.gov.sibbr.api.integration.dto.assessment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssessmentFaunaDTO {

    private Long id;
    private String scientificName;
    private String vernacularName;
    private String category;
    private String criteria;
    private String source;
    private Integer year;
    private Long officialId;

}
