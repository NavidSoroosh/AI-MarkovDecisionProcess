package tp;

import mdp.*;

import java.util.HashMap;
import java.util.Map;

public class BayesianBeliefUpdater<S, A, O> implements BeliefUpdater<S, A, O> {

	private POMDP<S, A, O> pomdp;
	private TransitionFunction<S, A> transitionFunction;
	private ObservationFunction<S, A, O> observationFunction;
	
	public BayesianBeliefUpdater(POMDP<S, A, O> newPomdp) {
		pomdp = newPomdp;
		transitionFunction = pomdp.getTransitionFunction();
		observationFunction = pomdp.getObservationFunction();
	}
	
	@Override
	public ProbabilityDistribution<S> updatedBelief(ProbabilityDistribution<S> beliefState, A action, O observation) {
		S previousState = null;
		Double total = 0d;
		HashMap<S, Double> probabilities = new HashMap<>();
		/*
		for (S state : pomdp.states()) {
			double sumColonne = 0;
			double nbLignes = 0;

			for (S nextState : pomdp.states()) {
				previousState = nextState;
				Double transition = transitionFunction.getTransitionProbability(state, action, nextState);
				Double observationProbability = observationFunction.observations(state, action, nextState).get(observation);
				double interieur = beliefState.get(state) * transition * observationProbability;
				nbLignes +=1 ;
				probabilities.put(nextState, probabilities.get(nextState) + interieur);
			}
			probabilities.put(previousState, probabilities.get(previousState) / nbLignes);
			nbLignes = 0;
		}*/

		for (S statePrime : pomdp.states()) {

			double sum = 0.0;
			for (S state : beliefState.support()) {
				Double transition = transitionFunction.getTransitionProbability(state, action, statePrime);
				Double observationProbability = observationFunction.observations(state, action, statePrime).get(observation);
				sum += beliefState.get(statePrime) * transition * observationProbability;
			}
			probabilities.put(statePrime, sum);
			total += sum;
		}

		HashMap<S, Double> probUpdated = new HashMap<>();

		for (Map.Entry bs : probabilities.entrySet()) {
			System.out.println(bs.getKey().toString() + bs.getValue().toString());
			probUpdated.put((S)bs.getKey(), (Double)bs.getValue()/total);
		}
		
		return ProbabilityDistribution.fromMap(probUpdated);
	}

}
