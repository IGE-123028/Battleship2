# Battleship 2.0 - RESPOSTAS

## Grupo: ADM

### B. Sprint - Parte 1

#### Exercício 2

O Maven começa a ler a lista de dependências e vai, uma a uma, procurá-las no repositório Maven local. Aquelas que não
estiverem disponíveis localmente serão obtidas no repositório remoto do Maven (Maven Central Repository). Estas
dependências declaradas são as dependências diretas. Muitas das vezes estas dependências diretas possuem as suas
próprias dependências diretas e, por sua vez, estas possuem mais dependências diretas. Neste sentido, são consideradas
dependências transitivas todas aquelas que sejam subdependências daquelas que foram declaradas no ficheiro POM.xml,
na raiz do projeto. 

Quando compilamos o projeto pela primeira vez, o Maven com o seu build automation engine tem que efetuar este tipo de
verificações sobre todas as dependências declaras (dependências diretas) e todas as suas dependências transitivas,
descarregando todas estas para o projeto. Este funcionamento resultará num tempo de compilação mais alargado numa
primeira vez, mas consideravelmente mais reduzido, visto que nas vezes seguintes, o processo de compilação já encontra
descarregadas a grande maioria das dependências.