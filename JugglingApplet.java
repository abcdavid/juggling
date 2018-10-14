// finds possible siteswaps for juggling patterns
import java.applet.*;
import java.awt.*;
import java.util.*;

public class JugglingApplet extends Applet implements Runnable
{
	Vector siteswaps;

//gui components
	private JuggleOptionsPanel optionsPanel;
        private Panel controlPanel,imagePanel,buttonPanel,scrollPanel;
	private List siteswapOutput;
	private Button generateButton,animateButton;
        private Scrollbar delayBar;
        private Label slowLabel,fastLabel;
// animation instance variables
	private Juggle juggle;
	private JuggleContext jContext;
	private float beatsElapsed;
        private Image image,ballImage;
        private Graphics gContext;
	private Thread animate;
        private static int sleepTime=40;
	private int imagesPerBeat=5;
	private int imageCount;
	private int repeatBeats;
	private int currentImage=0;
	private int width,height,ballSize;
        private static String ballImageName="ball.jpg";

	private int balls=3,beats=5;
	private Point ballPosition[][];

	private static Color color[]={Color.red,Color.green,Color.blue,Color.cyan,Color.magenta,Color.yellow};
        public String getAppletInfo()
        {
                return "Written by David Harvey\n david.harvey@pemail.net";
        }

	public void init()
	{
		int i;

		imagePanel=new Panel();
		controlPanel=new Panel();
		buttonPanel=new Panel();
                scrollPanel=new Panel();

		imagePanel.resize(300,300);

		siteswapOutput=new List(12,false);
		generateButton=new Button("Options...");
		animateButton=new Button("Animate");
                delayBar=new Scrollbar(Scrollbar.HORIZONTAL,25,1,1,150);
                slowLabel=new Label("Slow  ",Label.RIGHT);
                fastLabel=new Label("Fast",Label.LEFT);
		optionsPanel=new JuggleOptionsPanel(this,balls,beats);

                setBackground(Color.white);
		setLayout(new BorderLayout());
		add("West",imagePanel);
		add("East",controlPanel);
		controlPanel.setLayout(new BorderLayout());
                controlPanel.add("North",scrollPanel);
		controlPanel.add("West",siteswapOutput);
		controlPanel.add("East",buttonPanel);
		controlPanel.add("South",optionsPanel);
		optionsPanel.hide();
		buttonPanel.add(animateButton);
		buttonPanel.add(generateButton);
                scrollPanel.setLayout(new GridLayout(1,3));
                scrollPanel.add(slowLabel);
                scrollPanel.add(delayBar);
                scrollPanel.add(fastLabel);

		generateSiteswaps(balls,7,beats);
		siteswapOutput.select(0);
		siteswapOutput.makeVisible(0);

//                MediaTracker tracker=new MediaTracker(this);
//                ballImage=getImage(getCodeBase(),JugglingApplet.ballImageName);
//                tracker.addImage(ballImage,0);
                ballImage=createImage(50,50);
                Graphics g=ballImage.getGraphics();
                g.setColor(Color.red);
                g.fillOval(0,0,50,50);
//                imagePanel.getGraphics().drawImage(ballImage,50,50,ballImage.getWidth(this),ballImage.getHeight(this),this);
		animationInit((Siteswap)siteswaps.elementAt(0));
	}
	private void animationInit(Siteswap siteswap)
	{

		int ball,i;
		// tidy up
		juggle=null;
		jContext=null;
		ballPosition=null;
		image=null;

		juggle=new Juggle(siteswap);
		jContext=juggle.juggleContext();
		repeatBeats=juggle.getBeats();
		beatsElapsed=(float)repeatBeats;
		imageCount=imagesPerBeat*repeatBeats;
	
		width=jContext.getImageWidth();
		height=jContext.getImageHeight();
		ballSize=jContext.getBallSize();
		
		ballPosition=new Point[juggle.getBallCount()][imageCount];

		showStatus("Initialising animation...");		

		image=createImage(width,height);
		gContext=image.getGraphics();
                gContext.setColor(Color.white);
		gContext.fillRect(0,0,width,height);

		// get ball positions

		float beatsIncrement=(float)repeatBeats/(float)imageCount;
		for (i=0;i<imageCount;i++)
		{

			for (ball=0;ball<juggle.getBallCount();ball++)
			{
				ballPosition[ball][i]=juggle.getBallPosition(ball,beatsElapsed);
			}
			beatsElapsed+=beatsIncrement;
		}
		postEvent(new Event(this,Event.MOUSE_ENTER, ""));
		showStatus("Animation initialised");		


	}
	public void generateSiteswaps(int balls,int maxHeight,int beats)
	{
			SiteswapNetwork ssn;
			Siteswap siteswap;
			int i;

			this.balls=balls;
			this.beats=beats;
			busy();
			showStatus("Generating siteswaps...");		

			siteswapOutput.clear();
			if (balls>=maxHeight) return;
			ssn=new SiteswapNetwork(balls,maxHeight);
			showStatus("Generating juggling states...");
			ssn.generateStateNodes();
			showStatus("Forming links between states...");
			ssn.formLinks();
			showStatus("Finding Siteswaps...");
			siteswaps=ssn.findSiteswaps(beats);
			for (i=0;i<siteswaps.size();i++)
			{
				siteswap=(Siteswap)siteswaps.elementAt(i);
				siteswapOutput.addItem(siteswap.toString());
			}
			showStatus("Siteswaps found");
			ssn=null;
			endBusy();

	}
	public void busy()
	{
		animateButton.disable();
		generateButton.disable();
	}
	public void endBusy()
	{
		animateButton.enable();
		generateButton.enable();
	}
        public boolean handleEvent(Event event)
        {
                if (event.target==delayBar)
		{
                       JugglingApplet.sleepTime=1000/delayBar.getValue();
		}
                return super.handleEvent(event);
        }
	public boolean action(Event event,Object arg)
	{
		if (event.target==generateButton)
		{
			if (optionsPanel.isShowing())
				optionsPanel.hide();
			else optionsPanel.show();
			paintAll(getGraphics());
		}
		if (event.target==animateButton || (event.target==siteswapOutput && animateButton.isEnabled()))
		{
			// animate selected siteswap
			animateIndex(siteswapOutput.getSelectedIndex());
		}
		return true;
	}
	protected void animateIndex(int siteswapIndex)
	{
                if (siteswapIndex>=0  && siteswapIndex<siteswaps.size())
                {
                        busy();
                        if (siteswapOutput.getSelectedIndex()!=siteswapIndex)
                                siteswapOutput.select(siteswapIndex);
                        Siteswap siteswap=(Siteswap)siteswaps.elementAt(siteswapIndex);
                        stop();
                        animationInit(siteswap);
                        start();
                        endBusy();
                }
	}
	private void setImage(int imageNo)
	{
		int ball;
                int extraX,extraY;
                extraX=-ballImage.getWidth(this)/2;
                extraY=0;//-ballImage.getHeight(this)/2;
                gContext.setColor(Color.white);
		gContext.fillRect(0,0,width,height);
		for (ball=0;ball<juggle.getBallCount();ball++)
		{
			gContext.setColor(color[ball%6]);
			if (ballPosition[ball][imageNo]!=null)
                        gContext.drawImage(ballImage,ballPosition[ball][imageNo].x+extraX,ballPosition[ball][imageNo].y+extraY,ballImage.getWidth(this),ballImage.getHeight(this),this);
		}
		
	}
	public void start()
	{
		currentImage=0;

		if (animate==null)
		{
			showStatus("Animation started");
			animate=new Thread(this);
			animate.start();
		}
	}

