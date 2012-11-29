package A2.func;

import eveGP.internal.Tree;

/**
 * AvgBrightness (SELECTOR) : Float
 * @author Evan Verworn (4582938) <ev09qz@brocku.ca>
 */
public class AvgBrightness extends eveGP.GPfunction {

    @Override
    public float result(Tree... children) {
        return A2.AreaFunction.avg(
                "brightness:short[][]", (int) children[0].evaluate(),
                getIntVariable("X"), getIntVariable("Y"));
    }
    
    public String toString (Tree ... children){
        return "AVG_BRIGHTNESS " + children[0].toString();
    }
}
