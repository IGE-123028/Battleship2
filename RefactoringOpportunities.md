# Refactoring Opportunities

| Local | Nome do cheiro no código | Nome da refabricação | Responsável |
|---|---|---|------------:|
| Tasks::menu | Long Method / God Object | Extract Class / Extract Method (separar CLI e lógica) |  IGE-123010 |
| Move::processEnemyFire | Long Method / Mixed Concerns (lógica + formatação + I/O) | Extract Method / Extract Class (resultado vs serialização) |  IGE-123010 |
| Caravel::Caravel | Duplicate Code (posicionamento repetido) | Extrair Método / Strategy para posicionamento |  IGE-123010 |
| Carrack::Carrack | Duplicate Code (posicionamento repetido) | Extrair Método / Strategy para posicionamento |  IGE-123010 |
| Frigate::Frigate | Duplicate Code (posicionamento repetido) | Extrair Método / Strategy para posicionamento |  IGE-123010 |
| Ship::buildShip | Primitive Obsession / Retorno nulo | Replace Type Code with Enum; lançar IllegalArgumentException |  IGE-123010 |
| Position::equals/hashCode | Inconsistent Contract (hashCode inclui campos mutáveis) | Corrigir equals/hashCode (usar só row e column imutáveis) |  IGE-123016 |
| Game::randomEnemyFire | Complex Logic / Ineficiente | Simplificar; usar ThreadLocalRandom; usar Set para posições usadas |  IGE-123016 |
| Game::fireShots | Duplicate Logic | Extrair método comum para processar tiros |  IGE-123016 |
| Fleet::createRandom | Primitive Obsession (strings literais para tipos) | Usar Enum para tipos de navio; centralizar configuração |  IGE-123016 |
| Messages::static init | Static initializer faz IO / usa caminho de ficheiro | Carregar recursos do classpath; remover IO em static init |  IGE-123016 |
| LLMService::cleanJsonResponse | Fragile parsing / Feature Envy | Mover parsing para util JSON (Jackson) e validar |  IGE-123016 |
| HuggingFaceClient::chat | Broad exception handling / Sem timeouts | Introduzir timeouts/retries; usar DTOs e erros específicos |  IGE-123023 |
| PDFExporter::exportGameToPDF | Large Method / Mixed Concerns | Extrair cabeçalho/estatísticas/tabela em métodos |  IGE-123023 |
| Scoreboard::saveResult | Swallowed IO exceptions / Caminho fixo | Usar caminho configurável; log e não engolir exceções |  IGE-123023 |
| BoardColor::colored | Presentation in domain (ANSI dependency) | Mover apresentação para camada UI |  IGE-123023 |
| Compass::charToCompass | Switch Statement | Replace Conditional with Polymorphism / Factory |  IGE-123023 |
| Game::printBoard | Inappropriate Intimacy | Encapsulate Ship internals / Introduce query APIs |  IGE-123023 |
| Messages::get | Middle Man | Simplify API / Remove middle man |  IGE-123028 |
| IMove::/ Move:: | Data Class | Extract behavior to service / Convert to DTO |  IGE-123028 |
| Game::repeatedShot / myRepeatedShot | Message Chains (move.getShots().contains(...)) | Encapsulate query (e.g., Move.hasShot) |  IGE-123028 |
| Game::randomEnemyFire (temp) | Temporary Field | Remove temporary field reuse; redesign loop |  IGE-123028 |
| BoardColor | Lazy Class | Move to UI module; remove domain dependency |  IGE-123028 |
| Fleet::printStatus | Comments smell (commented-out code) | Remove dead/commented code or implement |  IGE-123028 |

Author: ADM
