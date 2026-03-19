package battleship;

import java.util.List;

public class LLMService {
    private final HuggingFaceClient client;
    private final String initialPrompt;

    public LLMService(String apiKey) {
        this.client = new HuggingFaceClient(apiKey);
        this.initialPrompt = """
                CONTEXTO E PAPEL

                Você é um estratega especialista no jogo da Batalha Naval (versão Descobrimentos Portugueses).
                O seu objetivo é afundar toda a frota inimiga com o menor número possível de tiros.

                REGRAS CRÍTICAS (OBRIGATÓRIAS)

                A resposta DEVE ser APENAS um objeto JSON válido
                NÃO incluir texto fora do JSON
                NÃO usar markdown (```json)
                Cada resposta deve conter exatamente 3 tiros válidos

                Coordenadas válidas:
                Linhas: A–J
                Colunas: 1–10
                Se violar qualquer regra acima, a resposta é inválida.

                MEMÓRIA (DIÁRIO DE BORDO – INTERNO)

                Você deve manter internamente um Diário de Bordo, que NÃO deve ser incluído na resposta.

                Para cada rajada:
                Número da rajada
                Coordenadas disparadas
                Resultado de cada tiro (água, atingido, afundado, tipo)
                Use este histórico para:
                Evitar tiros repetidos
                Inferir posições de navios
                Marcar zonas impossíveis (halo)

                PROIBIÇÕES

                Nunca repetir coordenadas já usadas
                Nunca disparar fora do tabuleiro
                Nunca disparar em diagonais de um acerto (exceto possível galeão)
                Nunca disparar adjacente a navio já afundado

                REGRAS TÁTICAS

                Prioridade máxima:
                Se houver um acerto anterior → atacar posições contíguas (N/S/E/O)
                Se navio afundado → marcar halo e ignorar zona

                Inferência:
                Caravelas, Naus, Fragatas → linhas retas
                Galeão → forma em T (única exceção para diagonais úteis)

                ESTRATÉGIA DE BUSCA

                Use uma estratégia híbrida:
                Probabilidade
                Começar perto do centro do tabuleiro
                Padrão adaptativo
                Dividir tabuleiro em quadrantes
                Usar padrão alternado (0/1)
                Saltos de 3 posições + ajuste ortogonal
                Movimento em espiral (ex: D3 → G4 → F7 → C6)
                Depois deslocar padrão na diagonal

                Eficiência
                Não seguir padrões rígidos previsíveis
                Não perseguir todos os acertos imediatamente
                Tentar encontrar múltiplos navios antes de finalizar

                RACIOCÍNIO

                O campo "raciocinio" deve conter:
                Explicação curta (1–2 frases)
                Estratégia aplicada (ex: exploração, continuação de acerto, padrão)
                NÃO incluir cadeia de pensamento detalhada.

                INPUT

                Você receberá um Histórico (Diário de Bordo externo) com resultados da última jogada.
                OUTPUT (FORMATO FIXO)
                {
                "raciocinio": "Explicação curta da decisão tática.",
                "rajada": [
                {"row": "A", "column": 1},
                {"row": "B", "column": 2},
                {"row": "C", "column": 3}
                ]
                }

                REGRA FINAL

                Se a frota inimiga estiver totalmente afundada:
                Continue a enviar 3 tiros (mesmo repetidos)
                Mas indique isso no raciocínio

                VALIDAÇÃO FINAL

                Antes de responder, valide mentalmente:
                Não repeti tiros
                Todos dentro do tabuleiro
                JSON válido
                Se alguma condição falhar, corrija antes de enviar.
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
                history.append("Rajada ").append(move.getNumber()).append(": ").append(Game.jsonShots(move.getShots()))
                        .append("\n");
                history.append("Relatório: ").append(move.processEnemyFire(false)).append("\n\n");
            }
        }

        String fullPrompt = initialPrompt + "\n" + history.toString()
                + "\nO seu output estritamente em JSON (com 'raciocinio' e 'rajada'):";

        String response = client.chat(fullPrompt);
        return cleanJsonResponse(response);
    }

    private String cleanJsonResponse(String response) {
        int arrayStartMarker = response.indexOf("\"rajada\"");
        int start = -1;

        if (arrayStartMarker != -1) {
            start = response.indexOf("[", arrayStartMarker);
        } else {
            start = response.indexOf("[");
        }

        int end = response.lastIndexOf("]");

        if (start != -1 && end != -1 && start < end) {
            return response.substring(start, end + 1);
        }
        return response.trim();
    }
}
