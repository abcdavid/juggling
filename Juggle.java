import java.util.*;
import java.awt.*;

public class Juggle
{
// defines a juggle

// composed using siteswap, and paths of individual balls

	private Siteswap siteswap;
	private BallPath ballPath[];
	private int ballCount;
	private JuggleContext jContext;

	static private int lowestCommonDenominator(int a,int b)
	{
		int l,h,lcd;
		if(a==b) return a;
		if (a>b)
		{
			h=a;
			l=b;
		}
		else
		{
			h=b;
			l=a;
		}
		lcd=h;
		while (lcd%l!=0)
		{
			lcd+=h;
		}
		return lcd;
	}
	Juggle(Siteswap siteswap)
	{
		State state=siteswap.getStartState();
		this.siteswap=siteswap;
		this.ballCount=siteswap.getBallCount();
		jContext=new JuggleContext(siteswap.getMaxThrow());

		ballPath=new BallPath[ballCount];
		int i=0,ballNo=0;
		while (ballNo<ballCount)
		{
			if (state.getState(i))
			{
				ballPath[ballNo]=new BallPath(siteswap,ballNo,i,jContext);
				ballNo++;
			}
			i++;
		}
	}
	public Point getBallPosition(int ballNo,float beatsElapsed)
	{
		return ballPath[ballNo].getBallPosition(beatsElapsed);
	}
	public int getBallCount()
	{
		return ballCount;
	}
	public int getBeats()
	{
		int i;
		int lcd=1; // lowest common denominator
		for (i=0;i<(ballCount);i++)
			lcd=lowestCommonDenominator(ballPath[i].getBeats(),lcd);
		return lcd;
	}
	public JuggleContext juggleContext()
	{
		return jContext;
	}
}