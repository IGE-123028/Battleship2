# RefactoringOpportunities

| Local | Nome do cheiro no código | Nome da refabricação | Número d@ alun@ |
|---|---|---|---:|
| Tasks::menu | Long Method / God Object | Extract Class / Extract Method (separar CLI e lógica) | |
| Move::processEnemyFire | Long Method / Mixed Concerns (lógica + formatação + I/O) | Extract Method / Extract Class (resultado vs serialização) | |
| Caravel::Caravel | Duplicate Code (posicionamento repetido) | Extrair Método / Strategy para posicionamento | |
| Carrack::Carrack | Duplicate Code (posicionamento repetido) | Extrair Método / Strategy para posicionamento | |
| Frigate::Frigate | Duplicate Code (posicionamento repetido) | Extrair Método / Strategy para posicionamento | |
| Ship::buildShip | Primitive Obsession / Retorno nulo | Replace Type Code with Enum; lançar IllegalArgumentException | |
| Position::equals/hashCode | Inconsistent Contract (hashCode inclui campos mutáveis) | Corrigir equals/hashCode (usar só row e column imutáveis) | |
| Position::adjacentPositions | Documentation mismatch (inclui diagonais) | Ajustar documentação ou lógica para refletir intenção | |
| Game::getAlienFleet | Bug (retorna myFleet) | Corrigir retorno para alienFleet; adicionar teste unitário | |
| Game::randomEnemyFire | Complex Logic / Ineficiente | Simplificar; usar ThreadLocalRandom; usar Set para posições usadas | |
| Game::fireShots | Duplicate Logic | Extrair método comum para processar tiros | |
| Fleet::createRandom | Primitive Obsession (strings literais para tipos) | Usar Enum para tipos de navio; centralizar configuração | |
| Messages::static init | Static initializer faz IO / usa caminho de ficheiro | Carregar recursos do classpath; remover IO em static init | |
| LLMService::cleanJsonResponse | Fragile parsing / Feature Envy | Mover parsing para util JSON (Jackson) e validar | |
| HuggingFaceClient::chat | Broad exception handling / Sem timeouts | Introduzir timeouts/retries; usar DTOs e erros específicos | |
| PDFExporter::exportGameToPDF | Large Method / Mixed Concerns | Extrair cabeçalho/estatísticas/tabela em métodos | |
| Scoreboard::saveResult | Swallowed IO exceptions / Caminho fixo | Usar caminho configurável; log e não engolir exceções | |
| BoardColor::colored | Presentation in domain (ANSI dependency) | Mover apresentação para camada UI | |
