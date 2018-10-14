import java.util.*;

public class Network
{
	int maxNodes;
	int maxArcs;
	private Vector nodeList;
	private Vector arcList;

	Network(int maxNodes)
	{
		nodeList=new Vector(maxNodes);
		arcList=new Vector(maxNodes*maxNodes);
		this.maxNodes=maxNodes;
		this.maxArcs=maxNodes*maxNodes;
	}

// Node operations

	public void addNode()
	{
		Node newNode;
		newNode=new Node(maxArcs);
		nodeList.addElement((Object)newNode);
	}
	public void addNode(Object nodeData)
	{
		Node newNode;
		newNode=new Node(maxArcs,nodeData);
		nodeList.addElement((Object)newNode);
	}
	public Node getNode(int i)
	{	
		Node node;
		node=(Node)nodeList.elementAt(i);
		return node;
	}
	public int getNodeCount()
	{
		return nodeList.size();
	}
	public void addNodeData(int index,Object data)
	{
		getNode(index).addData(data);
	}
	public Object getNodeData(int index)
	{
		return getNode(index).getData();
	}

// Arc operations

	public void addArc(Node fromNode,Node toNode)
	{
		Arc newArc;
		newArc=new Arc(fromNode,toNode);
		// add to arc list
		arcList.addElement((Object)newArc);
		// add to node
		fromNode.addArc(newArc);
	}
	public void addArc(Node fromNode,Node toNode,Object arcData)
	{
		Arc newArc;
		newArc=new Arc(fromNode,toNode);
		newArc.addData(arcData);
		// add to arc list
		arcList.addElement((Object)newArc);
		// add to node
		fromNode.addArc(newArc);
	
	}
	public Arc getArc(int i)
	{
		Arc arc;
		arc=(Arc)arcList.elementAt(i);
		return arc;
	}
	public int getArcCount()
	{
		return arcList.size();
	}
	public void addArcData(int index,Object data)
	{
		getArc(index).addData(data);
	}
	public Object getArcData(int index)
	{
		return getArc(index).getData();
	}
}

