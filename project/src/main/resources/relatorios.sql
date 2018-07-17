-- RELATORIOS SQL PARA CARGAS DE DADOS DA BASE BRUTA PARA NORMALIZADA

-- todo: ver especies que ficaram orfã
-- todo: ver scientificName que não são parents de ninguem
-- todo: ver nivel da arvore que ele está começand de um kingdom = 1
-- todo: ver parents que não contem filhos
-- todo: saber quantidade de filhos, quem e o pai, qual nivel ele está de, grau de completude da arvore em protagem talvez

-- Quantidade de taxon parent que não não contêm scientific
-- name correspondente Total: 158332 Parent Orfã: 2957
SELECT parent_name_usage
FROM fauna.taxon
WHERE parent_name_usage IN (SELECT scientific_name
                            FROM fauna.taxon);

-- Quantidade de taxon que taxonId existe na tabela de taxons
SELECT *
FROM fauna.name_vernacular
WHERE taxon_id IN (SELECT taxon_id
                   FROM fauna.taxon);

-- Quantidade de taxon que taxonId existe na tabela de life and substract
SELECT *
FROM fauna.name_vernacular
WHERE taxon_id IN (SELECT taxon_id
                   FROM fauna.taxon);

-- Carga distriuição fauna: total 105148 mas apenas 103817 possui taxons registrados na tabela de taxon
SELECT count(*) FROM fauna.distribution WHERE taxon_id in (SELECT taxon_id FROM fauna.taxon);
