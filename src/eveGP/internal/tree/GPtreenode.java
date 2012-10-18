package eveGP.internal.tree;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Evan Verworn (4582938) <ev09qz@brocku.ca>
 */
class GPtreenode {
    
    public ArrayList<GPtreenode> children;
    
    public GPtreenode (GPtreenode ... child) {
	children = new ArrayList<>();
	children.addAll(Arrays.asList(child));
    }
}
