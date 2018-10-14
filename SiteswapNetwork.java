import java.util.*;

public class SiteswapNetwork extends Network
{
	private int maxHeight;
	private int ballNo;

	public static int getStateCount(int balls,int maxHeight)
	{
		return factorial(maxHeight)/(factorial(maxHeight-balls)*factorial(balls));
	}
	private static int factorial(int n)
	{
		if (n<=1) return 1;
		return n*factorial(n-1);
	} 

	SiteswapNetwork(int ballNo,int maxHeight)
	{
		super(getStateCount(ballNo,maxHeight));
		this.ballNo=ballNo;
		this.maxHeight=maxHeight;
		if (maxHeight<ballNo)
		{
			System.out.println("Must set max height greater than number of balls");
		}
	}
	public void generateStateNodes()
	{	
		State state;
		state=new State(maxHeight);
		setStates(state,0,ballNo,maxHeight-ballNo);
	}
	private void setStates(State state,int pos,int balls,int emptyBeats)
	{
		if ((balls+emptyBeats)==0) 
		{
		// add state node
			Object stateCopy=(Object)state.copyState();
			addNode(stateCopy);
			//state.print();
		}
		else
		{
			if (balls>0)
			{
			state.setState(pos,true);
			setStates(state,pos+1,balls-1,emptyBeats);
			}
			if (emptyBeats>0)
			{
			state.setState(pos,false);
			setStates(state,pos+1,balls,emptyBeats-1);
			}
		}
	}
	public void formLinks()
	{
		Node node1,node2;
		State state1,state2;
		int i,j,throwHeight;
		int strength;
		for (i=0;i<getNodeCount();i++)
		{
			for (j=0;j<getNodeCount();j++)
			{
				node1=getNode(i);
				node2=getNode(j);
				state1=(State)node1.getData();
				state2=(State)node2.getData();
				throwHeight=state1.getLinkStrength(state2);
				if (throwHeight>=0)
				{
					ArcData arcData;
					arcData=new ArcData(throwHeight);		
					addArc(node1,node2,arcData);
				}
			}	
		}
	}
	public Vector findSiteswaps(int maxBeats)
	{
// returns list of siteswaps
		int i;//,j;
	//	Vector combinedList;
	//	Vector list;
		Path circuit;
	//	combinedList=new Vector();
		circuit=new Path(maxBeats);
		for (i=0;i<getNodeCount();i++)
		{
			circuit.findCircuits(getNode(i));
		/*	if (list!=null)
				for (j=0;j<list.size();j++)
					combinedList.addElement(list.elementAt(j));
		*/
			getNode(i).label(true);
		}
		return circuit.getSiteswapList();
	}
}

class ArcData
{
	private int throwHeight;
	private boolean labelled=false;
	ArcData(int throwHeight)
	{
		this.throwHeight=throwHeight;
	}
	public void setLabel(boolean set)
	{
		labelled=set;
	}
	public boolean isLabelled()
	{
		return labelled;
	}
	public int getThrowHeight()
	{
		return throwHeight;
	}
	public void print()
	{
		System.out.print(throwHeight+" ");
	}
}

class Path
{
	private Node startNode,finishNode;
	private int links[];  // stores throw heights
	Vector siteswaps; // list of siteswaps found
	private int maxLength;
	Path(int maxLength)
	{
		int i;
		links=new int[maxLength];
		this.maxLength=maxLength;
		siteswaps=new Vector();
	}

	public void findCircuits(Node startNode)
	{
// returns list of siteswaps
		findPaths(startNode,startNode);
	}
	public Vector getSiteswapList()
	{
		return siteswaps;
	}
	public void findPaths(Node startNode,Node finishNode)
	{
		this.startNode=startNode;
		this.finishNode=finishNode;
		getPaths(startNode,0);
	}
	private void getPaths(Node node,int arcsTraced)
	// finds paths using unlabelled nodes
	{
		if (node.isLabelled()) return;
		if (arcsTraced!=0 && node==finishNode) 
		{
			addToList(arcsTraced);
			//print(arcsTraced);
			return;
		}
		if (arcsTraced==maxLength) 
			return;
		else
		{
			// find links
			int i;
			Arc arc;
			ArcData arcData;
			for (i=0;i<node.getArcCount();i++)
			{
				arc=node.getArc(i);
				arcData=(ArcData)arc.getData();
				links[arcsTraced]=arcData.getThrowHeight();
				getPaths(arc.getFinishNode(),arcsTraced+1);
			}
		}
	}
	public Node startNode()
	{
		return startNode;
	}
	public Node finishNode()
	{
		return finishNode;
	}
	public void addToList(int beats)
	{
		Siteswap siteswap;
		State startState;
		int i;
		startState=(State)startNode.getData();
		siteswap=new Siteswap(beats,startState);
		for (i=0;i<beats;i++)
		{
			siteswap.setThrow(i,links[i]);
		}
	//	siteswap.print();
		siteswaps.addElement(siteswap);
	}
}
