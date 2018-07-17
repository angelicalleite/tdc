--- ANALISES DE CARGAS BASE BRUTA PARA NRMALIZADA ---

A seguir algun pontos e analises a serem considerados identificados no processamento dos dados brutos para o normalizado

flora so tem informação de endmism em api


#0:COMPLEMENTARIEDADE

- Talvez fazer coluna em hierarquia informando qual foi ultimo nivel preenchido, já que o parent so se referece ao rank predecessor
- Analise diferenciada para rank com parent vazios
- Analise diferenciada para elementos somente com um nivel
- Primeiro momento levar em consideração para complementariedade apenas principais ranks [ordem, filo, reino ...]

- Consulta identificar parents em comum das especies para melhor performace do algoritmo analisar dados de complementariedade pelo parent
- 280195 especies registradas 54095 são os parents encontrados em comun
- SELECT parent_txn FROM hierarchy.hierarchy WHERE type = 'hierarchy' GROUP BY parent_txn;
- Talvez na complementar colocar info do respectivo rank;
- corrigir algortmo parent zerado e contando e setando valor ao level e hieraquia;
- Atualizar quantidade de filhos  na hierarchy do type complementary
- Atualizar lognames apos novos itens

- Analisar se emenda parent dados fauna e flora normal com col


#0:CATALOGUE OF LIFE

- Col trata informações de extinção com uma flag is_extintic

#0:Ideias gerais para aplicação do projeto para melhor performance
+ Uso bibliotecas e api ala matching e gbif nameparser para analise de nomes cientificos
+ Analise de informações e detalhamento sobre especie ameaçadas para complemtar informações bases de ameaçada (nome, grau ameaça) brasileiro e iucn
+ Flora abrange reino fungi e plantae
+ Depois revisar e analisar estruturas de integridade das informações, uniques, tipos valores 
+ Analisar dados tabela data banco fauna
+ Verificar demais campos que estão associados ao endemism en distribuition e taxonomy
+ Colocar terceiro template jdbc para leitura do itis
+ Ver com david uso da lib gbif ou ala, já que o ala foi modificado para contexto deles, qual ter como referencia
+ Ver se para taxon orfãns que não tem seu respectivo parent pode ser cadastrado esse novo taxon para complementar essa informação com 
  id de refencia diferente fazendo referencia ao itis e seu id de origem
  
+ Taxon do rank filo não possui referencia de pai
+ Campo locality e locality_id parecem ser iguais
+ IPT reflora não contém informações sobre endemism consultar serviço
+ TypeSpecimen no reflora e relativo ao voucher disponivel no catalogo da flora
+ 

+ Informações de voucher estão na tabela specie_profiles do reflora, informção não disponivel para fauna

+ Base da Flora contém os reinos protistas, fungi e platae e possivel identificar o reino apenas pela informação fornecida no highterclassification

#1: Alguns dados não estão completos como synonimos, parents com a quantidade de origem devido a
+ Inconsistencia das informações, para isso o arquivo de relatorio.sql tem algumas consultas para verificar e analisar a
completude das informações

#2: Ideias a serem aproveitadas da base do ITIS
+ Flag falando sobre completudo da informação
+ Flag falando sobre uso do nome, valido ou invalido (analisar se essa flag tem relação com nomes aceitos e sinonimos)
+ Flag falando sobre credibilidade da informação (essa e boa para quando tiver as informações gerenciada pelo expertises)
+ Flag sobre crediblidade da informação e sua font
+ Informações sobre author e publicação separadas
+ Alguns parentes names ou parent_txn estão orfã não pq não existe, mas pq o nome do parent name não foi encontrado no scientificName

#3: Analise com André sobre a gerencia das informações pelos expertises
+ Base de apoio que ira receber os dados intermediarios para anaslise antes da informação voltar corrigida para base oficial
+ Flags na base normalizada para informar a credibilidade da informações 

#4: Extração dados bruto para normalizado FAUNA
+ Taxon (scientificName, status) gerar referencia de id para dado de origem
+ Rank com base no rank definido para scientificName (** ver estrategia para melhor associação do rank bruto com rank normalizado)
+ Parents (** inconsistencias de quantidade encontradas devido a alguns taxon terem parents que não possui scientificsNames associados)
+ Sinonimos (** inconsistencias de informações pois esses dados pode ser extraidos de diversas formas tanto pela 
  tabela sinonimos quanto pelo campo de accept_name_usage para taxon do tipo sinonimo)
+ VernacularName
+ SpecieProfile (** fauna life_substrate e flora: species_profile gbif usar SpecieProfile)
+ Semantics representa nomes scientificos detalhads, pode ser usado no harpia para melhor detalhamento da informação. 
  ** Uso api e lib gbif para analise de nomes cientifcos
+ Publicação (bibliografia, referencia)
+ Author com adição flag se autor e hibrido ou não usada pelo itis + complementar com informação quebradas pelo 
  semantics (Autores podera ser identificados tanto pela tabelas de lognames quanto author)
+ Endemism
+ Distribuição
+ Hierarchy (** extrair arvore higheterclassification, normal, complementar e gerar pela hierarquia dos rank, se algum item 
  da hierarquia for vazio encontrar sinlizador para indicar essa informação) 

#5: Extração dados FLORA


#6: Extração Gerais

- Ameaça catalogo Fauna e Flora com cncflora, crawler icmbio e iucn.
- Incorporar ideia André de data de criação e data de atualização dos dados.

ESTRATEGIA COMPLEMENTARIEDADE

- Quando bucar pelos parentes para complementariedade, olhar para coluna de parent e atualizar somente dados daquele parent

#7: Exemplo problema nomes
Acianthera pectinata,Acianthera pectinata (Lindl.) Pridgeon & M. W. Chase
Acianthera pectinata,Acianthera pectinata Pridgeon & M. W. Chase


Ameaças

Tratamento nomes para eliminar nomes repetidos, usar strategia canonical name mas cuidado com nome com espaços duplos que evita geração nome corretamente

encontrar nomes com espaços duplicados SELECT *
                                       FROM assessment.assessment
                                       WHERE assessment.scientific_name ~ '\s\s';
                                       
                                       
exexmplo
Hoffmannseggella  macrobulbosa (Pabst) H. G. Jones, devido ao espaço gera canonical name Hoffmannseggella sendo correto Hoffmannseggella macrobulbosa

tratar espaço duplo e trim no armazenamento do scientificname e canonical name
cnsultar se canonical name foi gerado corretamente, informara quantas palavras tem cada nome, analisar principalmente canonicais com unica palavra para ver se scientific name corresponde ou espaço gerou erro

SELECT
  canonical_name, scientific_name,
  array_length(regexp_split_to_array(canonical_name, '\s'), 1)
FROM taxonomy.semantics;

identificar canonicais names repetidos

SELECT
  canonical_name,
  scientific_name
FROM taxonomy.semantics
WHERE canonical_name IN (
  SELECT canonical_name
  FROM taxonomy.semantics
  GROUP BY canonical_name
  HAVING count(canonical_name) > 1)
  
  
  Analisar situação de taxons replicados
  
  consulta ver diferença de nomes authores e nomes autores gerados por semanatic
  
 select
   a.name       as author,
   s.authorship as s_author,
   s.bracket_authorship
 from taxonomy.taxonomy t INNER JOIN taxonomy.author a on t.author_id = a.id
   inner join taxonomy.semantic s on t.txn = s.txn
 where a.name <> s.authorship;