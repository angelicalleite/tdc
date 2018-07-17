package br.gov.sibbr.api.integration.dto.taxonomy;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EndemismDTO {

    private Long taxonId;
    private String inBrazil;

}
