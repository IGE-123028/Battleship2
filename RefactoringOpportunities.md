# Refactoring Opportunities (Martin Fowler Code Smells)

| Local                             | Code Smell (Fowler)                                     | Refactoring                                                         | Student ID |
| --------------------------------- | ------------------------------------------------------- | ------------------------------------------------------------------- | ---------- |
| Tasks::menu                       | Long Method / Large Class (God Class)                   | Extract Class; Extract Method (separate CLI from game logic)        | IGE-123010 |
| Move::processEnemyFire            | Long Method / Divergent Change                          | Extract Method; Introduce DTO (separate logic, formatting, and I/O) | IGE-123010 |
| Caravel::Caravel                  | Duplicated Code                                         | Extract Method; Strategy Pattern for placement                      | IGE-123010 |
| Ship::buildShip                   | Primitive Obsession                                     | Replace Type Code with Enum; Replace Null with Exception            | IGE-123010 |
| Compass::charToCompass            | Switch Statements                                       | Replace Conditional with Polymorphism / Factory Method              | IGE-123010 |
| BoardColor::colored               | Inappropriate Intimacy (presentation mixed with domain) | Move Method to UI layer (separation of concerns)                    | IGE-123010 |
| Position::equals/hashCode         | Data Class / Inconsistent State                         | Ensure immutability; Fix equals/hashCode contract                   | IGE-123016 |
| Game::randomEnemyFire             | Long Method / Complex Conditional                       | Simplify Conditional Logic; Replace Algorithm                       | IGE-123016 |
| Game::fireShots                   | Duplicated Code                                         | Extract Method (handle repeated shots in helper)                    | IGE-123016 |
| Fleet::createRandom               | Primitive Obsession                                     | Replace Strings with Enum; Introduce Configuration Object           | IGE-123016 |
| Messages::static init             | Temporary Field / Lazy Initialization misuse            | Replace with Lazy Initialization; Load from classpath               | IGE-123016 |
| LLMService::cleanJsonResponse     | Feature Envy                                            | Move Method; Use external JSON utility (Jackson)                    | IGE-123016 |
| HuggingFaceClient::chat           | Large Method / Exception Handling Smell                 | Extract Method; Introduce Specific Exceptions; Add timeouts/retries | IGE-123023 |
| PDFExporter::exportGameToPDF      | Long Method                                             | Extract Method (header, stats, table); Introduce Builder            | IGE-123023 |
| Scoreboard::saveResult            | Inappropriate Intimacy / Hard-coded Dependency          | Introduce Configuration; Replace Error Handling                     | IGE-123023 |
| Game::printBoard                  | Inappropriate Intimacy                                  | Encapsulate Field; Introduce Query Methods                          | IGE-123023 |
| Frigate::Frigate                  | Duplicated Code                                         | Extract Method; Strategy Pattern                                    | IGE-123023 |
| Messages::get                     | Middle Man                                              | Remove Middle Man; Inline Method                                    | IGE-123023 |
| IMove / Move                      | Data Class                                              | Move Behavior to Class; Encapsulate Data                            | IGE-123028 |
| Game::repeatedShot                | Message Chains                                          | Hide Delegate (Move.hasShot)                                        | IGE-123028 |
| Carrack::Carrack                  | Duplicated Code                                         | Extract Method; Strategy Pattern                                    | IGE-123028 |
| Game::randomEnemyFire (temp vars) | Temporary Field                                         | Replace Temp with Query; Refactor Loop                              | IGE-123028 |
| BoardColor (class)                | Lazy Class                                              | Inline Class / Move to UI layer                                     | IGE-123028 |
| Fleet::printStatus                | Dead Code                                               | Remove Dead Code                                                    | IGE-123028 |

**Author:** ADM
