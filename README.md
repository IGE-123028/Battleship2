# ⚓ Battleship 2.0

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![Java Version](https://img.shields.io/badge/Java-17%2B-blue)
![License](https://img.shields.io/badge/license-MIT-green)

> A modern take on the classic naval warfare game, designed for the XVII century setting with updated software engineering patterns.

---

## 📖 Table of Contents
- [Project Overview](#-project-overview)
- [Key Features](#-key-features)
- [Technical Stack](#-technical-stack)
- [Installation & Setup](#-installation--setup)
- [Code Architecture](#-code-architecture)
- [Roadmap](#-roadmap)
- [Contributing](#-contributing)

---

## 🎯 Project Overview
This project serves as a template and reference for students learning **Object-Oriented Programming (OOP)** and **Software Quality**. It simulates a battleship environment where players must strategically place ships and sink the enemy fleet.

### 🎮 The Rules
The game is played on a grid (typically 10x10). The coordinate system is defined as:

$$(x, y) \in \{0, \dots, 9\} \times \{0, \dots, 9\}$$

Hits are calculated based on the intersection of the shot vector and the ship's bounding box.

---

## ✨ Key Features
| Feature | Description | Status |
| :--- | :--- | :---: |
| **Grid System** | Flexible $N \times N$ board generation. | ✅ |
| **Ship Varieties** | Galleons, Frigates, and Brigantines (XVII Century theme). | ✅ |
| **AI Opponent** | Heuristic-based targeting system. | 🚧 |
| **Network Play** | Socket-based multiplayer. | ❌ |

---

## 🛠 Technical Stack
* **Language:** Java 17
* **Build Tool:** Maven / Gradle
* **Testing:** JUnit 5
* **Logging:** Log4j2

---

## 🚀 Installation & Setup

### Prerequisites
* JDK 17 or higher
* Git

### Step-by-Step
1. **Clone the repository:**
   ```bash
   git clone [https://github.com/britoeabreu/Battleship2.git](https://github.com/britoeabreu/Battleship2.git)
   ```
2. **Navigate to directory:**
   ```bash
   cd Battleship2
   ```
3. **Compile and Run:**
   ```bash
   javac Main.java && java Main
   ```

---

## 📚 Documentation

You can access the generated Javadoc here:

👉 [Battleship2 API Documentation](https://britoeabreu.github.io/Battleship2/)


### Core Logic
```java
public class Ship {
    private String name;
    private int size;
    private boolean isSunk;

    // TODO: Implement damage logic
    public void hit() {
        // Implementation here
    }
}
```

### Design Patterns Used:
- **Strategy Pattern:** For different AI difficulty levels.
- **Observer Pattern:** To update the UI when a ship is hit.
</details>

### Logic Flow
```mermaid
graph TD
    A[Start Game] --> B{Place Ships}
    B --> C[Player Turn]
    C --> D[Target Coordinate]
    D --> E{Hit or Miss?}
    E -- Hit --> F[Check if Sunk]
    E -- Miss --> G[AI Turn]
    F --> G
    G --> C
```

---

## 🗺 Roadmap
- [x] Basic grid implementation
- [x] Ship placement validation
- [ ] Add sound effects (SFX)
- [ ] Implement "Fog of War" mechanic
- [ ] **Multiplayer Integration** (High Priority)

---

## Final LLM Prompt

Considere agora a seguinte tática de geração de rajadas de tiros.
- Crie um Diário de Bordo com o registo de cada rajada disparada, numerando-as sequencialmente (Rajada 1, 2, 3...). Guarde as coordenadas exatas de cada tiro e o
respetivo resultado (Água, Nau atingida, Barca afundada, etc.). A memória é a principal arma de um bom estratega.
- Não dispare fora dos limites do mapa (ex: Z99) nem repita tiros em coordenadas
já testadas. A única exceção para este desperdício de pólvora é a última rajada do
jogo, apenas para perfazer os 3 tiros obrigatórios quando a frota inimiga já estiver
irremediavelmente no fundo do mar.
- Se atingir um navio numa rajada, dispare nas posições contíguas (Norte, Sul, Este,
Oeste) na jogada seguinte para descobrir a orientação da embarcação e acabar de a
afundar. No entanto, se a rajada anterior confirmar que o navio já foi afundado, não
dispare para as posições contíguas, pois os navios nunca estão encostados.
- Como as Caravelas, Naus e Fragatas são linhas retas, um tiro certeiro significa que o
resto do navio está na horizontal ou na vertical. Como os navios não se podem tocar
(nem sequer nos cantos), as posições diagonais a um tiro certeiro são garantidamente
água (a única exceção é o corpo do Galeão, devido à sua forma em T). Evitar estas
diagonais poupa imensos tiros.
- É mais provável que os navios estejam mais perto do centro porque oferece mais opções para a colocação dos navios. Por isso, parece um bom sítio para começar a bombardear.
- Vencer não é sobre destruir todos os navios o mais rápido possível, mas sim sobre conseguir concretizar o objetivo com o menor número de tiros.
- Disparar muitos tiros juntos, faz com que dispares mais tiros do que precisas para destruir todos os navios. Portanto, não caias na armadilha de seguir um padrão de xadrez ou seguir uma linha vertical/horizontal/diagonal tiro sim, tiro não. Os bons jogadores posicionam os navios de forma a que este tipo de estratégias falhe mais vezes do que tem sucesso.
- Não adotes um estilo de jogo demasiado metódico, desta forma tornas-te previsível e os teus adversários acabam por esconder os navios nas posição que tendes a atingir mais tarde.
- Quando o relatório de uma rajada confirmar que um navio foi afundado (ex: Fragata de
4 posições), analise os dados do seu Diário de Bordo para identificar exatamente onde
caíram esses 4 tiros. Confirmada a posição exata da carcaça, marque todas as quadrículas adjacentes (o halo de 1 posição em redor do navio) como água intransitável.
É impossível haver outra embarcação nesse perímetro.
- Divide as posições em 0 e 1 alternadamente.
- Divide o tabuleiro em 4 quadrantes.
- Começa perto do meio de um quadrante e escolhe 0 ou 1.
- Continua a atingir espaços que apenas correspondam ao número inicialmente conhecido, mas desfasando cada chamada em 3 espaços numa direção, depois um espaço ortogonalmente.
- Continua a mover-te num padrão em espiral. (ex: D3, G4, F7, C6). Isso vai fazer um retângulo
- Depois desfasas esse retângulo em 2 na diagonal (ex: F1, I2, H5, E4). Continua esse padrão. Podes ajustar a forma como usas o teu padrão.
- Não precisa de ser sempre em forma de retângulo, mas essencialmente estás a cortar faixas para que elimines a maioria dos possíveis esconderijos para a maioria dos navios.
- Não te preocupes em acabar com um acerto, continua com o teu padrão até teres presumivelmente encontrado pelo menos três navios.
- Se a sua frota for toda afundada, declare a derrota com honra. Em contrapartida, seja
um vencedor magnânimo se for o inimigo a render-se com os navios todos no fundo
do oceano!

---

## 🧪 Testing
We use high-coverage unit testing to ensure game stability. Run tests using:
```bash
mvn test
```

> [!TIP]
> Use the `-Dtest=ClassName` flag to run specific test suites during development.

---

## 🤝 Contributing
Contributions are what make the open-source community such an amazing place to learn, inspire, and create.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a **Pull Request**

---

## 📄 License
Distributed under the MIT License. See `LICENSE` for more information.

---
**Maintained by:** [@britoeabreu](https://github.com/britoeabreu)  
*Created for the Software Engineering students at ISCTE-IUL.*
