package A2.func;

import eveGP.internal.Tree;
import static eveGP.internal.Parameter.*;
import static java.lang.Math.*;

/**
 * AvgBrightness3x3 () : Short
 * @author Evan Verworn (4582938) <ev09qz@brocku.ca>
 */
public class StdDevEdge extends eveGP.GPfunction {
    @Override
    public float result(Tree... children) {
        int SIZE = (int) children[0].evaluate();
        float AVG = A2.AreaFunction.avg("edge:short[][]", SIZE,
                getIntVariable("X"), getIntVariable("Y"));
        short [][] map = null;
        int X = 0, Y = 0, W = 0, H = 0;
        float output = 0;
        try {
            map = (short[][]) get("edge:short[][]");
            X   = getIntVariable("X");
            Y   = getIntVariable("Y");
            W   = getI("Width");
            H   = getI("Height");
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            System.err.println("Failed on... "+ "edge:short[][]");
            System.exit(500);
        }
        for (int y = Y - SIZE / 2; y <= Y + SIZE / 2; y++)
            for(int x = X - SIZE / 2; x <= X + SIZE / 2; x++) {
                output += pow(map[(((x % W) + W) % W)][(((y % H) + H) % H)]-AVG, 2);
            }
        output /= SIZE * SIZE;

        return (float) sqrt(output);
    }

    public String toString (Tree ... children){
        return "STD_DEV_EDGE" + children[0].toString();
    }
}
