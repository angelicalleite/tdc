package br.gov.sibbr.api.integration.dto.taxonomy;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VernacularDTO {

    private Long taxonId;
    private String name;
    private String locality;
    private String language;

}
