# dungeon_chaos
Dungeon Chaos is a roguelike dungeon-crawler game developed in Java.
=======
# Dungeon Chaos

## Project Overview
**Dungeon Chaos** is a **roguelike dungeon-crawler game** developed in Java. Players explore procedurally generated labyrinths, battle various monsters, and collect items to enhance their abilities. The game follows the **Model-View-Controller (MVC)** design pattern, ensuring a clean and modular structure.

## Features
- **Procedural Dungeon Generation**
  - Randomly generated labyrinths with destructible and indestructible blocks.
  - Doors leading to new levels as the player progresses.
- **Combat System**
  - Multiple enemy types with unique behaviors (ghosts launching bombs, skeletons sending arrows, pirates with a sword and guiding parrot thieves...).
  - Hand-to-hand combat, ranged attacks (arrows), and bombs.
  - Special spells to summon monsters to assist the player.
  - Critical hits and damage effects.
- **Inventory & Items**
  - Various collectible items: potions, weapons, and power-ups.
  - Inventory system with limited slots.
  - Healing potions, damage-boosting potions, and time-stopping potions.
- **Artificial Intelligence**
  - Enemies react to the player's movements and attempt to avoid obstacles.
  - Some monsters can steal items from the player.
- **Graphical Interface**
  - Fully animated player, monsters, and attacks.
  - Pop-up notifications when damage is taken or critical hits occur.
- **Save & Load System**
  - Saves game progress using serialization.
  - Load the last saved game upon restart.
- **Dynamic Difficulty**
  - Enemies get tougher as the player progresses.

## Installation
### Dependencies
Ensure you have the following installed:
- **Java Development Kit (JDK 8 or later)**
- **Maven** (optional, for build automation)

### Project Structure
```
DungeonChaos/
│── src/            # Java source files (organized in MVC pattern)
│   ├── Controller/
│   ├── Main/
│   │   ├── Main.java  # Entry point of the game
│   ├── Model/
│   ├── View/
│── res/            # Sprites and game assets
│── README.md       # Project documentation
│── LICENSE         # License file (if applicable)
│── .gitignore      # Ignore compiled files and IDE settings
```

### Compilation & Execution
To compile the game manually:
```sh
javac -d bin -sourcepath src src/Main/Main.java
```

To run the game:
```sh
java -cp bin Main.Main
```

If using **Maven**:
```sh
mvn package
java -jar target/dungeon-chaos.jar
```

## Controls
- **Movement:** Arrow keys
- **Attack (Melee):** Spacebar
- **Attack (Ranged):** Left-click
- **Use Bomb:** B
- **Use Potion:** P
- **Pause Game:** Esc

## License
This project is released under the **GNU General Public License v3.0 (GPLv3) with a Non-Commercial Clause**. The game code can be modified and shared, but it **may not be used for commercial purposes** without explicit written permission from the authors.

## Authors
- **Quentin Gontier**
- **Bill Ndihokubwayo**
