package br.gov.sibbr.api.integration.dto.taxonomy;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PublicationDTO {

    private Long taxonId;

    private String reference;
    private String bibliographicCitation;
    private String namePublishedIn;
    private String namePublishedInYear;

    private String type;
    private String date;
    private String title;
    private String creator;
    private String identifier;

}
