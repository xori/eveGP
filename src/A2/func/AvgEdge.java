package A2.func;

import eveGP.internal.Tree;

/**
 * AvgBrightness5x5 () : Short
 * @author Evan Verworn (4582938) <ev09qz@brocku.ca>
 */
public class AvgEdge extends eveGP.GPfunction {

    @Override
    public float result(Tree... children) {
        return A2.AreaFunction.avg("edge:short[][]", (int) children[0].evaluate(),
                getIntVariable("X"), getIntVariable("Y"));
    }
    
    public String toString (Tree ... children){
        return "AVG_EDGE" + children[0].toString();
    }
}
