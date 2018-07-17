package br.gov.sibbr.api.integration.service;

import br.gov.sibbr.api.core.helper.JsonMapper;
import br.gov.sibbr.api.core.helper.Loggable;
import br.gov.sibbr.api.integration.dao.FloraDAO;
import br.gov.sibbr.api.integration.dao.TaxonomyDAO;
import br.gov.sibbr.api.integration.dto.taxonomy.DistributionDTO;
import br.gov.sibbr.api.integration.dto.taxonomy.OccurrenceMarksDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaxonomyFloraService implements Loggable {

    @Autowired
    private FloraDAO floraDAO;
    @Autowired
    private TaxonomyDAO taxonomyDAO;
    @Autowired
    private TaxonomyService taxonomyService;

    public void txnScientificNameReference() {
        taxonomyService.txnScientificName(floraDAO.findTaxonsScientificNameStatus(), 2L);
    }

    public void txnRank() {
        taxonomyService.txnRank(floraDAO.findTaxonsRanks(), 2L);
    }

    public void txnParent() {
        taxonomyService.txnParent(floraDAO.findTaxonsParent(), 2L);
    }

    public void txnSynonym() {
        taxonomyService.txnSynonym(floraDAO.findTaxonSynonym(), 2L);
    }

    public void txnAuthorShip() {
        taxonomyService.txnAuthorship(floraDAO.findTaxonsAuthorship(), 2L);
    }

    public void txnVernacularName() {
        taxonomyService.txnVernacularName(floraDAO.findTaxonsVernacularName(), 2L);
    }

    public void txnPublication() {
        taxonomyService.txnPublication(floraDAO.findTaxonsPublication(), 2L);
    }

    public void txnDistribuition() {
        List<DistributionDTO> distribuition = floraDAO.findTaxonsDistribuition().parallelStream()
                .filter(e -> e.getOccurrenceRemarks() != null && !e.getOccurrenceRemarks().equals("{}"))
                .peek(TaxonomyFloraService::occurenceMarks)
                .collect(Collectors.toList());

        taxonomyService.txnDistribuition(distribuition, 2L);
    }

    private static void occurenceMarks(DistributionDTO e) {
        OccurrenceMarksDTO om = JsonMapper.toObject(e.getOccurrenceRemarks(), OccurrenceMarksDTO.class);

        Boolean endemicBrazil = null;

        if (om != null) {

            if (om.getEndemism() != null && om.getEndemism().equalsIgnoreCase("Not endemic to Brazil"))
                endemicBrazil = false;
            else if (om.getEndemism() != null && om.getEndemism().equalsIgnoreCase("Endemic to Brazil"))
                endemicBrazil = true;

            e.setEndemicBrazil(endemicBrazil);
            e.setVegetationType(om.getVegetationType() == null ? null : String.join(", ", om.getVegetationType()));
            e.setDomain(om.getPhytogeographicDomain() == null ? null : String.join(", ", om.getPhytogeographicDomain()));
        }
    }

    public void txnSpecieProfile() {
        taxonomyService.txnSpecieProfile(floraDAO.findTaxonsSpecieProfile(), 2L);
    }

    public void txnTypeSpecimen() {
        taxonomyService.txnTypeSpecimen(floraDAO.findTaxonsTypeSpecimen(), 2L);
    }

    public void txnHierarchyTypeClassification() {
        taxonomyService.txnHierarchyTypeClassification(taxonomyDAO.findTxnNotHierarchy("classification", 2L), floraDAO.findTaxonsHierarchy());
    }

    public void txnHierarchyTypeHigherClassification() {
        taxonomyService.txnHierarchyTypeHigherClassification(taxonomyDAO.findTxnNotHierarchy("higherClassification", 2L), floraDAO.findTaxonsHierarchy());
    }

}
