# Refactoring Opportunities (Martin Fowler Code Smells)

| Local                             | Code Smell (Fowler)                                     | Refactoring                                                         | Student ID |
| --------------------------------- | ------------------------------------------------------- | ------------------------------------------------------------------- | ---------- |
| Game::repeatedShot                | Message Chains                                          | Hide Delegate (Move.hasShot)                                        | IGE-123010 |
| Game::myRepeatedShot              | Duplicated Code                                         | Extract Method (find repeated shots in helper)                      | IGE-123010 |
| Game::randomEnemyFire (temp vars) | Temporary Field                                         | Replace Temp with Query; Refactor Loop                              | IGE-123010 |
| Move::processEnemyFire            | Long Method / Divergent Change                          | Extract Method; Introduce DTO (separate logic, formatting, and I/O) | IGE-123010 |
| Caravel::Caravel                  | Duplicated Code                                         | Extract Method; Strategy Pattern for placement                      | IGE-123010 |
| Ship::buildShip                   | Primitive Obsession                                     | Replace Type Code with Enum; Replace Null with Exception            | IGE-123010 |
| Compass::charToCompass            | Switch Statements                                       | Replace Conditional with Polymorphism / Factory Method              | IGE-123010 |
| BoardColor::colored               | Inappropriate Intimacy (presentation mixed with domain) | Move Method to UI layer (separation of concerns)                    | IGE-123010 |
| Caravel / Carrack / Frigate    | Duplicated Code (line-filling logic)                    | Extract Superclass (LinearShip to hold common placement logic)      | IGE-123016 |
| Game::fireShots / fireMyShots | Duplicated Code (identical volley logic)                | Extract Method (unify common firing logic into a helper)            | IGE-123016 |
| Fleet::createRandom            | Primitive Obsession / Hard-coded dependency             | Introduce Parameter (for shipTypes composition array)               | IGE-123016 |
| LLMService::cleanJsonResponse  | Feature Envy                                             | Move Instance Method (to a JSON utility class)                      | IGE-123016 |
| LLMService (class)             | Hard-coded configuration                                | Introduce Parameter (for model name) and Introduce Field            | IGE-123016 |
| Game::fireShots                | Long expression / Complex logic                          | Introduce Variable (for 'isRepeatedInVolley' check)                 | IGE-123016 |
| HuggingFaceClient::chat           | Large Method / Exception Handling Smell                 | Extract Method; Introduce Specific Exceptions; Add timeouts/retries | IGE-123023 |
| PDFExporter::exportGameToPDF      | Long Method                                             | Extract Method (header, stats, table); Introduce Builder            | IGE-123023 |
| Scoreboard::saveResult            | Inappropriate Intimacy / Hard-coded Dependency          | Introduce Configuration; Replace Error Handling                     | IGE-123023 |
| Game::printBoard                  | Inappropriate Intimacy                                  | Encapsulate Field; Introduce Query Methods                          | IGE-123023 |
| Frigate::Frigate                  | Duplicated Code                                         | Extract Method; Strategy Pattern                                    | IGE-123023 |
| Messages::get                     | Middle Man                                              | Remove Middle Man; Inline Method                                    | IGE-123023 |
| IMove / Move                      | Data Class                                              | Move Behavior to Class; Encapsulate Data                            | IGE-123028 |
| Carrack::Carrack                  | Duplicated Code                                         | Extract Method; Strategy Pattern                                    | IGE-123028 |
| BoardColor (class)                | Lazy Class                                              | Inline Class / Move to UI layer                                     | IGE-123028 |
| Fleet::printStatus                | Dead Code                                               | Remove Dead Code                                                    | IGE-123028 |
| Tasks::menu                       | Long Method / Large Class (God Class)                   | Extract Class; Extract Method (separate CLI from game logic)        | IGE-123028 |

**Author:** ADM
