# projetos-tivit
Resolução do Desafio 1 :
gitgub  : https://github.com/marcelo-rebello1982/projetos-tivit/tree/main/st-api-teste-tivit 
projeto : st-api-teste-tivit
package : br.com.cadastroit.services.desafio1
class   : Desafio1.java


2) Descubra a lógica e complete o próximo elemento:

pela lógica a sequencia da opção A seria :
a) 1, 3, 5, 7, 9
b) 2, 4, 8, 16, 32, 64, 128
c) 0, 1, 4, 9, 16, 25, 36, 49
d) 4, 16, 36, 64, 100
e) 1, 1, 2, 3, 5, 8, 13 ( sequencia de fibbonaci )


Resolução do Desafio 3 :

3) Dado um vetor que guarda o valor de faturamento diário de uma distribuidora de todos os dias de um ano, faça um programa, na linguagem que desejar, que calcule e retorne:

- O menor valor de faturamento ocorrido em um dia do ano;
- O maior valor de faturamento ocorrido em um dia do ano;
- Número de dias no ano em que o valor de faturamento diário foi superior à média anual.

a) Considerar o vetor já carregado com as informações de valor de faturamento.

b) Podem existir dias sem faturamento, como nos finais de semana e feriados. Estes dias devem ser ignorados no cálculo da média.

gitgub  : https://github.com/marcelo-rebello1982/projetos-tivit/blob/main/st-api-teste-tivit/src/main/java/br/com/cadastroit/services/desafio3/main/Desafio3.java  
projeto : st-api-teste-tivit
package : br.com.cadastroit.services.desafio3.*
class   : Desafio3.java


Resolução do Desafio 4 :

gitgub       : https://github.com/marcelo-rebello1982/projetos-tivit/tree/main/st-apitivit-cadastroclientes 
projeto      : st-apitivit-cadastroclientes
package      : br.com.cadastroit.services.*
Controller   : PessoaController

4.1 ) Um cliente pode ter um número ilimitado de telefones; implementado no modelo relacional em br.com.cadastroit.services.model

4.2 ) Cada telefone de cliente tem um tipo, por exemplo: comercial, residencial, celular, etc. O sistema deve permitir cadastrar novos tipos de telefone.
  4.2.2 : implementado os controladores PessoaController , TelefoneController e EstadoController.

4.3 ) A princípio, é necessário saber apenas em qual estado brasileiro cada cliente se encontra. O sistema deve permitir cadastrar novos estados;

Solução:

  4.3.1 no PessoaController, basta no método /all passar a url desta forma : http://localhost:8080/administracao/pessoa/all/1/100?order=desc&resumir=false ou order=desc&resumir=true
     a api recebe um Map com chave/valor, e de acordo com o valor da chave resumir, retorna uma lista paginada de um DTO completo ( com todos os dados de pessoa )  ou resumido com apenas os seguintes dados:
id , nome , nome estado e sigla , no header retorna um summaryCount com a quantidade de objetos localizados.

4.4 para cadastrar novos estados , utilizar EstadoController.


Você ficou responsável pela parte da estrutura de banco de dados que será usada pelo aplicativo. Assim sendo:

4.5 ) Proponha um modelo lógico para o banco de dados que vai atender a aplicação. Desenhe as tabelas necessárias, os campos de cada uma e marque com setas os relacionamentos existentes entre as tabelas;
  4.5.1     modelo em st-apitivit-cadastroclientes\Diagrama ER de banco de dados.pdf

4.5.2 ) - Faça uma busca utilizando comando SQL que traga o código, a razão social e o(s) telefone(s) de todos os clientes do estado de São Paulo (código "SP");
  4.5.2.1 - implementado PessoaController 3 opções nas seguintes rotas :
     opção 1 - http://localhost:8080/administracao/pessoa/findUfByCriteria/SP
               busca pela sigla estado fazendo join com pessoa/estado utilizando
	       CriteriaBuilder / CriteriaQuery / Root / Join e TypedQuery 

     opção 2 - http://localhost:8080/administracao/pessoa/findByQueryParam/SP
               utiliza sql nativo junto ao EntityManager, Query e retorna uma 
	       lista do objeto tipo pessoa.

     opção 3 - http://localhost:8080/administracao/pessoa/findUfByJPQL/1/100
	       aqui eu preciso passar no header o estadoId , pré supondo que na aplicação eu terei um combo onde
	       poderia por exemplo selecionar o estado e no header enviar o estadoId , via JPQL é feito um join
	       com estado e , utiliza JPQL puro, retorna um List<Object[]> result = query.getResultList();
	       que é recuperado e convertido para um objeto pessoa , que por sua vez 
	       é inserido no retorno de uma lista de pessoa



Resolução do Desafio 5 :

Para resolver , precisamos considerar

1)
Carro de    Ribeirão Preto para Barretos: velocidade de 90 km/h.
Caminhão de Barretos para Ribeirão Preto: velocidade de 80 km/h.
Distância entre Ribeirão Preto e Barretos: 125 km.

Questão : "Quando eles se cruzarão no percurso, qual estará mais próximo da cidade de Ribeirão Preto?"


2) Definir a distância total e a velocidade relativa:

Vrelativa  = 90+80 =170 km/h

2.1) Calcular o tempo ("t") até o encontro:    distancia/velocidade

t= 170/125 0,735 horas = 44 minutos

2.2 ) tempo adicional gasto pelo carro com pedágios , levando em conta 15 minutos a mais.

tCarro = ( 170/125 0,735 horas ) + 15 = 59 minutos
tCarro = 59

3) Distância  Percorrida  pelo  Caminhão em  44  minutos:

dCaminhao = 80 x ( 44/60) = 58.67
dRestante = 125 - 58.27 = 66.73

4) Distância  Percorrida  pelo  Carro  59  minutos:

dCarro = 90 x ( 59/60) = 88.50
diferencaDistancia = 125 - 88.50 = 36.50

considerando os 3 pedagios, o carro terá percorrido uma distancia maior em ralação ao seu ponto de partida, que é
Ribeirão preto, o caminhão mesmo em uma velocidade menor, no sentido oposto terá percorrido uma distância menor em 
relação ao carro, mas em relação a Ribeirão preto, estará em uma distância menor que o carro, portanto o caminhão estará
mais perto de ribeirão pretro.

