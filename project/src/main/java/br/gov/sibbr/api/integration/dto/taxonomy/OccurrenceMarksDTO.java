package br.gov.sibbr.api.integration.dto.taxonomy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OccurrenceMarksDTO {

    // Dados OccurrenceMarks distribuição reflora
    private String endemism;
    private List<String> phytogeographicDomain;
    private List<String> vegetationType;

}
