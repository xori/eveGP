package A2.func;

import eveGP.internal.Tree;

/**
 *
 * @author Evan Verworn (4582938) <ev09qz@brocku.ca>
 */
public class B extends eveGP.GPfunction {

    @Override
    public float result(Tree... children) {
        return A2.AreaFunction.avg("blue:short[][]", (int) children[0].evaluate(),
                getIntVariable("X"), getIntVariable("Y"));
    }
    
    public String toString (Tree ... children){
        return "Bx"+children[0].toString();
    }
    
}
