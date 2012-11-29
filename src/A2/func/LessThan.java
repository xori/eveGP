package A2.func;

import eveGP.internal.Tree;

/**
 *
 * @author Evan Verworn (4582938) <ev09qz@brocku.ca>
 */
public class LessThan extends eveGP.GPfunction {

    @Override
    public float result(Tree... children) {
        float d = children[0].evaluate() - children[1].evaluate();
        return (d < 0) ? 1 : 0;
    }
    
    public String toString (Tree ... children) {
        return "( < "+ children[0].toString() +
                " " + children[1].toString() + " )";
    }
}
