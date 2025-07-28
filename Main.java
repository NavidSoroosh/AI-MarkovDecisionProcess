package tp;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import algorithms.BWNaiveBeliefUpdater;
import bw.BWArrowController;
import bw.BWMap;
import bw.BWPOSimulation;
import bw.BWSimulation;
import bw.maps.IslandsBWMap;
import bw.maps.SeveralBridgesBWMap;
import bw.maps.TinyBWMap;
import bw.mdp.*;
import bw.visualization.BWPolicyViewer;
import control.NonStationaryRandomPolicy;
import control.Policy;
import control.PolicyBasedAgentController;
import control.StationaryPolicy;
import mdp.BeliefUpdater;
import mdp.ProbabilityDistribution;
import simulation.Agent;
import toyproblems.TinyMDP;

public class Main {

	public static void main(String[] args) {
		// Problem definition
		System.out.print("Building problem...");
		System.out.flush();
		BWMap map = new TinyBWMap();
		int maxSpeed = 3;
		BWPOMDP pomdp = BWPOMDP.makeStandardProblem(map, maxSpeed);
		System.out.println(" done.");

// Agent definition, at a random initial state
		Random rand = new Random();
		int row = rand.nextInt(map.height);
		int col = rand.nextInt(map.width);
		BWState initialState = pomdp.stateFactory.getState(row, col, 0);
		Agent<BWState> agent = new Agent<>(initialState);
		System.out.println("Agent created at initial state " + initialState);

// Belief state management
		BeliefUpdater<BWState, BWAction, BWObservation> updater = new BayesianBeliefUpdater<>(pomdp);
		Set<BWState> states = new HashSet<>();
		for (BWState state : pomdp.states()) {
			states.add(state);
		}
		ProbabilityDistribution<BWState> initialBeliefState = ProbabilityDistribution.uniform(states);

// Simulation
		System.out.println("Running simulation forever...");
		BWPOSimulation simu = new BWPOSimulation(pomdp);
		BWArrowController agentController = new BWArrowController();
		simu.addAgent(agent, agentController, updater, initialBeliefState);
		int sleep = 200;
		simu.runWithListeners(sleep, agentController);
/*
		double value = 5;
		double gamma = 0.9;
		double epsilon = 0.1;
		TinyMDP tinyMDP = new TinyMDP(value);

		ValueIteration<String, String> solver = new ValueIteration<>(tinyMDP, gamma, epsilon);
		StationaryPolicy<String, String> policy = solver.solve();

		boolean ok = true;
		for (String state : tinyMDP.states()) {
			Set<String> optimalActions = tinyMDP.optimalActions(state, gamma);
			String computedAction = policy.get(state);
			if (!optimalActions.contains(computedAction)) {
				System.out.println("ERROR: VISolver computed action " + computedAction);
				System.out.println(
						"Problem is TinyMDP with value " + value + ", gamma is " + gamma + ", epsilon is " + epsilon);
				System.out.println("True set of optimal actions is " + optimalActions);
				ok = false;
				break;
			}
		}

		if (ok) {
			System.out.println("Test passed");
		}

		/*
		Random rand = new Random();

		// Problem definition
		System.out.print("Building problem...");
		System.out.flush();
		//BWMap map = new SeveralBridgesBWMap();
		BWMap map = new IslandsBWMap();
		int maxSpeed = 3;
		BWMDP mdp = BWMDP.makeStandardProblem(map, maxSpeed);
		System.out.println(" done.");

		// Policy computation
		System.out.print("Building policy...");
		System.out.flush();
		//StationaryPolicy<BWState, BWAction> policy = new AdHocIslandsPolicy<>(mdp, rand, map);
		ValueIteration<BWState, BWAction> bwSolver = new ValueIteration<BWState, BWAction>(mdp, gamma, epsilon);
		StationaryPolicy<BWState, BWAction> bwPolicy = bwSolver.solve();
		System.out.println(" done.");

		// Agent definition, at a random initial state      
		int row = rand.nextInt(map.height);
		int col = rand.nextInt(map.width);
		BWState initialState = mdp.stateFactory.getState(row, col, 0);
		Agent<BWState> agent = new Agent<>(initialState);
		System.out.println("Agent created at initial state " + initialState);

		// Policy visualization
		for (int speed = 0; speed <= maxSpeed; speed++) {
			System.out.print("Displaying policy at speed " + speed);
			System.out.flush();
			BWPolicyViewer viewer = new BWPolicyViewer(mdp, bwPolicy, speed);
			viewer.showFrame("Bridge world policy at speed " + speed);
			System.out.println(" done.");
		}

		// Simulation
		System.out.println("Running simulation forever...");
		BWSimulation simu = new BWSimulation(mdp);
		simu.addAgent(agent, new PolicyBasedAgentController<BWState, BWAction>(bwPolicy, initialState));
		int sleep = 200;
		simu.run(sleep);
		*/

	}

}
