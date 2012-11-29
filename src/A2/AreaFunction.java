package A2;

import static eveGP.internal.Parameter.*;

/**
 *
 * @author Evan Verworn (4582938) <ev09qz@brocku.ca>
 */
public class AreaFunction {

    public static float avg (String array, int size, int X, int Y) {
        short [][] map = null;
        int W = 0, H = 0;
        float output = 0;
        try {
            map = (short[][]) get(array);
            W   = getI("Width");
            H   = getI("Height");
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            System.err.println("Failed on... "+ array);
            System.exit(500);
        }
        
        for (int y = Y - size / 2; y <= Y + size / 2; y++)
            for(int x = X - size / 2; x <= X + size / 2; x++) {
                output += map[(((x % W) + W) % W)][(((y % H) + H) % H)];
            }
        
        output /= size * size;
        
        return output;
    }
}
