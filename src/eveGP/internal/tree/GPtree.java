package eveGP.internal.tree;

import eveGP.*;

/**
 *
 * @author Evan Verworn (4582938) <ev09qz@brocku.ca>
 */
public class GPtree {
    public float score = Float.NaN;
    
    private GPtreenode root = null;
    
    public GPtree(GPproblem p) {
	root = new GPtreenode(p);
    }
    
    
}
