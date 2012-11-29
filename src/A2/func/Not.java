package A2.func;

import eveGP.internal.Tree;

/**
 *
 * @author Evan Verworn (4582938) <ev09qz@brocku.ca>
 */
public class Not extends eveGP.GPfunction {

    @Override
    public float result(Tree... children) {
        return 1 - children[0].evaluate();
    }
    
    public String toString (Tree ... children) {
        String o = "( NOT ";
        for (Tree t : children)
            o += t.toString()+ " ";
        o+= ")";
        return o;
    }
}
