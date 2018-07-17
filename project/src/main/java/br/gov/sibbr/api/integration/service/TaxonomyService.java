package br.gov.sibbr.api.integration.service;

import br.gov.sibbr.api.core.helper.JsonMapper;
import br.gov.sibbr.api.core.helper.Loggable;
import br.gov.sibbr.api.integration.dao.EcologyDAO;
import br.gov.sibbr.api.integration.dao.TaxonomyDAO;
import br.gov.sibbr.api.integration.dto.taxonomy.*;
import br.gov.sibbr.api.integration.entity.ecology.*;
import br.gov.sibbr.api.integration.entity.quality.Inconsistent;
import br.gov.sibbr.api.integration.entity.taxonomy.*;
import br.gov.sibbr.api.integration.repository.taxonomy.*;
import br.gov.sibbr.api.integration.resource.ParserNameGbif;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.util.Iterable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class TaxonomyService implements Loggable {

    //todo: criar consulta de checagem do nome author com nome gerado pelo semantics

    @Autowired
    private TaxonomyDAO taxonomyDAO;
    @Autowired
    private EcologyDAO ecologyDAO;
    @Autowired
    private ParserNameGbif parserNameGbif;
    @Autowired
    private HierarchyService hierarchyService;

    @Autowired
    private TaxonomyRespository taxonomyRespository;
    @Autowired
    private VernacularRepository vernacularRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private DistribuitionRepository distribuitionRepository;
    @Autowired
    private PublicationRepository publicationRepository;
    @Autowired
    private SpecieProfileRepository specieProfileRepository;
    @Autowired
    private SynonymRepository synonymRepository;
    @Autowired
    private SemanticRepository semanticRepository;
    @Autowired
    private TypeSpecimenRepository typeSpecimenRepository;
    @Autowired
    private InconsistentRepository inconsistentRepository;

    public void txnScientificName(List<TaxonomyDTO> taxons, Long originId) {
        taxons.parallelStream().peek(System.out::println).forEach(e -> {

            String rankName = e.getRank().replaceAll("[^A-Za-z0-9]", "").trim();
            String kingdomName = e.getKingdom().replaceAll("[^A-Za-z0-9]", "").trim();

            Rank rank = taxonomyDAO.getRankByNameOrSimilar(rankName);
            Kingdom kingdom = taxonomyDAO.getKingdomByNameOrSimilar(kingdomName);

            Reference reference = Reference.builder()
                    .originalTxn(e.getTaxonId())
                    .origin(Origin.builder().id(originId).build())
                    .build();

            String authorName = StringUtils.isBlank(e.getAuthor()) ? null : e.getAuthor().trim();

            Author author = authorName == null ? null : taxonomyDAO.getAuthor(authorName);
            author = author == null && authorName != null
                    ? authorRepository.save(Author.builder().name(authorName).build())
                    : author;

            Taxonomy txn = Taxonomy.builder()
                    .rank(rank)
                    .kingdom(kingdom)
                    .scientificName(e.getScientificName())
                    .nomeclaturalStatus(StringUtils.lowerCase(e.getNomeclaturalStatus()))
                    .status(e.getStatus())
                    .reference(Collections.singletonList(reference))
                    .author(author)
                    .build();

            taxonomyRespository.save(txn);
        });
    }

    public void txnRank(List<TaxonomyDTO> taxons, Long originId) {
        taxons.parallelStream().peek(System.out::println).forEach(e -> {
            Taxonomy txn = taxonomyDAO.getTxnByOriginalTxn(e.getTaxonId(), originId);
            txn.setRank(taxonomyDAO.getRankByNameOrSimilar(e.getRank()));

            taxonomyRespository.save(txn);
        });
    }

    public void txnParent(List<ParentDTO> parents, Long originId) {
        List<TaxonomyDTO> txns = taxonomyDAO.getTxnsByOriginalId(originId);

        parents.parallelStream().peek(System.out::println).forEach(e -> {
            Taxonomy txn = null;
            Long parent = this.getTxnByOriginalTxn(txns, e.getParentId());

            if (parent != null)
                txn = taxonomyDAO.getTxnByOriginalTxn(e.getTaxonId(), originId);

            if (txn != null) {
                txn.setParentTxn(parent);
                taxonomyRespository.save(txn);
            }
        });
    }

    public void txnAuthorship(List<AuthorDTO> authorDTOS, Long origin) {
        authorDTOS.parallelStream().peek(System.out::println).forEach(e -> {
            Taxonomy txn = taxonomyDAO.getTxnByOriginalTxn(e.getTaxonId(), origin);

            if (txn != null) {
                String authorName = StringUtils.isBlank(e.getAuthirship()) ? null : e.getAuthirship().replace("(", "").trim();

                Author author = authorName == null ? null : taxonomyDAO.getAuthor(authorName);
                author = author == null && authorName != null
                        ? authorRepository.save(Author.builder().name(authorName).build())
                        : author;

                txn.setAuthor(author);
                taxonomyRespository.save(txn);
            }
        });
    }

    public void txnSynonym(List<SynonymDTO> synonyums, Long originId) {
        List<TaxonomyDTO> txns = taxonomyDAO.getTxnsByOriginalId(originId);

        synonyums.parallelStream().peek(System.out::println).forEach(e -> {
            Long syn = this.getTxnByOriginalTxn(txns, e.getTaxonId());
            Long acpt = this.getTxnByOriginalTxn(txns, e.getAcceptedTxn());

            if (syn != null && acpt != null) {
                Synonym synonym = Synonym.builder()
                        .synonym(Taxonomy.builder().txn(syn).build())
                        .accepted(Taxonomy.builder().txn(acpt).build())
                        .relationship(e.getRelationship())
                        .build();

                synonymRepository.save(synonym);
            }
        });
    }

    public void txnVernacularName(List<VernacularDTO> vernaculares, Long originId) {
        List<TaxonomyDTO> txns = taxonomyDAO.getTxnsByOriginalId(originId);

        vernaculares.parallelStream().peek(System.out::println).forEach(e -> {
            Long txn = this.getTxnByOriginalTxn(txns, e.getTaxonId());

            if (txn != null) {
                Vernacular vernacular = Vernacular.builder()
                        .txn(Taxonomy.builder().txn(txn).build())
                        .language(StringUtils.capitalize(StringUtils.lowerCase(e.getLanguage().trim())))
                        .locality(e.getLocality())
                        .name(StringUtils.capitalize(StringUtils.lowerCase(e.getName().trim())))
                        .build();

                vernacularRepository.save(vernacular);
            }
        });
    }

    public void txnTypeSpecimen(List<TypeSpecimenDTO> specimens, Long originId) {
        List<TaxonomyDTO> txns = taxonomyDAO.getTxnsByOriginalId(originId);

        specimens.parallelStream().peek(System.out::println).forEach(e -> {
            Long txn = this.getTxnByOriginalTxn(txns, e.getTaxonId());

            if (txn != null) {
                TypeSpecimen specimen = TypeSpecimen.builder()
                        .catalogNumber(e.getCatalogNumber())
                        .collectionCode(e.getCollectionCode())
                        .locality(e.getLocality())
                        .recordedBy(e.getRecordedBy())
                        .source(e.getSource())
                        .typeStatus(e.getTypeStatus())
                        .txn(Taxonomy.builder().txn(txn).build())
                        .build();

                typeSpecimenRepository.save(specimen);
            }
        });
    }

    public void txnSpecieProfile(List<SpecieProfileDTO> specieProfiles, Long originId) {
        List<TaxonomyDTO> txns = taxonomyDAO.getTxnsByOriginalId(originId);

        specieProfiles.parallelStream().peek(System.out::println).forEach(e -> {
            Long txn = this.getTxnByOriginalTxn(txns, e.getTaxonId());

            if (txn != null) {
                SpeciesProfile profile = SpeciesProfile.builder()
                        .habitat(e.getHabitat())
                        .lifeForm(e.getLifeForm())
                        .txn(Taxonomy.builder().txn(txn).build())
                        .build();

                specieProfileRepository.save(profile);
            }
        });
    }

    public void txnPublication(List<PublicationDTO> publications, Long originId) {
        List<TaxonomyDTO> txns = taxonomyDAO.getTxnsByOriginalId(originId);

        publications.parallelStream().peek(System.out::println).forEach(e -> {
            Long txn = this.getTxnByOriginalTxn(txns, e.getTaxonId());

            if (txn != null) {
                Publication publication = Publication.builder()
                        .bibliographicCitation(e.getBibliographicCitation())
                        .namePublishedIn(e.getNamePublishedIn())
                        .namePublishedInYear(e.getNamePublishedInYear())
                        .reference(e.getReference())
                        .txn(Taxonomy.builder().txn(txn).build())
                        .build();

                publicationRepository.save(publication);
            }
        });
    }

    public void txnDistribuition(List<DistributionDTO> distribuitions, Long originId) {
        List<TaxonomyDTO> txns = taxonomyDAO.getTxnsByOriginalId(originId);

        distribuitions.parallelStream().peek(System.out::println).forEach(e -> {
            Long txn = this.getTxnByOriginalTxn(txns, e.getTaxonId());

            if (txn != null) {
                Distribuition distribution = Distribuition.builder()
                        .countryCode(e.getCountryCode())
                        .environment(e.getEnvironment())
                        .locality(e.getLocality())
                        .domain(e.getDomain())
                        .marineDomain(e.getMarineDomain())
                        .establishmentMeans(e.getEstablishmentMeans())
                        .endemicBrazil(e.getEndemicBrazil())
                        .vegetationType(e.getVegetationType())
                        .txn(Taxonomy.builder().txn(txn).build())
                        .build();

                distribuitionRepository.save(distribution);
            }
        });
    }

    // Semantics

    public void txnSemantics() {
        List<Semantic> semantics = taxonomyDAO.findTxnNotSemantics("synonym");

        semantics.parallelStream().peek(System.out::println).forEach(e -> {
            SemanticDTO lgn = Objects.requireNonNull(JsonMapper.toList(parserNameGbif.getDetailsScientificName(e.getScientificName()), SemanticDTO.class)).get(0);

            if (lgn != null) {
                e.setType(lgn.getType());
                e.setYear(lgn.getYear());
                e.setGenusOrAbove(lgn.getGenusOrAbove());
                e.setSpecificEpithet(lgn.getSpecificEpithet());
                e.setInfraSpecificEpithet(lgn.getInfraSpecificEpithet());
                e.setAuthorship(lgn.getAuthorship());
                e.setSensu(lgn.getSensu());
                e.setCanonicalName(lgn.getCanonicalName());
                e.setCanonicalNameWithMarker(lgn.getCanonicalNameWithMarker());
                e.setCanonicalNameComplete(lgn.getCanonicalNameComplete());
                e.setRankMarker(lgn.getRankMarker());
                e.setBracketAuthorship(lgn.getBracketAuthorship());
                e.setParsed(lgn.getParsed());
                e.setAuthorsParsed(lgn.getAuthorsParsed());

                semanticRepository.save(e);
            }
        });
    }

    // Hierarchy

    public void txnHierarchyTypeClassification(List<HierarchyDTO> txns, List<HierarchyDTO> hierarchys) {
        txns.parallelStream().peek(System.out::println).forEach(e -> {

            HierarchyDTO hierarchyDTO = hierarchys.stream().filter(x -> x.getTaxonId().equals(e.getOriginalTxn())).findFirst().orElse(null);

            String hierarchy = hierarchyDTO == null ? null : hierarchyService.txnHierarchyTypeClassification(hierarchyDTO);
            hierarchyService.txnHierarchyTypeClassification(hierarchyDTO, "classification", hierarchy, e.getTxn(), e.getParentTxn());

        });
    }

    public void txnHierarchyTypeHigherClassification(List<HierarchyDTO> txns, List<HierarchyDTO> hierarchys) {
        txns.parallelStream().peek(System.out::println).forEach(e -> {

            HierarchyDTO hierarchyDTO = hierarchys.stream().filter(x -> x.getTaxonId().equals(e.getOriginalTxn())).findFirst().orElse(null);

            String hierarchy = hierarchyDTO == null ? null : hierarchyDTO.getHigherClassification().replace(";", " | ");
            hierarchyService.txnHierarchyTypeClassification(hierarchyDTO, "higherClassification", hierarchy, e.getTxn(), e.getParentTxn());
        });
    }

    public void txnHierarchyTypeHierarchy() {
        hierarchyService.txnHierarchyTypeHierarchy(taxonomyDAO.findTxnTypeHierarchy(), taxonomyDAO.getHierarchy());
    }

    public void txnHierarchyTypeComplementary() {
        hierarchyService.txnHierarchyComplementary(taxonomyDAO.findHierarchyByHigherTxn());
    }


    public void txnHierarchyFull() {
        hierarchyService.txnHierarchyFull(taxonomyDAO.findHierarchiesComplementarysNotFull());
    }

    // Quality

    public void txnInsconsistenceScientificNameDuplicate() {
        txnInsconsistenceTaxonomy(taxonomyDAO.findTaxonomyDuplicateScientificName(), "scientific name", "Nomes cientificos não devem ser duplicados", "duplicate");
    }

    public void txnInsconsistenceScientificNameNomeclatura() {
        this.txnInsconsistenceTaxonomy(taxonomyDAO.findTaxonomyScientificNameContains("?"), "scientific name", "Scientific name contém caracteres não esperado", "nomeclatural");
    }

    public void txnInsconsistenceRankBlank() {
        this.txnInsconsistenceTaxonomy(taxonomyDAO.findTaxonomyRankIsNull(), "rank", "Rank taxon não devem ser nulos", "nulo");
    }

    public void txnInsconsistenceStatusBlank() {
        this.txnInsconsistenceTaxonomy(taxonomyDAO.findTaxonomyStatusIsNull(), "status", "Status taxon não devem ser nulos", "nulo");
    }

    private void txnInsconsistenceTaxonomy(List<TaxonomyDTO> txns, String fields, String reason, String type) {
        List<Inconsistent> inconsistents = txns.stream().map(e -> Inconsistent.builder()
                .fields(fields)
                .type(type)
                .reason(reason)
                .txn(Taxonomy.builder().txn(e.getTxn()).build())
                .build()).collect(Collectors.toList());

        inconsistentRepository.save(inconsistents);
    }

    // Taxon

    private Long getTxnByOriginalTxn(List<TaxonomyDTO> txns, Long originalTxn) {
        return txns.stream()
                .filter(x -> x.getOriginalTxn().equals(originalTxn))
                .map(TaxonomyDTO::getTxn)
                .findFirst()
                .orElse(null);
    }

    // Ecology

    public void ecologySpecieProfile() {
        List<SpeciesProfile> speciesProfiles = ecologyDAO.getSpecieProfiles();

        speciesProfiles.parallelStream().peek(System.out::println).forEach(e -> {
            if (e.getHabitat() != null) {
                String namesHabitats = this.collection(e.getHabitat(), "|");
                Set<Habitat> habitats = ecologyDAO.getHabitats(namesHabitats);
                if (!habitats.isEmpty()) e.setHabitats(habitats);
            }

            if (e.getLifeForm() != null) {
                String namesLifeForms = this.collection(e.getLifeForm(), "|");
                Set<LifeForm> lifeForms = ecologyDAO.getLifeForms(namesLifeForms);
                if (!lifeForms.isEmpty()) e.setLifeForms(lifeForms);
            }

            specieProfileRepository.save(e);
        });
    }

    public void ecologyDistribuition() {
        List<Distribuition> distribuitions = ecologyDAO.getDistribuitions();

        distribuitions.parallelStream().peek(System.out::println).forEach(e -> {

            if (e.getDomain() != null) {
                String biomeNames = this.collection(e.getDomain(), ",");
                Set<Biome> biomes = ecologyDAO.getBiomes(biomeNames);
                if (!biomes.isEmpty()) e.setBiomes(biomes);
            }

            if (e.getMarineDomain() != null) {
                String marineNames = this.collection(e.getMarineDomain(), ",");
                Set<BiomeMarine> biomesMarines = ecologyDAO.getBiomeMarines(marineNames);
                if (!biomesMarines.isEmpty()) e.setBiomeMarines(biomesMarines);
            }

            if (e.getVegetationType() != null) {
                String vegetationNames = this.collection(e.getVegetationType(), ",");
                Set<VegetationType> vegetationTypes = ecologyDAO.getVegetationTypes(vegetationNames);
                if (!vegetationTypes.isEmpty()) e.setVegetationTypes(vegetationTypes);
            }

            distribuitionRepository.save(e);
        });
    }

    private String collection(String name, String delimiter) {
        final List<String> nm = Arrays.stream(name.split(delimiter)).map(e -> "'" + e.trim().toUpperCase() + "'").collect(Collectors.toList());
        return StringUtils.join(nm, ",");
    }

}
