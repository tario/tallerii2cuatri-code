package mreleditor.conversor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
  * @author ycoppel@google.com (Yohann Coppel)
  * 
  * @param <T>
  *          Object's type in the tree.
*/
public class Tree<T> {

  private T root;

  private ArrayList<Tree<T>> leafs = new ArrayList<Tree<T>>();

  private Tree<T> parent = null;
  private int level;
  private HashMap<T, Tree<T>> locate = new HashMap<T, Tree<T>>();
  private HashMap<T, Integer> levels = new HashMap<T, Integer>();

  public Tree(T root) {
    this.root = root;
    locate.put(root, this);
    levels.put(root, 1);
    level=1;
  }

  public void addLeaf(T root, T leaf) {
    if (locate.containsKey(root)) {
      locate.get(root).addLeaf(leaf);
    } else {
      addLeaf(root).addLeaf(leaf);
    }
    
  }
  private void increaseLevel(int childLevel){
	  if(level==childLevel){
		  level++;
		  if(parent!=null)
			  parent.increaseLevel(childLevel+1);
	  }
  }
  public Tree<T> addLeaf(T leaf) {
    Tree<T> t = new Tree<T>(leaf);
    leafs.add(t);
    t.parent = this;
    t.locate = this.locate;
    t.levels=levels;
    locate.put(leaf, t);
    t.levels.clear();
    levels.put(leaf,levels.get(root).intValue()+1);
    
    increaseLevel(1);
    
    return t;
  }

  public int getLevel(){
	  return level;
  }
  public Tree<T> setAsParent(T parentRoot) {
    Tree<T> t = new Tree<T>(parentRoot);
    t.leafs.add(this);
    this.parent = t;
    t.locate = this.locate;
    t.locate.put(root, this);
    t.locate.put(parentRoot, t);
    return t;
  }

  public T getRoot() {
    return root;
  }

  public Tree<T> getTree(T element) {
    return locate.get(element);
  }

  public Tree<T> getParent() {
    return parent;
  }

  public Collection<T> getSuccessors(T root) {
    Collection<T> successors = new ArrayList<T>();
    Tree<T> tree = getTree(root);
    if (null != tree) {
      for (Tree<T> leaf : tree.leafs) {
        successors.add(leaf.root);
      }
    }
    return successors;
  }

  public Collection<Tree<T>> getSubTrees() {
    return leafs;
  }

  public static <T> Collection<T> getSuccessors(T of, Collection<Tree<T>> in) {
    for (Tree<T> tree : in) {
      if (tree.locate.containsKey(of)) {
        return tree.getSuccessors(of);
      }
    }
    return new ArrayList<T>();
  }

  @Override
  public String toString() {
    return printTree(0);
  }

  private static final int indent = 2;

  private String printTree(int increment) {
    String s = "";
    String inc = "";
    for (int i = 0; i < increment; ++i) {
      inc = inc + " ";
    }
    s = inc + root;
    for (Tree<T> child : leafs) {
      s += "\n" + child.printTree(increment + indent);
    }
    return s;
  }
}