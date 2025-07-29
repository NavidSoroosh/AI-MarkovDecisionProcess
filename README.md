# Markov Decision Process Framework

This project implements a modular and extensible framework for solving **Markov Decision Processes (MDPs)** in **Java**. It includes core algorithms and advanced planning strategies such as:

- `AdHocIslandsPolicy` — A heuristic-based policy for exploration in structured environments.
- `BayesianBeliefUpdater` — A belief-state updater for partially observable environments.
- `ValueIteration` — The classic dynamic programming method to compute optimal policies.

---

## Core Components

### `AdHocIslandsPolicy.java`

- Implements a navigation and decision-making strategy tailored for "island" environments (e.g., partially explored or fragmented maps).
- Useful when the transition model is partially known or structured in clusters.
- Adapts between exploration and exploitation using configurable heuristics.

### `BayesianBeliefUpdater.java`

- Maintains and updates probability distributions over hidden states or models.
- Designed for **POMDPs** (Partially Observable Markov Decision Processes).
- Performs **Bayesian inference** upon receiving observations or feedback.

### `ValueIteration.java`

- Implements the **Value Iteration algorithm** using Bellman updates.
- Supports:
  - Discount factor (γ)
  - Convergence tolerance
  - Policy extraction after convergence
