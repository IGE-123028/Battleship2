package battleship;

import java.util.List;

public class LLMService {
    private final HuggingFaceClient client;
    private final String initialPrompt;

    public LLMService(String apiKey) {
        this.client = new HuggingFaceClient(apiKey);
        this.initialPrompt = """
            Considere que é um perito no famoso jogo da Batalha Naval, aqui numa versão do tempo
            dos Descobrimentos Portugueses, jogado num tabuleiro com linhas identificadas de A a
            J e colunas de 1 a 10. Deve começar por criar secretamente a sua frota de 11 navios:
            • 4 Barcas (1 posição na quadrícula)
            • 3 Caravelas (2 posições na quadrícula)
            • 2 Naus (3 posições na quadrícula)
            • 1 Fragata (4 posições na quadrícula)
            • 1 Galeão (5 posições na quadrícula, em forma de T, com um corpo de 3 posições
            e, numa das suas extremidades, uma posição adicional para cada lado, correspon-
            dentes às chamadas "asas da ponte")
            
            Os navios podem ser gerados com qualquer orientação no tabuleiro, desde que não
            toquem uns nos outros, nem mesmo por um canto (i.e. na diagonal), embora possam
            estar encostados às margens do tabuleiro. Repare que enquanto uma caravela, uma nau
            ou uma fragata pode ter duas orientações possíveis (norte-sul ou este-oeste), um galeão
            pode ter quatro orientações diferentes.
            
            Tática ("few-shot prompting" e regras):
            1. A nossa interação será através de objetos JSON.
            Uma rajada tem o seguinte formato:
            [
              {"row": "A", "column": 5},
              {"row": "C", "column": 10},
              {"row": "F", "column" : 5}
            ]
            
            2. A resposta a uma rajada é feita em conjunto, como nos 3 exemplos:
            Exemplo A (Uma barca ao fundo, um tiro numa nau e um tiro na água):
            {
              "validShots": 3,
              "sunkBoats": [ {"count": 1, "type": "Barca"} ],
              "repeatedShots": 0,
              "outsideShots": 0,
              "hitsOnBoats": [ {"hits": 1, "type": "Nau"} ],
              "missedShots": 1
            }
            Exemplo B (Um tiro fora do tabuleiro, um tiro repetido e um tiro na água):
            {
              "validShots": 1,
              "sunkBoats": [ ],
              "repeatedShots": 1,
              "outsideShots": 1,
              "hitsOnBoats": [ ],
              "missedShots": 1
            }
            Exemplo C (Um tiro numa nau, um tiro no galeão e uma caravela ao fundo):
            {
              "validShots": 3,
              "sunkBoats": [{"count": 1, "type": "Caravela"}],
              "repeatedShots": 0,
              "outsideShots": 0,
              "hitsOnBoats": [{"hits": 1, "type": "Nau"}, {"hits": 1, "type": "Galeao"}],
              "missedShots": 0
            }
            
            3. Estratégia do jogo:
            • O Histórico abaixo atua como Diário de Bordo. A memória é a principal arma.
            • Não dispare fora dos limites do mapa (ex: Z99) nem repita tiros em coordenadas já testadas. A única exceção para este desperdício é a última rajada do jogo para perfazer 3 tiros se a frota inimiga já estiver afundada.
            • Se atingir um navio numa rajada, dispare nas posições contíguas (Norte, Sul, Este, Oeste) na jogada seguinte. Se a rajada anterior confirmar que o navio afundou, não dispare nas contíguas, os navios nunca se tocam.
            • Como Caravelas, Naus e Fragatas são linhas retas, as posições diagonais a um tiro certeiro são água. O Galeão também não toca outros navios nas diagonais. Evite atirar nas diagonais de alvos conhecidos e poupará tiros.
            • Confirmada a posição da carcaça do navio afundado, assuma o halo de 1 posição em redor do navio como água intransitável. É impossível haver outra embarcação ali.
            
            O seu ÚNICO objetivo nesta iteração é fornecer APENAS um objeto JSON com a sua próxima rajada de 3 tiros, usando o formato indicado, sem nenhum texto adicional de introdução ou saudação.
            """;
    }

    public String getNextMove(Game game) throws Exception {
        StringBuilder history = new StringBuilder();
        history.append("Histórico de tiros (Diário de Bordo):\n");
        
        List<IMove> alienMoves = game.getAlienMoves();
        if (alienMoves.isEmpty()) {
            history.append("Nenhuma rajada efetuada ainda. Todos os espaços de A1 a J10 podem ser atirados.\n");
        } else {
            for (IMove move : alienMoves) {
                history.append("Rajada ").append(move.getNumber()).append(": ").append(Game.jsonShots(move.getShots())).append("\n");
                history.append("Relatório: ").append(move.processEnemyFire(false)).append("\n\n");
            }
        }
        
        String fullPrompt = initialPrompt + "\n" + history.toString() + "\nPróxima rajada (JSON array, estritamente válido):";
        
        String response = client.chat(fullPrompt);
        return cleanJsonResponse(response);
    }

    private String cleanJsonResponse(String response) {
        int start = response.indexOf("[");
        int end = response.lastIndexOf("]");
        if (start != -1 && end != -1 && start < end) {
            return response.substring(start, end + 1);
        }
        return response.trim();
    }
}
