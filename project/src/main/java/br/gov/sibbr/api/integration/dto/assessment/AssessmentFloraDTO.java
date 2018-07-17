package br.gov.sibbr.api.integration.dto.assessment;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AssessmentFloraDTO {

    private Long taxonId;
    private Long assessmentId;
    private Long metadatoId;
    private Long profileId;
    private String scientificName;

    private String category;
    private String criteria;

    private String evaluator;
    private String assessor;

    private String rationale;
    private String notes;

    private Integer year;
    private LocalDateTime dateOfAssessment;

}
