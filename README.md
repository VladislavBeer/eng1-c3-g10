# ENG1 Cohort 3 Group 10

#### Goose Patrol

Private repository for our Engineering 1 (COM00019I) team project at the University of York.

**Project website:** ([https://github.com/VladislavBeer/eng1-c3-g10/](https://vladislavbeer.github.io/eng1-c3-g10/))

--- 
## Assessment 1 deliverables

| Deliverable                    | Location                      |
| ------------------------------ | ----------------------------- |
| Requirements                   | `docs/Req1.pdf`               |
| Architecture                   | `docs/Arch1.pdf`              |
| Method selection and planning  | `docs/Plan1.pdf`              |
| Risk assessment and mitigation | `docs/Risk1.pdf`              |
| Implementation report          | `docs/Impl1.pdf`              |
| Executable JAR                 | `releases/[Goose-Patrol].jar` |

## Running the game

Requires Java [17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html).

Launch the desktop launcher using
```main/lwjgl3/src/main/java/com/github/goose_patrol/lwjgl3/Lwjgl3Launcher.java```

To create an executable jar of your own, open Powershell, cd into the project root (eng1-c3-g10) and run:
```.\gradlew.bat lwjgl3:jar```

The runnable JAR will be created at:
```lwjgl3\build\libs\GoosePatrol-1.0.0.jar```

To run the JAR, use Powershell and paste the following:
```java -jar lwjgl3\build\libs\GoosePatrol-1.0.0.jar```

## Repository structure

- `src/`: game source code
- `docs/`: PDF deliverables, plus design evidence (interim diagrams, CRC cards, etc.)
- `assets/`: game assets and their license information

## Third-party libraries and assets

Listed with their licenses in `docs/Impl1.pdf`.

## Team

Cohort 3, Group 10: Vladislav Beer, Matthew Crossland, James Marchbank, Mani Keeble, Rob Pegrum, William George (LaFive), Dima Tarasiuk
