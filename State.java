public class State
{
	private boolean state[];
	private int noStates;
	State(int states)
	{
		state=new boolean[states];
		noStates=states;
	}
	public boolean getState(int pos)
	{
		int newPos=pos%state.length;
		return state[newPos];
	}
	public void setState(int pos,boolean set)
	{
		if (pos<state.length)
			state[pos]=set;
		else System.out.println("Cannot set state");
	}
	public State copyState()
	{
		State state;
		int i;
		state=new State(noStates);
		for (i=0;i<noStates;i++)
			state.setState(i,getState(i));
		return state;
	}
	public State getStateBeatLater()
	{
		State s;
		int i;
		s=new State(noStates);
		for (i=0;i<(noStates-1);i++)
			s.setState(i,getState(i+1));
		s.setState(noStates-1,false);
		return s;
	}
	public int getLinkStrength(State s2) // link strength from this node
							// in direction of parameter node
{
		State s1,sDiff;
		int i,count=0;
		int throwVal=0;
		s1=this.getStateBeatLater();
		sDiff=xorState(s1,s2);
		for (i=0;i<noStates;i++)
			if (sDiff.getState(i)) {throwVal=i+1;count++;}
		if (count==1) return throwVal;
		if (count==0) return 0;		
		return -1;
	}
	private State xorState(State state1,State state2)
	{
		State s;
		int i;
		s=new State(noStates);
		for (i=0;i<noStates;i++)
		{
			if (state1.getState(i)!=state2.getState(i)) s.setState(i,true);
			else s.setState(i,false);
		}
		return s;
		
	}

	public void print()
	{
		int i;
		for (i=0;i<state.length;i++)
		{
			if (state[i]) System.out.print(" x ");
			else System.out.print(" _ ");
		}
		System.out.println();	
	}

}