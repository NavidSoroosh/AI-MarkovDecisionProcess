package tp;

import control.*;
import bw.mdp.*;
import bw.*;
import mdp.*;

import java.util.*;

public class ValueIterationV2<S,A>{

    MDP<S,A> mdp;
    double gamma;
    double epsilon;
    TransitionFunction<S,A> transitions;
    RewardFunction<S,A> Rewards;
    Map<S, A> politic = null;

    public ValueIterationV2(MDP<S,A> mdp, double gamma, double epsilon){
        this.mdp=mdp;
        this.Rewards = this.mdp.getRewardFunction();
        this.transitions = this.mdp.getTransitionFunction();
        this.gamma=gamma;
        this.epsilon=epsilon;
    }

    public StationaryPolicy solve(){
        //faire des backups jusqu'a convergence
        Map<S, Double> Vnar = initVnar();
        Map<S, Double> Vnar2 = backup(Vnar);
        while(valPolitic(Vnar2) - valPolitic(Vnar) > this.epsilon){
        //while(Math.abs(valPolitic(Vnar2) - valPolitic(Vnar)) > this.epsilon){
            Vnar=Vnar2;
            Vnar2 = backup(Vnar2);
        }
        this.politic=getPolitic(Vnar);
        MapPolicy p = new MapPolicy();
        for (Map.Entry w : this.politic.entrySet()){
            p.setAction(w.getKey(),w.getValue());
        }
        return p;
    }

    public Map<S, Double>backup(Map<S, Double>Vnar){
        Map<S, Map<A, Double>> Qnar = QnarMap(Vnar);

        Map<S, Double> Vnar2 = new HashMap<S, Double>();
        for(S state : this.mdp.states()){
            Double valMax = Collections.max(Qnar.get(state).entrySet(), Map.Entry.comparingByValue()).getValue();
            Vnar2.put(state,valMax);
        }
        return Vnar2;
    }

    public Map<S, Map<A, Double>> QnarMap(Map<S, Double>Vnar){
        Map<S, Map<A, Double>> Qnar = new HashMap<S, Map<A, Double>>();
        for(S state : this.mdp.states()){
            Map<A, Double> Qpart = new HashMap<A, Double>();
            for(A action : this.mdp.actions(state)){
                ProbabilityDistribution<S> prob = this.transitions.getNextStatesDistribution(state, action);
                Double sum = 0.0;
                for(S stateStar : prob.support()){
                    Double valVnar = Vnar.get(stateStar);
                    sum += this.transitions.getTransitionProbability(state,action,stateStar)*(this.Rewards.getReward(state,action,stateStar)+this.gamma*valVnar);
                }
                Qpart.put(action,sum);
            }
            Qnar.put(state,Qpart);
        }
        return Qnar;
    }

    public Double valPolitic(Map<S, Double> map){
        Double sum = 0.0;
        for (Double d : map.values()) {
            sum += d;
        }
        return sum;
    }

    public Map<S, Double> initVnar(){
        Map<S, Double> Vnar = new HashMap<S, Double>();
        Iterator<S> states = this.mdp.states().iterator();
        while(states.hasNext()){
            S state = states.next();
            Vnar.put(state,0.0);
        }
        return Vnar;
    }

    public Map<S, A> getPolitic(Map<S, Double>Vnar){
        Map<S, Map<A, Double>> Qnar = QnarMap(Vnar);

        Map<S, A> politic = new HashMap<S, A>();
        Iterator<S> states = this.mdp.states().iterator();
        while(states.hasNext()){
            S state = states.next();
            A a = Collections.max(Qnar.get(state).entrySet(), Map.Entry.comparingByValue()).getKey();
            politic.put(state,a);
        }
        return politic;
    }

}