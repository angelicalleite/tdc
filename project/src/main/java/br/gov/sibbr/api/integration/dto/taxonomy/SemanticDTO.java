package br.gov.sibbr.api.integration.dto.taxonomy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SemanticDTO {

    private String scientificName;
    private String type;
    private String year;
    private String genusOrAbove;
    private String specificEpithet;
    private String authorship;
    private String sensu;
    private Boolean parsed;
    private Boolean authorsParsed;
    private String canonicalName;
    private String canonicalNameWithMarker;
    private String canonicalNameComplete;
    private String rankMarker;
    private String infraSpecificEpithet;
    private String bracketAuthorship;

    private String nomeclaturalStatus;
    private String status;
    private Long txn;
}
