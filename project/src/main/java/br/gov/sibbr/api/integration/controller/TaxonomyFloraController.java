package br.gov.sibbr.api.integration.controller;

import br.gov.sibbr.api.core.http.RestResponse;
import br.gov.sibbr.api.integration.service.TaxonomyFloraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flora")
public class TaxonomyFloraController implements RestResponse {

    @Autowired
    private TaxonomyFloraService taxonomyFloraService;

    @GetMapping("/txn")
    public ResponseEntity<?> upScientificNameReference() {
        taxonomyFloraService.txnScientificNameReference();
        return ok("Up Taxon ScientificName and Reference");
    }

    @GetMapping("/rank")
    public ResponseEntity<?> upRank() {
        taxonomyFloraService.txnRank();
        return ok("Up Taxon Rank");
    }

    @GetMapping("/parent")
    public ResponseEntity<?> upParent() {
        taxonomyFloraService.txnParent();
        return ok("Up Taxon Parent");
    }

    @GetMapping("/author")
    public ResponseEntity<?> upAuthorShip() {
        taxonomyFloraService.txnAuthorShip();
        return ok("Up Taxon Author");
    }

    @GetMapping("/synonym")
    public ResponseEntity<?> upSynonym() {
        taxonomyFloraService.txnSynonym();
        return ok("Up Taxon Synonym");
    }

    @GetMapping("/vernacular")
    public ResponseEntity<?> upVernacular() {
        taxonomyFloraService.txnVernacularName();
        return ok("Up Taxon Vernacular Name");
    }

    @GetMapping("/distribution")
    public ResponseEntity<?> upDistrbuition() {
        taxonomyFloraService.txnDistribuition();
        return ok("Up Taxon Distribuition");
    }

    @GetMapping("/publication")
    public ResponseEntity<?> upPublication() {
        taxonomyFloraService.txnPublication();
        return ok("Up Taxon Publication");
    }

    @GetMapping("/profile")
    public ResponseEntity<?> upSpecieProfile() {
        taxonomyFloraService.txnSpecieProfile();
        return ok("Up Taxon SpecieProfile");
    }

    @GetMapping("/specimen")
    public ResponseEntity<?> upTypeSpecimen() {
        taxonomyFloraService.txnTypeSpecimen();
        return ok("Up Taxon TypeSpecimen");
    }

    @GetMapping("/hierarchy/type/classification")
    public ResponseEntity<?> upHierarchyTypeClassification() {
        taxonomyFloraService.txnHierarchyTypeClassification();
        return ok("Up Taxon Hierarchy by Classification");
    }

    @GetMapping("/hierarchy/type/higherclassification")
    public ResponseEntity<?> upHierarchyTypeHigherClassification() {
        taxonomyFloraService.txnHierarchyTypeHigherClassification();
        return ok("Up Taxon Hierarchy by Classification");
    }

}
