package eveGP.internal.tree;

import eveGP.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Evan Verworn (4582938) <ev09qz@brocku.ca>
 */
class GPtreenode {
    
    public ArrayList<GPtreenode> children;
    public GPproblem problem;
    public GPfunction function;
    // or static.thread0.problem

    
    public GPtreenode (GPproblem problem, GPtreenode ... child) {
        this.problem = problem;
	children = new ArrayList<GPtreenode>();
	children.addAll(Arrays.asList(child));
    }
}
