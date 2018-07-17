package br.gov.sibbr.api.integration.service;

import br.gov.sibbr.api.core.helper.JsonMapper;
import br.gov.sibbr.api.integration.dao.AssessmentDAO;
import br.gov.sibbr.api.integration.dao.TaxonomyDAO;
import br.gov.sibbr.api.integration.dto.assessment.*;
import br.gov.sibbr.api.integration.dto.taxonomy.SemanticDTO;
import br.gov.sibbr.api.integration.entity.assessment.*;
import br.gov.sibbr.api.integration.entity.taxonomy.Taxonomy;
import br.gov.sibbr.api.integration.repository.assessment.*;
import br.gov.sibbr.api.integration.resource.ParserNameGbif;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AssessmentService {

    @Autowired
    private TaxonomyDAO taxonomyDAO;
    @Autowired
    private AssessmentDAO assessmentDAO;

    @Autowired
    private ParserNameGbif parserNameGbif;

    @Autowired
    private ReferenceRepository referenceRepository;
    @Autowired
    private OfficialRepository officialRepository;
    @Autowired
    private PublicationRepository publicationRepository;
    @Autowired
    private ContribuitionRepository contribuitionRepository;
    @Autowired
    private AssessmentRepository assessmentRepository;
    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private DistribuitionRepository distribuitionRepository;
    @Autowired
    private VernacularRepository vernacularRepository;
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private EconomicValueRepository economicValueRepository;
    @Autowired
    private ActionRepository actionRepository;
    @Autowired
    private EcologyRepository ecologyRepository;

    /**
     * Normalizar assessments com scientificName ou canonicalName duplicado
     */
    public void asmtNormalizateDuplicate() {
        List<String> canonicalDuplicate = assessmentDAO.findAssessmentsDuplicateDistinct();
        List<AssessmentDTO> assessmentDTO = assessmentDAO.findAssessmentsDuplicate();

        canonicalDuplicate.stream().peek(System.out::println).forEach(e -> {

            List<AssessmentDTO> assessments = assessmentDTO.stream()
                    .filter(x -> x.getCanonicalName().trim().equalsIgnoreCase(e.trim()))
                    .filter(t -> t.getCanonicalName().equals("accept"))
                    .collect(Collectors.toList());

            Long assessmentMaxId = assessments.size() == 1 ? assessments.get(0).getId()
                    : assessmentDTO.stream().filter(x -> x.getCanonicalName().trim().equalsIgnoreCase(e.trim()))
                    .max(Comparator.comparing(x -> x.getScientificName().length()))
                    .get()
                    .getId();

            assessmentDTO.stream()
                    .filter(y -> y.getCanonicalName().trim().equalsIgnoreCase(e.trim()))
                    .forEach(y -> historyRepository.updateAssessmentId(assessmentMaxId, y.getId()));
        });
    }

    /**
     * Obter txns associado a especie ameaçada apartir do scientificName ou canonicalName
     */
    public void asmtTxns() {
        assessmentRepository.findByTaxonomyIsNull().parallelStream().peek(System.out::println).forEach(e -> {
            e.setTaxonomy(taxonomyDAO.getTxnBySemantics(e.getScientificName(), e.getCanonicalName()));

            assessmentRepository.save(e);
        });
    }

    /**
     * Processar ameaças global fonte IUCN
     */
    public void asmtGlobal() {
        try {
            assessmentDAO.findAssessmentsGlobal().parallelStream().peek(System.out::println).forEach(e -> {
                Assessment assessment = this.getAssessment(e.getScientificName().trim());

                History history = this.getHistory(e.getCode(), null, e.getCode() == null ? e.getCategory() : null, e.getYear(), null, assessment);

                if (history != null)
                    this.getReference(e.getTaxonId(), "global", history.getId(), 6L, null);

            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Processar ameaças regionais por estados, planilhas digitadores
     */
    public void asmtRegional() {
        List<AssessmentRegionDTO> assessmentRegionDTOS = assessmentDAO.findAssessmentsRegional();

        try {
            assessmentRegionDTOS.parallelStream().peek(System.out::println).forEach(e -> {
                Assessment assessment = this.getAssessment(e.getScientificName().trim());

                History history = getHistory(e.getCategory(), e.getCriteria(), e.getGrupo(), e.getYear(), null, assessment);

                this.getDistribuition(history, e.getLocaly(), e.getCategory());

                Official official = this.getOfficial(e.getCode(), e.getOfficial(), e.getLocaly(), e.getYear());

                this.getReference(null, "regional", history.getId(), 7L, official.getId());
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Processar ameaças regionais por estados, planilhas digitadores
     */
    public void asmtFlora() {
        List<AssessmentFloraDTO> assessmentFloraDTO = assessmentDAO.findAssessmentsFlora();

        try {
            assessmentFloraDTO.stream().peek(System.out::println).forEach(e -> {
                Assessment assessment = this.getAssessment(e.getScientificName().trim());

                History history = getHistory(e.getCategory(), e.getCriteria(), null, 2014, e.getDateOfAssessment(), assessment);

                if (history != null) {
                    this.getReference(e.getTaxonId(), "nacional", history.getId(), 4L, 31L);
                    this.getContribuition(e.getAssessor(), e.getEvaluator(), null, history.getId());
                    this.getNote(history, e.getNotes(), e.getRationale(), null);
                }

                if (e.getProfileId() != null) {
                    EconomicValue ec = assessmentDAO.findAssessmentsFloraEconomicValue(e.getProfileId(), assessment);
                    if (ec != null) economicValueRepository.save(ec);

                    List<Action> actions = assessmentDAO.findAssessmentsFloraAction(e.getProfileId(), history);
                    if (actions != null) actionRepository.save(actions);

                    Ecology ecology = assessmentDAO.findAssessmentsFloraEcology(e.getProfileId(), assessment);
                    if (ecology != null) ecologyRepository.save(ecology);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Processar ameaças ICMBIO 2014
     */
    public void asmtFauna() {
        this.asmtFauna(assessmentDAO.findAssessmentsFauna(), 1L);
    }

    /**
     * Processar ameaças ICMBIO 2014 categorias NT
     */
    public void asmtFaunaNT() {
        this.asmtFauna(assessmentDAO.findAssessmentsFaunaNT(), 2L);
    }

    /**
     * Processar ameaças ICMBIO 2014 categorias DD
     */
    public void asmtFaunaDD() {
        this.asmtFauna(assessmentDAO.findAssessmentsFaunaDD(), 3L);
    }

    /**
     * Gerar e salvar novo assessments com seu respectivo histoicom metadados e referencia
     */
    private void asmtFauna(List<AssessmentFaunaDTO> assessmentFaunaDTO, Long originId) {
        try {
            assessmentFaunaDTO.parallelStream().peek(System.out::println).forEach(e -> {
                Assessment assessment = this.getAssessment(e.getScientificName().trim());

                History history = this.getHistory(e.getCategory(), e.getCriteria(), null, e.getYear(), null, assessment);

                if (e.getSource() != null && history != null)
                    this.getPublication(null, e.getSource(), null, history.getId());

                if (e.getVernacularName() != null)
                    this.getVernacular("", "", e.getVernacularName(), assessment.getId());

                if (history != null)
                    this.getReference(null, "nacional", history.getId(), originId, e.getOfficialId());
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Criar e retornar assessment baseado no scientificName e consultar respectivo ]
     * taxon para associação com especie ameaçada
     */
    private Assessment getAssessment(String scientificName) {
        List<Assessment> assessment = assessmentDAO.findAssessmentByName(scientificName.trim());

        if (assessment == null || CollectionUtils.isEmpty(assessment)) {
            String canonical = Objects.requireNonNull(JsonMapper.toList(parserNameGbif.getDetailsScientificName(scientificName), SemanticDTO.class)).get(0).getCanonicalName();

            List<Assessment> asmtCanonical = assessmentDAO.findAssessmentByName(canonical.trim());
            if (asmtCanonical != null && !CollectionUtils.isEmpty(asmtCanonical)) return asmtCanonical.get(0);

            Taxonomy txn = taxonomyDAO.getTxnBySemantics(scientificName, canonical);

            Assessment asmt = Assessment.builder()
                    .scientificName(scientificName.replace("  ", " ").trim())
                    .canonicalName(canonical)
                    .taxonomy(txn)
                    .build();

            return assessmentRepository.save(asmt);
        }

        return assessment.get(0);
    }

    /**
     * Criar e retornar official documento apartir dos parametros informados
     */
    private Official getOfficial(String code, String name, String localy, Integer year) {
        Official official = assessmentDAO.findOfficialByCode(code.trim());

        return official != null ? official :
                officialRepository.save(Official.builder()
                        .code(code.toUpperCase())
                        .locality(localy)
                        .name(name)
                        .year(year)
                        .build());
    }

    /**
     * Criar e retornar history apartir dos parametros informados
     */
    private History getHistory(String category, String criteria, String description, Integer year, LocalDateTime dateOfAssessment, Assessment assessment) {
        return historyRepository.save(History.builder()
                .category(category)
                .criteria(criteria)
                .description(description)
                .year(year)
                .assessment(assessment)
                .dateOfAssessment(dateOfAssessment)
                .build());
    }

    /**
     * Criar e salvar reference apartir dos parametros informados
     */
    private void getReference(Long originalId, String typeReference, Long history, Long origin, Long official) {
        referenceRepository.save(Reference.builder()
                .originalId(originalId)
                .typeReference(typeReference)
                .history(History.builder().id(history).build())
                .origin(Origin.builder().id(origin).build())
                .official(official == null ? null : Official.builder().id(official).build())
                .build());
    }

    /**
     * Criar contribuition a partir dos parametros informados
     */
    private void getContribuition(String assessor, String evaluation, String outher, Long history) {
        contribuitionRepository.save(Contribuition.builder()
                .assesor(assessor)
                .evaluation(evaluation)
                .outher(outher)
                .history(History.builder().id(history).build())
                .build());
    }

    /**
     * Criar Vernacular a partir dos parametros informados
     */
    private void getVernacular(String language, String localy, String name, Long assessmentId) {
        vernacularRepository.save(Vernacular.builder()
                .language(language)
                .locality(localy)
                .name(name)
                .assessment(Assessment.builder().id(assessmentId).build())
                .build());
    }

    /**
     * Criar publication a partir dos parametros informados
     */
    private void getPublication(String bibliographic, String reference, String status, Long history) {
        publicationRepository.save(Publication.builder()
                .bibliographicCitation(bibliographic)
                .reference(reference)
                .status(status)
                .history(History.builder().id(history).build())
                .build());
    }

    /**
     * Criar distribuition a partir dos parametros informados
     */
    private void getDistribuition(History history, String localy, String category) {
        if (StringUtils.isNotBlank(category))
            distribuitionRepository.save(Distribuition.builder()
                    .history(history)
                    .locality(localy)
                    .build());
    }

    /**
     * Criar notes a partir dos parametros informados
     */
    private void getNote(History history, String notes, String reason, String reserach) {
        noteRepository.save(Note.builder()
                .history(history)
                .notes(notes)
                .reason(reason)
                .research(reserach)
                .build());
    }
}
