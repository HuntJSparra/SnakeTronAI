# SnakeTronAI
A collection of A.I. implementations, created by TeamiumPremium, for a competitive variant of Snake.
For the course: Artificial Intelligence Through Machine Game Playing, Spring 2021.

Yes, we know the name "TeamiumPremium" is ~~terrible~~ great. It keeps things light.

## Structure
**TeamiumPremium**: Directory containing the AI implementations and associated code (helper classes).

**AI-Game-Playing**: Directory containing the Snake environment, provided by https://github.com/mlepinski/AI-Game-Playing (*main* branch).

## Dependencies
* Neuroph
* Deep Learning 4 Java (beta7)
* JavaFX

## A.I. Players
**Claustrophobium**: AI that chooses a random move in an empty or food space (i.e., no snakes). This is the *best* performing A.I.

**NeuroPhremiumPlayer**: Deep learning network using Neuroph. It was abandon due to issues getting Neurph to train multiple games.

**DeepQiumPlayer**: Deep-Q network implemented in Deep Learning 4 Java. Random moves use Claustrophobium to select a "safe" move. It learns to move north.

**ConvolutiumPlayer**: Same as DeepQium, but uses a few convolution layers. It learns to move north.

*A note on encoding...* for neural networks, spaces are one-hot encoded as empty, enemy snake body, enemy snake head, self head, self body, or food (6 total).

## AI-Game-Playing Subtree
The Snake environment is handled using *git subtree* (as opposed to *submodule*). *Subtree* was chosen over *submodule* for usability when the Snake environment is updated. *Subtree* simply requires the repository to be updated. *Submodule* requires all contributors to update, making user-error likely.

*Subtree* also allows the repository to be cloned without extra commands.

**The subtree can be manually updated with**:
`git subtree pull --prefix AI-Game-Playing https://github.com/mlepinski/AI-Game-Playing.git main --squash`

*Time permitting, I hope to replace this manual update with a GitHub Action.*
