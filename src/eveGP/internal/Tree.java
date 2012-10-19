package eveGP.internal;

import eveGP.*;

/**
 *
 * @author Evan Verworn (4582938) <ev09qz@brocku.ca>
 */
public class Tree implements Comparable<Tree>{
    public float score = Float.NaN;
    public GPfunction root;
    public int thread = -1;
    
    public Tree () {
       
    }

    @Override
    public int compareTo(Tree o) {
	if (Math.abs(this.score) > Math.abs(o.score)) return 1;
	else if (Math.abs(this.score) < Math.abs(o.score)) return -1;
	else return 0;
	// return this.score - o.score;
    }
}