package tp;

import bw.BWMap;
import bw.mdp.BWAction;
import bw.mdp.BWMDP;
import bw.mdp.BWState;
import control.Policy;

import java.util.*;

public class AdHocIslandsPolicy<S, A> implements Policy<BWState, BWAction>
{
	protected Random random;
	private BWAction action;
	private BWMap map;

	public AdHocIslandsPolicy(BWMDP mdp, Random rand, BWMap newMap)
	{
		this.random = rand;
		this.map = newMap;
		this.action = BWAction.EAST;
	}

	@Override
	public BWAction get(BWState state)
	{

		if (state.speed < 1 || (state.speed < 3  && state.type != BWMap.CellType.WATER))
		{
			return BWAction.ACCELERATE;
		}

		if (state.j + 1 == map.width)
		{
			return BWAction.SOUTH;
		}

		return action;
	}
}
