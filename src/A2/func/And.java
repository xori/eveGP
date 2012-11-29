package A2.func;

import eveGP.internal.Tree;

/**
 *
 * @author Evan Verworn (4582938) <ev09qz@brocku.ca>
 */
public class And extends eveGP.GPfunction {

    @Override
    public float result(Tree... children) {
        for (Tree t : children)
            if (t.evaluate() == 0)
                return 0;
        return 1;
    }
    
    public String toString (Tree ... children) {
        String o = "( AND ";
        for (Tree t : children)
            o += t.toString()+ " ";
        o+= ")";
        return o;
    }
}
