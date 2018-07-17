package br.gov.sibbr.api.integration.service;

import br.gov.sibbr.api.core.helper.JsonMapper;
import br.gov.sibbr.api.integration.dao.TaxonomyDAO;
import br.gov.sibbr.api.integration.dto.taxonomy.HerarchyClassificationDTO;
import br.gov.sibbr.api.integration.dto.taxonomy.HierarchyComplementaryDTO;
import br.gov.sibbr.api.integration.dto.taxonomy.HierarchyDTO;
import br.gov.sibbr.api.integration.entity.taxonomy.*;
import br.gov.sibbr.api.integration.repository.taxonomy.HierarchyRepository;
import br.gov.sibbr.api.integration.repository.taxonomy.TaxonomyRespository;
import br.gov.sibbr.api.integration.resource.CatalogueOfLife;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class HierarchyService {

    @Autowired
    private CatalogueOfLife catalogueOfLife;

    @Autowired
    private TaxonomyDAO taxonomyDAO;
    @Autowired
    private TaxonomyRespository taxonomyRespository;
    @Autowired
    private HierarchyRepository hierarchyRepository;

    /**
     * HIERARCHY TYPE COMPLEMENTARY: Geração da estrutura hierarchy tipo complementary das lista de higher txns
     */
    public void txnHierarchyComplementary(List<HierarchyDTO> hierarchys) {
        //todas higher hierarchys e consulta no catalogo da vida sua respectiva hierarquia para geração complementar
        hierarchys.stream()
                .peek(e -> e.setComplementary(findHierarchyByCol(e.getName(), e.getRank())))
                .filter(e -> !CollectionUtils.isEmpty(e.getComplementary()))
                .forEach(e -> {
                    if (e.getComplementary() != null)
                        this.generatedComplemetary(e.getComplementary(), e.getKingdomId(), e.getHigherTxn());
                });
    }

    /**
     * Consultar hierarquia taxonomica complementar no catalogue of life para especie e rank informado
     */
    private List<HierarchyComplementaryDTO> findHierarchyByCol(String scientificName, String rank) {
        String col = catalogueOfLife.searchByName(scientificName, rank.toLowerCase(), "json", "full");
        return JsonMapper.toList(col, "/results", HierarchyComplementaryDTO.class);
    }

    /**
     * Gerar hierarquia apartir de cada complementary da hierarchy
     */
    private void generatedComplemetary(List<HierarchyComplementaryDTO> complementarys, Long kingdomId, Long higherTxn) {
        complementarys.stream().peek(System.out::println).forEach(e -> {
            hierarchyComplementary(e.getClassification(), kingdomId, higherTxn);
        });
    }

    /**
     * Consultar item da lista de classification obtida do catalogo da vida para especie pesquisa
     * e gerar informações e hieraquia complementar para concatenação das hierarchys
     */
    private void hierarchyComplementary(List<HerarchyClassificationDTO> classification, Long kingdomId, Long higherTxn) {
        Long parent = null;
        String complementary = "";
        List<Long> hierarchys = new ArrayList<>();

        // obter dados da arvore complementar
        for (HerarchyClassificationDTO e : classification) {
            List<Taxonomy> taxon = taxonomyRespository.findByScientificName(e.getName());

            Taxonomy txn = getTaxonomy(taxon, kingdomId, parent, e.getName(), e.getRank());
            parent = txn != null ? txn.getTxn() : null;

            if (parent != null) {
                hierarchys.add(parent);
                complementary += parent + " | ";
            }
        }

        long higher = hierarchys.isEmpty() ? 0 : hierarchys.get(0);

        // consultar lista de hierarquias que contem higherTxn informado
        List<Hierarchy> hierarchy = hierarchyRepository.findByHigherTxnAndType(higherTxn, "hierarchy");

        // gerar lista de hierarquias complementares com base para gerar nova lista com complementariedade
        this.hierarchysComplementary(hierarchy, complementary, hierarchys.size(), higher, parent);
    }

    /**
     * Obter lista de hierarquias complementares e para criar item complementar informações de complementariedade
     */
    private void hierarchysComplementary(List<Hierarchy> hierarchys, String complementary, Integer level, Long higher, Long parent) {
        List<Hierarchy> hierarchysComplementarys = hierarchys.stream()
                .map(e -> getHierarchyComplemetary(e, complementary, level, parent, higher == 0 ? parent : higher))
                .collect(Collectors.toList());

        hierarchyRepository.save(hierarchysComplementarys);
    }

    /**
     * Obter hierarquia complementar formatada
     */
    private Hierarchy getHierarchyComplemetary(Hierarchy e, String complementary, Integer level, Long parent, Long higher) {
        String hierarchy = complementary + e.getHierarchy();
        Long parentTxn = e.getParentTxn() != null && e.getParentTxn() != 0 ? e.getParentTxn() : parent;

        return getHierarchyComplemetary("complementary", level + e.getLevel(), e.getCountChildren(), hierarchy, parentTxn, higher == 0 ? parent : higher, e.getTxn().getTxn());
    }

    /**
     * Obter taxonomy da listas de txns se existir ou gerar novo taxon
     */
    private Taxonomy getTaxonomy(List<Taxonomy> txns, Long kingdomId, Long parentTxn, String scientificName, String rank) {

        if (!CollectionUtils.isEmpty(txns))
            return txns.get(0);

        if (scientificName != null) {
            Taxonomy txn = this.getTaxonomyByReferenceOrigin(scientificName, rank, kingdomId, parentTxn, 3L);
            return taxonomyRespository.save(txn);
        }

        return null;
    }

    /**
     * HIERARCHY TYPE FULL: Geração da estrtura hierarchy tipo full a partir do tipo complementary
     */
    public void txnHierarchyFull(List<Hierarchy> hierarchys) {
        hierarchys.parallelStream().forEach(e -> {
            String hierarchy = this.getHierarchyFull(e.getHierarchy());
            Hierarchy typeFull = this.getHierarchyTypeFullByComplementary(e, hierarchy);

            hierarchyRepository.save(typeFull);
        });
    }

    /**
     * Obter hierarquia complementary [A | B | C]  e substituir txn por sua respectiva
     * informação complementar [Especie:Rank:KingdomId | Especie:Rank:KingdomId ...]
     */
    private String getHierarchyFull(String complementary) {
        String full = complementary;

        for (HierarchyDTO e : getHierarchiesByComplemntary(complementary)) {
            full = StringUtils.replace(full, e.getTxn() + "", e.getHierarchyFull());
        }

        return full;
    }

    /**
     * Obter lista de txns da arvore complementar e consultar respectivos taxons e
     * kingdons de cada txn para elaboração da hierarqui full
     */
    private List<HierarchyDTO> getHierarchiesByComplemntary(String complementary) {

        // converter hierachy complementary formato ('A' | 'B' | 'C') em lista de inteiros [A,B,C]
        final List<Integer> txns = Arrays.stream(complementary.replace(" ", "").split(Pattern.quote("|")))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        // informar txns para retornar informações complementares de rank e kingdom para relaoração da hierarquia full
        return taxonomyDAO.findHierarchyFullByTxns(StringUtils.join(txns, ","));
    }

    /**
     * Criar Hierarchy do tipo full apartir dos dados de sua arvore complementar e nova hierarquia
     */
    private Hierarchy getHierarchyTypeFullByComplementary(Hierarchy comp, String hierarchy) {
        return this.getHierarchyComplemetary("full", comp.getLevel(), comp.getCountChildren(), hierarchy, comp.getParentTxn(), comp.getHigherTxn(), comp.getTxn().getTxn());
    }

    /**
     * HIERARCHY TYPE HIGHER/CLASSIFICATION: Criar Hierarchy do tipo hierarchyComplementary ou higherClassification a partir das informações de hierarchy
     */
    public void txnHierarchyTypeClassification(HierarchyDTO hierarchyDTO, String type, String hierarchy, Long txn, Long parentTxn) {
        if (hierarchyDTO != null) {
            Long childrens = taxonomyDAO.countByParentTxn(txn);
            Hierarchy classification = this.getHierarchyComplemetary(type, hierarchy.split(Pattern.quote("|")).length, childrens, hierarchy, parentTxn, null, txn);

            hierarchyRepository.save(classification);
        }
    }

    /**
     * HIERARCHY TYPE CLASSIFICATION: Gerar hiearachy hierarchyComplementary a partir de campos hierarcquias taxonomicas
     */
    public String txnHierarchyTypeClassification(HierarchyDTO hrq) {
        String three = (StringUtils.isBlank(hrq.getKyngdom()) ? "" : hrq.getKyngdom().concat(" | ")) +
                (StringUtils.isBlank(hrq.getPhylum()) ? "" : hrq.getPhylum().concat(" | ")) +
                (StringUtils.isBlank(hrq.getClasse()) ? "" : hrq.getClasse().concat(" | ")) +
                (StringUtils.isBlank(hrq.getOrder()) ? "" : hrq.getOrder().concat(" | ")) +
                (StringUtils.isBlank(hrq.getFamily()) ? "" : hrq.getFamily().concat(" | ")) +
                (StringUtils.isBlank(hrq.getGenus()) ? "" : hrq.getGenus());

        return StringUtils.removeEnd(three, " | ");
    }

    /**
     * HIERARCHY TYPE HIERARCHY: Criar Hierarchy do tipo hierarchy da estrtura taxonomica de parent
     */
    public void txnHierarchyTypeHierarchy(List<Taxonomy> txns, List<HierarchyDTO> hyerachys) {
        txns.parallelStream().peek(System.out::println).forEach(e -> {
            Long childrens = taxonomyDAO.countByParentTxn(e.getTxn());

            HierarchyDTO hierarchyDTO = hyerachys.stream()
                    .filter(x -> x.getTxn().equals(e.getTxn()))
                    .findFirst()
                    .orElse(null);

            // String hierarchy = this.getHierarchyIntegration(txn, txn);
            String hierarchy = hierarchyDTO == null ? null : hierarchyDTO.getHierarchyFull();
            Long higherTxn = hierarchy == null ? null : Long.valueOf(hierarchy.split(Pattern.quote("|"))[0].trim());
            Hierarchy a = this.getHierarchyComplemetary("hierarchy", hierarchy.split(Pattern.quote("|")).length, childrens, hierarchy, e.getParentTxn(), higherTxn, e.getTxn());

            hierarchyRepository.save(a);

        });
    }

    /**
     * Obter hierarchy a partir dos parametros informados
     */
    private Hierarchy getHierarchyComplemetary(String type, Integer level, Long childrens, String hierarchy, Long parentTxn, Long higherTxn, Long txn) {
        return Hierarchy.builder()
                .type(type)
                .level(level)
                .countChildren(childrens)
                .hierarchy(hierarchy)
                .parentTxn(parentTxn)
                .higherTxn(higherTxn)
                .txn(Taxonomy.builder().txn(txn).build())
                .build();
    }

    /**
     * Obter taxonomy a partir dos parametros informados
     */
    private Taxonomy getTaxonomyByReferenceOrigin(String scientificName, String rankName, Long kingdomId, Long parentTxn, Long originId) {

        Reference reference = Reference.builder().origin(Origin.builder().id(originId).build()).build();
        Rank rank = taxonomyDAO.getRankByNameOrSimilar(rankName.replaceAll("[^A-Za-z0-9]", ""));

        return Taxonomy.builder()
                .rank(rank)
                .scientificName(scientificName)
                .parentTxn(parentTxn)
                .kingdom(Kingdom.builder().id(kingdomId).build())
                .status("accept")
                .reference(Collections.singletonList(reference))
                .build();
    }

}