	public void stop()
	{
		if (animate!=null)
		{
			animate.stop();
			animate=null;
		}
	}
	public void paint(Graphics g)
	{
		update(g);	}

	public void run()
	{
		while (true)
		{
                        setImage(currentImage);
                        repaint();

                        postEvent(new Event(this,Event.MOUSE_ENTER, ""));

                        currentImage= ++currentImage % imageCount;
		
                        try
                        {
                                Thread.sleep(JugglingApplet.sleepTime);
                        }
                        catch (InterruptedException e)
                        {
                                showStatus(e.toString());
                        }
		}
	}

	public void update(Graphics g)
	{
                g.drawImage(image,0,0,imagePanel);       
        }


}

class JuggleOptionsPanel extends Panel
{
        private Label l1,l3,empty;
	private Choice ballsChoice,beatsChoice;
        private Button generateButton;
	private JugglingApplet parent;

	int balls,height=7,beats;

	int minBalls=1;
	int maxBalls=7;
	int minBeats=1;
	int maxBeats=9;

	JuggleOptionsPanel(JugglingApplet parent,int balls,int beats)
	{
		this.parent=parent;
		this.balls=balls;
		this.beats=beats;

		l1=new Label("Number of balls");
		l3=new Label("Maximum beats");
                empty=new Label("");
		ballsChoice=new Choice();
		beatsChoice=new Choice();

                empty=new Label("");
                generateButton=new Button("Generate Possibilities");

		setLayout(new GridLayout(4,2,10,5));

		add(l1);add(ballsChoice);
		add(l3);add(beatsChoice);
                add(empty);add(generateButton);
		
		for (int i=0;i<10;i++)
		{
			if ((i+minBalls)<maxBalls) ballsChoice.addItem(String.valueOf(i+minBalls));
			if ((i+minBeats)<maxBeats) beatsChoice.addItem(String.valueOf(i+minBeats));
		}
	
		ballsChoice.select(balls-minBalls);
		beatsChoice.select(beats-minBeats);

		resize(200,160);
		show();
	}
	public boolean action(Event e,Object o)
	{
		if (e.target==ballsChoice)
		{
			balls=ballsChoice.getSelectedIndex()+minBalls;
		}
		if (e.target==beatsChoice)
		{
			beats=beatsChoice.getSelectedIndex()+minBeats;
		}
		if (e.target==generateButton)
		{
			generateButton.disable();
			parent.generateSiteswaps(balls,height,beats);
			parent.animateIndex(0);
			generateButton.enable();
			hide();
			parent.paintAll(parent.getGraphics());
		}
		return true;
	}

}
