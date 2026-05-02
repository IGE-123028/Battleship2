# Refactoring Opportunities

| Local | Code smell | Refactoring | Student ID |
|---|---|---|---|
| Tasks::menu | Long Method / God Object | Extract Class / Extract Method (separate CLI from game logic) | IGE-123010 |
| Move::processEnemyFire | Long Method / Mixed Concerns (logic + formatting + I/O) | Extract Service / Convert to DTO (separate logic and serialization) | IGE-123010 |
| Caravel::Caravel | Duplicate Code (repeated placement logic) | Extract Method / Strategy for placement | IGE-123010 |
| Ship::buildShip | Primitive Obsession / Null return | Replace Type Code with Enum; throw IllegalArgumentException for unknown type | IGE-123010 |
| Compass::charToCompass | Switch Statement | Replace Conditional with Polymorphism / Factory | IGE-123010 |
| BoardColor::colored | Presentation in domain (ANSI dependency) | Move presentation code to UI layer | IGE-123010 |
| Position::equals/hashCode | Inconsistent Contract (hashCode uses mutable fields) | Fix equals/hashCode (use only immutable row and column) | IGE-123016 |
| Game::randomEnemyFire | Complex Logic / Inefficient | Simplify algorithm; use ThreadLocalRandom; use Set for used positions | IGE-123016 |
| Game::fireShots | Duplicate Logic | Extract Method (easy): extract alreadyShot handling into helper | IGE-123016 |
| Fleet::createRandom | Primitive Obsession (string literals for ship types) | Use Enum for ship types; centralize configuration | IGE-123016 |
| Messages::static init | Static initializer performs IO / file path usage | Load resources from classpath; remove heavy IO in static init | IGE-123016 |
| LLMService::cleanJsonResponse | Fragile parsing / Feature Envy | Move parsing to robust JSON util (Jackson) and validate output | IGE-123016 |
| HuggingFaceClient::chat | Broad exception handling / No timeouts | Introduce timeouts/retries; use DTOs and specific exceptions | IGE-123023 |
| PDFExporter::exportGameToPDF | Large Method / Mixed Concerns | Extract header/stats/table into methods; inject dependencies | IGE-123023 |
| Scoreboard::saveResult | Swallowed IO exceptions / Hard-coded path | Use configurable path; log errors; do not swallow exceptions | IGE-123023 |
| Game::printBoard | Inappropriate Intimacy | Encapsulate Ship internals; introduce query APIs | IGE-123023 |
| Frigate::Frigate | Duplicate Code (repeated placement logic) | Extract Method / Strategy for placement | IGE-123023 |
| Messages::get | Middle Man | Simplify API / Remove middle man | IGE-123023 |
| IMove::/Move:: | Data Class | Extract behavior to service / Convert to DTO | IGE-123028 |
| Game::repeatedShot / myRepeatedShot | Message Chains (move.getShots().contains(...)) | Encapsulate query (e.g., Move.hasShot) | IGE-123028 |
| Carrack::Carrack | Duplicate Code (repeated placement logic) | Extract Method / Strategy for placement | IGE-123028 |
| Game::randomEnemyFire (temp) | Temporary Field | Remove temporary field reuse; redesign loop | IGE-123028 |
| BoardColor (class) | Lazy Class | Move to UI module; remove domain dependency | IGE-123028 |
| Fleet::printStatus | Commented-out dead code | Remove commented code or implement as needed | IGE-123028 |

Author: ADM
