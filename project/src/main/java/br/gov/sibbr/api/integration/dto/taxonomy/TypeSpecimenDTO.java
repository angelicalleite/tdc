package br.gov.sibbr.api.integration.dto.taxonomy;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TypeSpecimenDTO {

    private String catalogNumber;
    private String collectionCode;
    private String locality;
    private String recordedBy;
    private String source;
    private String typeStatus;
    private Long taxonId;

}
