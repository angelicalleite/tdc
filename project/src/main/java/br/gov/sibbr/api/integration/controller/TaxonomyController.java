package br.gov.sibbr.api.integration.controller;

import br.gov.sibbr.api.core.http.RestResponse;
import br.gov.sibbr.api.integration.service.TaxonomyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/txn")
public class TaxonomyController implements RestResponse {

    @Autowired
    private TaxonomyService taxonomyService;

    @GetMapping("semantic")
    public ResponseEntity<?> upSemantics() {
        taxonomyService.txnSemantics();
        return ok("Hierarchy generated");
    }

    @GetMapping("hierarchy/hierarchy")
    public ResponseEntity<?> upHierarchyTypeHierarchy() {
        taxonomyService.txnHierarchyTypeHierarchy();
        return ok("Hierarchy generated");
    }

    //caso hierarquia complementar não for genrada para taxon, higherTxn não encontrado no col, consultar hierarchy comun
    @GetMapping("hierarchy/complementary")
    public ResponseEntity<?> upHierarchyTypeComplementary() {
        taxonomyService.txnHierarchyTypeComplementary();
        return ok("Hierarchy complemenntary generated sucess");
    }

    @GetMapping("hierarchy/full")
    public ResponseEntity<?> upHierarchyTypeFull() {
        taxonomyService.txnHierarchyFull();
        return ok("Hierarchy Full generated with success");
    }

    @GetMapping("inconsistentes/name/nomeclatura")
    public ResponseEntity<?> upNameInconsistence() {
        taxonomyService.txnInsconsistenceScientificNameNomeclatura();
        return ok("Hierarchy generated");
    }

    @GetMapping("inconsistentes/name/duplicate")
    public ResponseEntity<?> upNameDuplicate() {
        taxonomyService.txnInsconsistenceScientificNameDuplicate();
        return ok("Hierarchy generated");
    }

    @GetMapping("inconsistentes/rank/blank")
    public ResponseEntity<?> upRankEmpty() {
        taxonomyService.txnInsconsistenceRankBlank();
        return ok("Hierarchy generated");
    }

    @GetMapping("inconsistentes/status/blank")
    public ResponseEntity<?> upStatusEmpty() {
        taxonomyService.txnInsconsistenceStatusBlank();
        return ok("Hierarchy generated");
    }

    @GetMapping("ecology/profile")
    public ResponseEntity<?> upEcologySpecieProfile() {
        taxonomyService.ecologySpecieProfile();
        return ok("Ecology specie profile");
    }

    @GetMapping("ecology/distribuition")
    public ResponseEntity<?> upEcologyDistribuition() {
        taxonomyService.ecologyDistribuition();
        return ok("Ecology distribuition");
    }

}
