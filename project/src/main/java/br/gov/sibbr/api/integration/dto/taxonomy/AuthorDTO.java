package br.gov.sibbr.api.integration.dto.taxonomy;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorDTO {

    private Long id;
    private Long taxonId;
    private String authirship;

}
