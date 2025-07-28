package tp;

import java.util.*;

import control.MapPolicy;
import control.StationaryPolicy;
import mdp.*;

public class ValueIteration <S, A>
{

	private MDP<S, A> mdp;
	private Double gamma;
	private Double epsilon;
	private TransitionFunction<S, A> transitionFunction;
	private RewardFunction<S, A> rewardFunction;
	private HashMap<S, HashMap<A, Double>> Qmap = new HashMap<>();

	public ValueIteration(MDP<S, A> newMdp, Double newGamma, Double newEpsilon)
	{
		mdp = newMdp;
		gamma = newGamma;
		epsilon = newEpsilon;
		transitionFunction = mdp.getTransitionFunction();
		rewardFunction = mdp.getRewardFunction();
	}

	public HashMap<S, Double> backup(HashMap<S, Double> newVmap)
	{

		HashMap<S, Double> Vmap = newVmap;
		HashMap<A, Double> actionsRewards = new HashMap<>();

		for(S state : this.mdp.states()){

			for (A action : this.mdp.actions(state)){
				ProbabilityDistribution<S> probNextStates = transitionFunction.getNextStatesDistribution(state, action);
				List<S> statesStar = new ArrayList<>();
				statesStar.addAll(probNextStates.support());
				Double sum = 0d;

				for (S stateStar : statesStar){
					sum += transitionFunction.getTransitionProbability(state,action,stateStar)*Vmap.get(stateStar);

				//Double transition = transitionFunction.getTransitionProbability(state, action, nextState);
				Double reward = rewardFunction.getReward(state, action, stateStar);

				actionsRewards.put(action, reward + sum * gamma * newVmap.get(stateStar));//transition
				}
				Qmap.put(state, actionsRewards);

				Double maxValue = 0d;

				for (Map.Entry<A, Double> entry : Qmap.get(state).entrySet())
				{
					if (maxValue == 0 || entry.getValue().compareTo(maxValue) > 0)
					{
						maxValue = entry.getValue();
						actionsRewards.put(entry.getKey(), entry.getValue());
					}
				}

				Qmap.put(state, actionsRewards);
				Vmap.put(state, maxValue);
			}
		}

		return Vmap;
	}

	public StationaryPolicy<S, A> solve()
	{
		// faire des backups jusqu'à convergence
		HashMap<S, Double> vn0 = new HashMap<S, Double>();
		Iterator<S> states = this.mdp.states().iterator();
		while(states.hasNext()){
			vn0.put(states.next(),0.0);
		}

		HashMap<S, Double> vn1 = backup(vn0);

        while (valPolitic(vn0) - valPolitic(vn1) > this.epsilon){
            vn0 = vn1;
            vn1 = backup(vn0);
        }

		MapPolicy p = new MapPolicy();
		for (Map.Entry w : getPolitic().entrySet()){
			p.setAction(w.getKey(),w.getValue());
		}
		return p;
	}

	public Double valPolitic(Map<S, Double> map){
		Double sum = 0.0;
		for (Double d : map.values()) {
			sum += d;
		}
		return sum;
	}

	public Map<S, A> getPolitic(){
		Map<S, A> politic = new HashMap<S, A>();
		Iterator<S> states = this.mdp.states().iterator();
		while(states.hasNext()){
			S state = states.next();
			//System.out.println(Qmap.get(state).toString());
			System.out.println(Qmap.get(state).toString());
			A a = Collections.max(Qmap.get(state).entrySet(), Map.Entry.comparingByValue()).getKey();
			politic.put(state,a);
		}
		return politic;
	}
}
