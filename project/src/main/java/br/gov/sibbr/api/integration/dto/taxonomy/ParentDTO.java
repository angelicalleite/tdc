package br.gov.sibbr.api.integration.dto.taxonomy;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParentDTO {

    private Long taxonId;
    private Long parentId;



}
