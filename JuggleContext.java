import java.util.*;
import java.awt.*;
// data for the appearance of a juggle

public class JuggleContext
{
	// defaults
	private int imageWidth=300;
	private int imageHeight=300;

	private int maxThrowHeight=180;
        private int maxCarryDepth=60;
	private int maxBeats=5; // highest throw of pattern

	private int ballSize=30;

	Vector launchPositions;
	Vector landingPositions;

	float fractionAirTime=0.66f;

	//Vector flightStyles;

	// default positions
	//	private Point launchPosL,launchPosR,landingPosR,landingPosL;
                private Point launchPosL=new Point(135,190);
                private Point landingPosR=new Point(250,190);
                private Point launchPosR=new Point(165,190);
                private Point landingPosL=new Point(50,190);


		JuggleContext(int maxBeats)
		{
		this.maxBeats=maxBeats;
// default juggling pattern
		// throw/catch positions as cascade
		launchPositions=new Vector(2);
		landingPositions=new Vector(2);
		launchPositions.addElement(launchPosL);
		launchPositions.addElement(launchPosR);
		landingPositions.addElement(landingPosR);
		landingPositions.addElement(landingPosL);
		}
		public int getImageWidth()
		{
			return imageWidth;
		}
		public int getImageHeight()
		{
			return imageHeight;
		}
		public int getBallSize()
		{
			return ballSize;
		}
		public Point leftHandThrowPos()
		{
			return launchPosL;
		}	
		public Point rightHandThrowPos()
		{
			return launchPosR;
		}
		public Point rightHandCatchPos()
		{
			return landingPosR;
		}
		public Point leftHandCatchPos()
		{
			return landingPosL;
		}
		public int getXPosition(int startX,int endX,
								float fractionElapsed)
		{
			int xDifference=endX-startX;
			return (int)(fractionElapsed*(float)xDifference)+startX;
		}
		public float getRelativeY(float fractionElapsed)
		{
		// returns fraction of maximum height
			return 4f*fractionElapsed*(1f-fractionElapsed);
		}
		public float getHeight(int throwBeats)
		{
			float timeFraction=(float)throwBeats/(float)maxBeats;
			return (float)maxThrowHeight*(float)(timeFraction*timeFraction);
		}
		public float getFractionAirTime()
		{
			return fractionAirTime;
		}
		public int getCarryDepth()
		{
			return maxCarryDepth;
		}
}
