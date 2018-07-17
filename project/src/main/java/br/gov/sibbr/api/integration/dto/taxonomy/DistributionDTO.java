package br.gov.sibbr.api.integration.dto.taxonomy;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DistributionDTO {

    private Long taxonId;

    private String countryCode;
    private String establishmentMeans;
    private String locality;

    private String environment;
    private String marineDomain;
    private String domain;

    private Boolean endemicBrazil;
    private String occurrenceRemarks;

    private String vegetationType;
}
