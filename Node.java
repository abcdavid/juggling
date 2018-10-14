import java.util.*;

public class Node
{
	Object nodeData;
	Vector arcList;
	boolean labelled=false;

	Node(int maxArcs)
	{
		arcList=new Vector(maxArcs);
	}
	Node(int maxArcs,Object nodeData)
	{
		this(maxArcs);
		addData(nodeData);
	}
	public void label(boolean isLabelled)
	{
		labelled=isLabelled;
	}
	public boolean isLabelled()
	{
		return labelled;
	}
	public void addArc(Arc arc)
	{
		arcList.addElement((Object)arc);
	}
	public Arc getArc(int i)
	{
		Object data;
		data=arcList.elementAt(i);
		return (Arc)data;
	}
	public int getArcCount()
	{
		return arcList.size();
	}
	public void addData(Object newData)
	{
		nodeData=newData;
	}
	public Object getData()
	{
		return nodeData;
	}
}