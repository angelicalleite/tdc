package br.gov.sibbr.api.integration.controller;

import br.gov.sibbr.api.core.http.RestResponse;
import br.gov.sibbr.api.integration.service.AssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/assessments")
public class AssessmentController implements RestResponse {

    @Autowired
    private AssessmentService assessmentService;

    @GetMapping("txns")
    public ResponseEntity<?> upTxn() {
        assessmentService.asmtTxns();
        return ok("Assessment Txns UP");
    }

    @GetMapping("duplicates")
    public ResponseEntity<?> upNormalizeDuplicates() {
        assessmentService.asmtNormalizateDuplicate();
        return ok("Assessment normalize duplicates UP");
    }

    @GetMapping("fauna")
    public ResponseEntity<?> upFauna() {
        assessmentService.asmtFauna();
        return ok("Assessment Fauna ICMBio UP");
    }

    @GetMapping("fauna/nt")
    public ResponseEntity<?> upFaunaNT() {
        assessmentService.asmtFaunaNT();
        return ok("Assessment Fauna ICMBio UP NT");
    }

    @GetMapping("fauna/dd")
    public ResponseEntity<?> upFaunaDD() {
        assessmentService.asmtFaunaDD();
        return ok("Assessment Fauna ICMBio UP DD");
    }

    @GetMapping("global")
    public ResponseEntity<?> upGlobal() {
        assessmentService.asmtGlobal();
        return ok("Assessment IUCN UP");
    }

    @GetMapping("flora")
    public ResponseEntity<?> upFlora() {
        assessmentService.asmtFlora();
        return ok("Assessment Flora UP");
    }

    @GetMapping("regional")
    public ResponseEntity<?> upRegional() {
        assessmentService.asmtRegional();
        return ok("Assessment Fauna Regional UP");
    }
}
