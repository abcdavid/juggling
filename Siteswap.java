public class Siteswap
{
	private int throwHeights[];
	private State startState;

	Siteswap(int beats,State state)
	{
		throwHeights=new int[beats];
		startState=state;
	}
	public void setThrow(int pos,int throwHeight)
	{
		throwHeights[pos]=throwHeight;
	}
	public int getThrow(int pos)
	{
		return throwHeights[pos];
	}
	public State getStartState()
	{
		return startState;
	}
	public int getBallCount()
	{
		int i;
		int sum=0;
		for (i=0;i<throwHeights.length;i++)
			sum+=getThrow(i);
		sum/=throwHeights.length;
		return sum;
	}
	public int getMaxThrow()
	{
		int i,max;
		max=0;
		for (i=0;i<throwHeights.length;i++)
		{
			if (throwHeights[i]>max) max=throwHeights[i];
		}
		return max;
	}
	public int getBeats()
	{
		return throwHeights.length;
	}

	public int nextThrowPosition(int beat)
	{
		return (this.getThrow(beat)+beat)%this.getBeats();
	}

	public String toString()
	{
		String result;
		int i;
		result="";
		for (i=0;i<throwHeights.length;i++)
			result+=String.valueOf(throwHeights[i])+" ";
		return result;
	}
	public void print()
	{
		int i;
		for (i=0;i<throwHeights.length;i++)
		{
			System.out.print(throwHeights[i]+" ");
		}
		System.out.println();
	}
}
