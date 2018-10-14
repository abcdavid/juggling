import java.awt.*;

public class BallFlight
{
	private int beats; // beats before thrown again
	private Point startPoint; // thrown from
	private Point catchPoint; // caught
	private BallFlight nextFlight;
	private JuggleContext jContext;

	BallFlight(int beats,Point startPoint,Point catchPoint,JuggleContext jContext)
	{
		this.beats=beats;
		this.startPoint=startPoint;
		this.catchPoint=catchPoint;
		this.jContext=jContext;
	}
	public void addFlight(BallFlight newFlight)
	{
		if (nextFlight!=null) nextFlight.addFlight(newFlight);
		else nextFlight=newFlight;
	}
	public Point getBallPosition(float beatsElapsed)
	{
		if (beatsElapsed>(float)beats) 
			return nextFlight.getBallPosition(beatsElapsed-(float)beats);
		boolean isAloft=true;
		float flightFraction=jContext.getFractionAirTime(); // proportion of time in air
		float airTime=flightFraction*(float)beats;
		float handTime=(1-flightFraction)*(float)beats;
		float beatsFlight;


		if (beatsElapsed>airTime) isAloft=false;

		if (isAloft) 
		{
			beatsFlight=(beatsElapsed/airTime);
			int x=jContext.getXPosition(startPoint.x,catchPoint.x,beatsFlight);
			float yFloat=jContext.getRelativeY(beatsFlight);
			float yHeight=jContext.getHeight(beats);
			int y=startPoint.y-(int)(yFloat*yHeight);
			return new Point(x,y);
		}	
		else
		{
			beatsFlight=(beatsElapsed-airTime)/handTime;
			int x=jContext.getXPosition(catchPoint.x,
							nextFlight.getStartPoint().x,beatsFlight);
			float yFloat=jContext.getRelativeY(beatsFlight);
			int yHeight=jContext.getCarryDepth();
			int y=catchPoint.y+(int)(yFloat*(float)yHeight);
			return new Point(x,y);
			
		}
	}
	public Point getStartPoint()
	{
		return startPoint;
	}
}
