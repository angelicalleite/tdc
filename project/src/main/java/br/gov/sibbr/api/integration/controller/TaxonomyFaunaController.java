package br.gov.sibbr.api.integration.controller;

import br.gov.sibbr.api.core.http.RestResponse;
import br.gov.sibbr.api.integration.service.TaxonomyFaunaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fauna")
public class TaxonomyFaunaController implements RestResponse {

    @Autowired
    private TaxonomyFaunaService taxonomyFaunaService;

    @GetMapping("/txn")
    public ResponseEntity<?> upScientificNameReference() {
        taxonomyFaunaService.txnScientificNameReference();
        return ok("Up Taxon ScientificName and Reference");
    }

    @GetMapping("/rank")
    public ResponseEntity<?> upRank() {
        taxonomyFaunaService.txnRank();
        return ok("Up Taxon Rank");
    }

    @GetMapping("/parent")
    public ResponseEntity<?> upParent() {
        taxonomyFaunaService.txnParent();
        return ok("Up Taxon Parent");
    }

    @GetMapping("/author")
    public ResponseEntity<?> upAuthor() {
        taxonomyFaunaService.txnAuthor();
        return ok("Up Taxon Author");
    }

    @GetMapping("/synonym")
    public ResponseEntity<?> upSynonym() {
        taxonomyFaunaService.txnSynonym();
        return ok("Up Taxon Synonym");
    }

    @GetMapping("/vernacular")
    public ResponseEntity<?> upVernacularName() {
        taxonomyFaunaService.txnVernacularName();
        return ok("Up Taxon VernacularName!");
    }

    @GetMapping("/profile")
    public ResponseEntity<?> upSpecieProfile() {
        taxonomyFaunaService.txnSpecieProfile();
        return ok("Up Taxon Specie Profile");
    }

    @GetMapping("/publication")
    public ResponseEntity<?> upPublication() {
        taxonomyFaunaService.txnPublication();
        return ok("Up Taxon Publication");
    }

    @GetMapping("/distribution")
    public ResponseEntity<?> upDistribuition() {
        taxonomyFaunaService.txnDistribuition();
        return ok("Up Taxon Distribuition");
    }

    @GetMapping("/hierarchy/type/classification")
    public ResponseEntity<?> upHierarchyTypeClassification() {
        taxonomyFaunaService.txnHierarchyTypeClassification();
        return ok("Up Taxon Hierarchy by Classification");
    }

    @GetMapping("/hierarchy/type/higherclassification")
    public ResponseEntity<?> upHierarchyTypeHigherClassification() {
        taxonomyFaunaService.txnHierarchyTypeHigherClassification();
        return ok("Up Taxon Hierarchy by Classification");
    }

    @GetMapping("/assessments")
    public ResponseEntity<?> upAssetents() {
        taxonomyFaunaService.txnHierarchyTypeHigherClassification();
        return ok("Up Taxon Hierarchy by Classification");
    }

}
