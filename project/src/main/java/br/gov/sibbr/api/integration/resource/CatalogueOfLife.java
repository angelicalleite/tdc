package br.gov.sibbr.api.integration.resource;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "catalogueOfLife", url = "http://webservice.catalogueoflife.org/col")
public interface CatalogueOfLife {

    @GetMapping("/webservice")
    String searchByName(@RequestParam("name") String name,
                        @RequestParam(value = "rank", required = false) String rank,
                        @RequestParam(value = "format", defaultValue = "json") String format,
                        @RequestParam(value = "response", defaultValue = "full") String response);
}
