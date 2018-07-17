package br.gov.sibbr.api.integration.service;

import br.gov.sibbr.api.core.helper.Loggable;
import br.gov.sibbr.api.integration.dao.FaunaDAO;
import br.gov.sibbr.api.integration.dao.TaxonomyDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaxonomyFaunaService implements Loggable {

    @Autowired
    private FaunaDAO faunaDAO;
    @Autowired
    private TaxonomyDAO taxonomyDAO;
    @Autowired
    private TaxonomyService taxonomyService;

    /**
     * Etapa: Primeira carga de taxonomia scientificName, rank e gerar referência com font e identificador (taxonId) de origem
     */
    public void txnScientificNameReference() {
        taxonomyService.txnScientificName(faunaDAO.findTaxonsScientificNameStatus(), 1L);
    }

    /**
     * Etapa: Checar tacons ranks nulos
     */
    public void txnRank() {
        taxonomyService.txnRank(faunaDAO.findTaxonsRanks(), 1L);
    }

    /**
     * Etapa: Obter parent name, consultar respectivo taxonId do parent, obter o respectivo txn desse parent e associar,
     * executar para cada taxon
     */
    public void txnParent() {
        taxonomyService.txnParent(faunaDAO.findTaxonsParent(), 1L);
    }

    /**
     * Etapa: Obter informações sobre autoria dos taxons
     */
    public void txnAuthor() {
        taxonomyService.txnAuthorship(faunaDAO.findTaxonsAuthorship(), 1L);
    }

    /**
     * Etapa: Obter informações sobre sinonimos e aceito para associação dos taxons
     */
    public void txnSynonym() {
        taxonomyService.txnSynonym(faunaDAO.findTaxonSynonym(), 1L);
    }

    /**
     * Etapa: Obter informações sobre vernacularNames dos taxons
     */
    public void txnVernacularName() {
        taxonomyService.txnVernacularName(faunaDAO.findTaxonsVernacularName(), 1L);
    }

    /**
     * Etapa: Obter informações sobre specieProfile dos taxons
     */
    public void txnSpecieProfile() {
        taxonomyService.txnSpecieProfile(faunaDAO.findTaxonsSpecieProfile(), 1L);
    }

    /**
     * Etapa: Obter informações sobre publicações dos taxons
     */
    public void txnPublication() {
        taxonomyService.txnPublication(faunaDAO.findTaxonsPublication(), 1L);
    }

    /**
     * Etapa: Obter informações sobre higherClassification e classification do taxon para geração hierarquica
     */
    public void txnHierarchyTypeClassification() {
        taxonomyService.txnHierarchyTypeClassification(taxonomyDAO.findTxnNotHierarchy("classification", 1L), faunaDAO.findTaxonsHierarchy());
    }

    /**
     * Etapa: Obter informações sobre higherClassification e classification do taxon para geração hierarquica
     */
    public void txnHierarchyTypeHigherClassification() {
        taxonomyService.txnHierarchyTypeHigherClassification(taxonomyDAO.findTxnNotHierarchy("higherClassification", 1L), faunaDAO.findTaxonsHierarchy());
    }

    /**
     * Etapa: Obter informações sobre distribuição dos taxons
     */
    public void txnDistribuition() {
        taxonomyService.txnDistribuition(faunaDAO.findTaxonsDistribuition(), 1L);
    }

}
