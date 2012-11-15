package eveGP.func;

import eveGP.internal.Tree;

/**
 *
 * @author Evan Verworn (4582938) <ev09qz@brocku.ca>
 */
public class Or extends eveGP.GPfunction {

    @Override
    public float result(Tree... children) {
        for (Tree t : children)
            if (t.evaluate() == 1)
                return 1;
        return 0;
    }
    
    public String toString (Tree ... children) {
        String o = "( OR ";
        for (Tree t : children)
            o += t.toString()+ " ";
        o+= ")";
        return o;
    }
}
