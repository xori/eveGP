package eveGP.func;

import eveGP.internal.Tree;

/**
 *  Example function of roughly equal. Where the tree determines how equal they
 *      need to be.
 * 
 * function RoughlyEqual ( Float, Float, Float ) : Boolean
 * 
 * @author Evan Verworn (4582938) <ev09qz@brocku.ca>
 */
public class RoughlyEqual extends eveGP.GPfunction {

    @Override
    public float result(Tree... children) {
        float d = Math.abs(children[1].evaluate() - children[2].evaluate());
        if (d < children[0].evaluate())
            d = 1;
        else
            d = 0;
        return d;
    }
    
    public String toString (Tree ... children) {
        return "( ~= "+ children[0].toString() +
                " " + children[1].toString() +
                " " + children[2].toString() + " )";
    }
    
}
