package eveGP.internal;

import eveGP.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Evan Verworn (4582938) <ev09qz@brocku.ca>
 */
public class Tree implements Comparable<Tree>{
    public float score = Float.NaN;
    public GPfunction function;
    public ArrayList<Tree> children;
    public int thread = -1;
    public int depth = 0;
    
    public Tree (GPfunction func, Tree ... children) {
       function = func;
       this.children = new ArrayList<Tree>(Arrays.asList(children));
    }
    
    public Tree (GPfunction func) {
	function = func;
    }

    public float evaluate () {
	return function.result((Tree[]) children.toArray());
    }
    // Tree gp = new Tree(func);
    // gp.addChildren(node);
    // function result(Tree ... t) {
    //	    t[0].evaluate();
    //	    ...
    
    public void addChildren(Tree node) {
	children.add(node);
    }
    
    @Override
    public int compareTo(Tree o) {
	if (Math.abs(this.score) > Math.abs(o.score)) return 1;
	else if (Math.abs(this.score) < Math.abs(o.score)) return -1;
	else return 0;
	// return this.score - o.score;
    }
}