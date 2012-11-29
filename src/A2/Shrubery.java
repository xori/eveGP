package A2;

import eveGP.internal.Parameter;
import eveGP.internal.Tree;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import static eveGP.internal.Parameter.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Evan Verworn (4582938) <ev09qz@brocku.ca>
 */
public class Shrubery extends eveGP.GPproblem {
    
    private int tP, tN, fP, fN;
    private float bS = 9999999;

    @Override
    public float evaluate(Tree tree) {
        float error = 0;
        
        float e1, e2;
        int falseP, falseN;
        
        ArrayList<Point> correct, incorrect;
        correct = (ArrayList<Point>) get("correct:List");
        incorrect = (ArrayList<Point>) get("incorrect:List");
        
        ConcurrentHashMap<Point, ConcurrentLinkedQueue<Tree>> scoring;
	ConcurrentLinkedQueue<Tree> temp;
	scoring = (ConcurrentHashMap<Point, ConcurrentLinkedQueue<Tree>>) Parameter.get("shared.table");
        
        for(Point p : correct) {
            setVariable("X", p.x); setVariable("Y", p.y);
            if (tree.evaluate() == 0) {
                error++; // False Negative
                temp = scoring.get(p);
                if (temp == null) {
                    temp = new ConcurrentLinkedQueue<Tree>();
                    temp.add(tree);
                    scoring.put(p, temp);
                } else {
                    temp.add(tree);
                }               
            }
        }
        falseN = (int) error;
        e1 = error / correct.size();
        error = 0;
        for(Point p : incorrect) {
            setVariable("X", p.x); setVariable("Y", p.y);
            if (tree.evaluate() == 1) {
                error ++; // False Positive
                temp = scoring.get(p);
                if (temp == null) {
                    temp = new ConcurrentLinkedQueue<Tree>();
                    temp.add(tree);
                    scoring.put(p, temp);
                } else {
                    temp.add(tree);
                }
            }
        }
        falseP = (int) error;
        e2 = error / incorrect.size();
        
//        if (e1 + e2 < bS) {
//            fP = falseP;
//            fN = falseN;
//            tP = correct.size() - fN;
//            tN = incorrect.size() - fP;      
//            bS = (e1+e2);
//        }
        
        return e1 + e2;
    }
    
