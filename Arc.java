public class Arc
{
	Node fromNode;
	Node toNode;
	Object arcData;
	Arc(Node start,Node finish)
	{
		fromNode=start;
		toNode=finish;
	} 
	public Node getStartNode()
	{
		return fromNode;
	}
	public Node getFinishNode()
	{
		return toNode;
	}
	public void addData(Object data)
	{
		arcData=data;
	}
	public Object getData()
	{
		return arcData;
	}
}