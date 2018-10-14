import java.awt.*;

public class BallPath
{
	private int ball;  // which ball
	private int startBeat; // first throw on this beat
	private BallFlight startFlight;
	//private BallFlight ballFlight[];
	private JuggleContext jContext;
	private int beats;  // number of beats before path repeats


	BallPath(Siteswap siteswap,int ball,int startBeat,JuggleContext jContext)
	{
		this.jContext=jContext;
		this.ball=ball;
		this.startBeat=startBeat;

// generate default flights (flight=throw + carry)
		int siteswapIndex=startBeat%siteswap.getBeats();
		int startIndex=siteswapIndex;
		int throwHeight;
		boolean isLeftHandThrow=false;
		if (startBeat%2==0) isLeftHandThrow=true; // initially thrown from left or right?
		boolean isLeftHandStart=isLeftHandThrow;
		int beatCount=0; // count of beats of path

		do
		{
			throwHeight=siteswap.getThrow(siteswapIndex);
			beatCount+=throwHeight;
			addFlight(throwHeight,isLeftHandThrow);
			if (throwHeight%2==1) isLeftHandThrow=!isLeftHandThrow;
			siteswapIndex=siteswap.nextThrowPosition(siteswapIndex);
		} while (!(siteswapIndex==startIndex && isLeftHandThrow==isLeftHandStart));
			addFlight(throwHeight,isLeftHandThrow); // link to start throw
			this.beats=beatCount; // set repeat value
	}
	private void addFlight(int height,boolean leftHandThrow)
	{
		Point pos1,pos2;
		if (leftHandThrow)
		{
			pos1=jContext.leftHandThrowPos();
			if (height%2==1)
			{
				pos2=jContext.rightHandCatchPos();
			}
			else
			{
				pos2=jContext.leftHandCatchPos();
			}
		}
		else
		{
			pos1=jContext.rightHandThrowPos();
			if (height%2==1)
			{
				pos2=jContext.leftHandCatchPos();
			}
			else
			{
				pos2=jContext.rightHandCatchPos();
			}
		}
		if (startFlight==null)
		startFlight=new BallFlight(height,pos1,pos2,jContext);
		else startFlight.addFlight(new BallFlight(height,pos1,pos2,jContext));
	}
	public Point getBallPosition(float beatsElapsed)
	{
		float beatsToDo=(beatsElapsed-startBeat)%(float)beats;

		if (beatsToDo<0) return (Point) null;
		return startFlight.getBallPosition(beatsToDo);
	}
	public int getBeats()
	{
		return beats;
	}

}