    /**
     * The freaking huge preprocessing bit.
     */
    @Override
    public void setup () {
        try {
            Parameter.set("shared.table", new ConcurrentHashMap<Point, ConcurrentLinkedQueue<Tree>>());
            
            BufferedImage global = ImageIO.read(new File("resources/boats.png"));
            BufferedImage overlay = ImageIO.read(new File("resources/boats-overlay-small.png"));
            BufferedImage edge   = ImageIO.read(new File("resources/boats-edge.png"));
            set("global:BufferedImage", global);
            set("overlay:BufferedImage", overlay);
            set("edge:BufferedImage", edge);
            
            int width = global.getWidth();
            int height = global.getHeight();
            set("Width", width);
            set("Height", height);
            
            ArrayList<Point> correct = new ArrayList<Point>(), 
                    wrong = new ArrayList<Point>();
            short [][]  brightness  = new short[width][height], 
                        edgeArray   = new short[width][height],
                        red         = new short[width][height], 
                        green       = new short[width][height], 
                        blue        = new short[width][height];
            
            for(int y = 0; y < overlay.getHeight(); y++)           
                for(int x = 0; x < overlay.getWidth(); x++) {
                    // Get training data.
                    int rgb = overlay.getRGB(x, y);
                    if (rgb == 0xFFFF0000) { // Red
                        wrong.add(new Point(x,y));
                    } else if (rgb == 0xFF00FF00) { // Greem
                        correct.add(new Point(x,y));
                    }
                    // Get Brightness
                    rgb = global.getRGB(x, y);
                    int b = rgb & 0x000000FF;
                    rgb = rgb >>> 8;
                    int g = rgb & 0x000000FF;
                    rgb = rgb >>> 8;
                    int r = rgb & 0x000000FF;
                    brightness[x][y] = (short) (r * 0.241 + b * 0.068 + g * 0.691);
                    
                    // Colour
                    red[x][y] = (short) r;
                    blue[x][y] = (short) b;
                    green[x][y] = (short) g;
                    
                    // Edge
                    rgb = edge.getRGB(x, y);
                    b = rgb & 0x000000FF;
                    rgb = rgb >>> 8;
                    g = rgb & 0x000000FF;
                    rgb = rgb >>> 8;
                    r = rgb & 0x000000FF;
                    edgeArray[x][y] = (short) (r * 0.241 + b * 0.068 + g * 0.691);
                }
            set("red:short[][]", red);
            set("blue:short[][]", blue);
            set("green:short[][]", green);
            set("brightness:short[][]", brightness);
            set("correct:List", correct);
            set("incorrect:List", wrong);
            set("edge:short[][]", edgeArray);
            
            System.err.println("correct:: "+ correct.size());
            System.err.println("incorrect:: "+wrong.size());
            
        } catch (Exception ex) {
            Logger.getLogger(Shrubery.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(404);
        }
    }    
    
    
    public void stats ( int generation, Tree ... trees ) {
        // DO STATS
        super.stats(generation, trees);
        
        // DO TESTING
        BufferedImage B = new BufferedImage(getI("Width"), getI("Height"), BufferedImage.TYPE_BYTE_INDEXED);
        BufferedImage Train = new BufferedImage(getI("Width"), getI("Height"), BufferedImage.TYPE_BYTE_INDEXED);
        Graphics2D g = B.createGraphics();
        Graphics2D gTrain = Train.createGraphics();
        ArrayList<Point> correct, incorrect;
        correct = (ArrayList<Point>) get("correct:List");
        incorrect = (ArrayList<Point>) get("incorrect:List");
        
        for (int i = 0; i < 1; i++) {
            tP = tN = fP = fN = 0;            
            
            for(Point p : correct) {
                setVariable(trees[i], "X", p.x); setVariable(trees[i], "Y", p.y);
                if (trees[i].evaluate() == 0) {
                    g.setColor(Color.yellow);
                    g.drawLine(p.x, p.y, p.x, p.y); // False Negative
                    fN++;
                } else {
                    g.setColor(Color.green);
                    g.drawLine(p.x, p.y, p.x, p.y); // True Positive
                    tP++;
                }
            }
            for(Point p : incorrect) {
                setVariable(trees[i], "X", p.x); setVariable(trees[i], "Y", p.y);
                if (trees[i].evaluate() == 1) {
                    g.setColor(Color.red);
                    g.drawLine(p.x, p.y, p.x, p.y); // False Positive
                    fP++;
                } else {
                    g.setColor(Color.gray);
                    g.drawLine(p.x, p.y, p.x, p.y); // True Negative
                    tN++;
                }
            }
            
            for (int y = 0; y < getI("Height"); y++) {
                for (int x = 0; x < getI("Width"); x++) {
                    setVariable(trees[i], "X", x); setVariable(trees[i], "Y", y);
                    float car = trees[0].evaluate();
                    if (car == 0) {
                        gTrain.setColor(Color.black);
                    } else {
                        gTrain.setColor(Color.green);
                    }
                    gTrain.drawLine(x, y, x, y);
                }
            }
            
            
        }
        try {
            ImageIO.write(B, "png", new File("output/"+getS("job")+"."+generation+".png"));
            ImageIO.write(Train, "png", new File("output/"+getS("job")+".T"+generation+".png"));
            if (generation == 0) {
                try { new File("output/"+getS("job")+"confusionMatrix.txt").delete();}
                catch(Exception e) {}
            }
            FileWriter fw = new FileWriter("output/"+getS("job")+"confusionMatrix.txt", true);
            if (generation == 0)
                fw.write(("True Pos,True Neg,False Pos,False Neg"));
            fw.write(tP + "," + tN + "," + fP + "," + fN+"\n");
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(Shrubery.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
