package br.gov.sibbr.api.integration.resource;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * GBIF API ParserName disponibiliza recurso para detlhar informações sobre scietificName
 * <p>
 * Docs: Descrição dados http://tools.gbif.org/nameparser/api.do
 * Docs: Acesso API https://www.gbif.org/developer/species#parser
 * Docs: Blog http://gbif.blogspot.com.br/2017/06/gbif-name-parser.html
 */
@FeignClient(name = "parseName", url = "http://api.gbif.org/v1/parser")
public interface ParserNameGbif {

    @GetMapping("/name")
    String getDetailsScientificName(@RequestParam("name") String scientificName);

    @GetMapping("/name")
    String getDetailsScientificName(@RequestParam("name") List<String> scientificNames);
}
