package CheckersClasses;

/**
 * @author Jake Strauch
 * Child-sibling node type for an n-ary tree.
 */
public class CSNode<E>
{
  public static double C = Math.sqrt(2);
  //public static double C = 0;
  /*protected CSNode<E> firstChild;
  protected CSNode<E> nextSibling;*/
  protected CSNode<E> parent = null;
  protected CSNode<E>[] children = null;
  protected E data;
  int numLegalChildren = 0;
  int player;
  protected int simulations = 0;
  protected int wins = 0;
  protected int draws = 0;

  public double UCB()
  {
    return (wins+(((double)draws)/2)) /(double)simulations + C*Math.sqrt(Math.log(parent.simulations)/simulations);
  }

  public CSNode(){}
  
  public CSNode(E data)
  {
    this(data, null, null);
  }
  
  public CSNode(E data, CSNode<E>[] children, CSNode<E> parent)
  {
    this.children = children;
    this.parent = parent;
    this.data = data;
  }
  
  public boolean isLeaf()
  {
    return children == null;
  }
  
  public CSNode<E>[] getChildren()
  {
    return children;
  }
  
  public void setChildren(CSNode<E>[] children)
  {
    this.children = children;
  }

  public E getData()
  {
    return data;
  }
  
  public void setData(E data)
  {
    this.data = data;
  }

  public void backPropogate(int result)
  {
    simulations +=1;
    wins +=result;
    //result = (result ==0)?1:0;
    if(parent != null)
      parent.backPropogate(result);

  }
  public void backPropogateNodes(int result)
  {
    simulations +=1;
    if(result == -1)
      draws +=1;
    else {
      wins += result;
      result = (result == 0) ? 1 : 0;
    }
    if(parent != null)
      parent.backPropogateNodes(result);
  }


}

