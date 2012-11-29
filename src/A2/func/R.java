package A2.func;

import eveGP.internal.Tree;

/**
 *
 * @author Evan Verworn (4582938) <ev09qz@brocku.ca>
 */
public class R extends eveGP.GPfunction {

    @Override
    public float result(Tree... children) {
        return A2.AreaFunction.avg("red:short[][]", (int) children[0].evaluate(),
                getIntVariable("X"), getIntVariable("Y"));
    }
    
    public String toString (Tree ... children){
        return "Rx"+children[0].toString();
    }
    
}
