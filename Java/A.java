import java.util.*;
public class A {    
    static final WareHouse T = new WareHouse(2,0,1); //don't want these starting values changed. only want the plan made from them
	static final WareHouse A = new WareHouse(0,3,0);
	static final WareHouse B = new WareHouse(0,2,0);

	public static int h1(NodeTree<String> l) //boxes heuristic - problem orientated
	{
		return l.A.medium() + l.T.small() + l.B.medium() + l.T.large(); //returns sum of boxes to move
	}
	public static int h2(NodeTree<String> l) //Manhattan heuristic
	{
		return 5 - height(l,0); //returns boxes+1 - layer ie. 5 - layer for this test
	}
	public static int exploreNode(NodeTree<String> l) //-1 means go back. 0 means goal. 1 means goal not achieved
	{
		
		l.T = l.parent.T;
		l.A = l.parent.A;
		l.B = l.parent.B;
		if(l.parent.data.equals("T")&&l.data.equals("A")) { //if going from truck to warehouse A
			if(l.parent.T.small()==0) //no boxes to move
				return 2;
			l.T.takeSmall();//take small from truck
			l.A.addSmall(); //add small to warehouse A
			return 0;
		}
		if(l.parent.data.equals("T")&&l.data.equals("B")) { //if going from truck to warehouse B
			if(l.parent.T.large()==0) //no boxes to move
				return 2;
			l.T.takeLarge();//take large from truck
			l.B.addLarge(); //add large to warehouse B
			return 0;
		}
		if(l.parent.data.equals("A")&&l.data.equals("T")) { //if going to truck from warehouse A
			if(l.parent.A.medium()==0) //no boxes to move
				return 2;
			l.A.takeMedium();//take medium from A
			l.T.addMedium(); //add medium to truck
			return 0;
		}
		if(l.parent.data.equals("B")&&l.data.equals("T")) { //if going to truck from warehouse B
			if(l.parent.B.medium()==0) //no boxes to move
				return 2;
			l.B.takeMedium();//take medium from A
			l.T.addMedium(); //add medium to truck
			return 0;
		}
		if(l.parent.data.equals("A")&&l.data.equals("B") || l.parent.data.equals("B")&&l.data.equals("A")) { //robot not dropping anything off
			return 2;
		}
		return 1;
	}
	public static boolean checkgoal(NodeTree<String> l)
	{
		if((T.small()==T.large())&&A.medium()==A.large()&&B.medium()==B.small()&&T.small()==0&&A.medium()==0&&B.medium()==0)
			return true;
		return false;
	}
	public static List<NodeTree<String>> Astar(NodeTree<String> S, PriorityQueue<NodeTree<String>> fringe) //no heuristic added yet
	{
		List<NodeTree<String>> closedSet = new LinkedList<NodeTree<String>>(); //list of node locations that have been evaluated
		closedSet.add(S);
		generateNodes(S);
		NodeTree<String> left = S.children.get(0);
		NodeTree<String> right = S.children.get(1);
		left.cost +=  exploreNode(left) + h1(left);
		right.cost += exploreNode(right) + h1(right);
		fringe.add(left);
		fringe.add(right);
		while(!fringe.isEmpty())
		{
			NodeTree<String> tmp = fringe.remove();
			closedSet.add(tmp);
			System.out.println(tmp.data);
			if(checkgoal(tmp))
			{
				System.out.println("goal complete");
				return closedSet;
			}
			generateNodes(tmp);
			left = tmp.children.get(0);
			right = tmp.children.get(1);
			left.cost += exploreNode(left) + h1(left);
			right.cost += exploreNode(right) + h1(right);
			fringe.add(left);
			fringe.add(right);
		}		
		return null;
	}
	public static void generateNodes(NodeTree L)
	{
		switch(L.data.toString()) { //check where node is, generate children
		   case "T" :
		      L.addChild("A", 1);
		      L.addChild("B", 1);
		      System.out.println("Parent T: child A,B");
		      break; 
		   
		   case "A" :
			   L.addChild("T", 1);
			   L.addChild("B", 2); //Higher cost moving between A and B
			   System.out.println("Parent A: T,B");
		      break; // optional
		   case "B" :
			   L.addChild("T", 1);
			   L.addChild("A", 2);
			   System.out.println("Parent B: child T,A");
		      break; // optional
		   // You can have any number of case statements.
		   default :  
		} 
	}
	public static void main(String args[]) {
		
		NodeTree<String> T0 = new NodeTree<>("T",0);   //start
		Comparator<NodeTree<String>> comparator = new CostCompare();
	    PriorityQueue<NodeTree<String>> fringe = new PriorityQueue<NodeTree<String>>(comparator);//list of nodes to be evaluated(in order of estimated distance)
		T0.T = T;
		T0.A = A;
		T0.B = B;
		generateNodes(T0);
		List<NodeTree<String>> goal = Astar(T0,fringe);
		for(int i = 0; i<goal.size();i++)
		{
			System.out.println(goal.get(i).data);
		}
	}
	static int height(NodeTree<String> node, int depth)
	{
		if(node.parent ==null)
			return 1 + depth;
	    return 1 + height(node.parent,depth);
	}
	public static void printNodes(NodeTree<String> l, int i)
	{
		if(l.children==null)
		{
			return;
		}
		System.out.print(i + ". ");
		i++;
		System.out.println(l.getData());
		for(NodeTree node : l.children) {
			printNodes(node,i);
		}
	}
	
}


class CostCompare implements Comparator<NodeTree<String>>
{
    public int compare(NodeTree<String> x, NodeTree<String> y)
    {
        if (x.cost < y.cost)
        {
            return -1;
        }
        if (x.cost > y.cost)
        {
            return 1;
        }
        return 0;
    }
}
class NodeTree<T>{
	WareHouse A;
	WareHouse T;
	WareHouse B;
	int cost;
    T data;
    NodeTree<T> parent = null;
    List<NodeTree<T>> children;

    public NodeTree(T data, int cost) {
    	this.cost = cost;
        this.data = data;
        this.children = new LinkedList<NodeTree<T>>();
    }

    public void addChild(T child, int i) {
        NodeTree<T> childNode = new NodeTree<T>(child, i);
        childNode.setParent(this);
        this.children.add(childNode);
    }

    public void addChild(NodeTree child) {
        child.setParent(this);
        this.children.add(child);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    private void setParent(NodeTree parent) {
        this.parent = parent;
    }

    public NodeTree getParent() {
        return parent;
    }

	
}

