package jordan.szalontai.unicrush;

import java.util.Arrays;

/**
 *
 * @author Szalontai Jordán
 */
public class Candy implements Comparable<Candy> {
    
    public static enum Color {
        EMPTY,
        RED,
        GREEN,
        BLUE,
        ORANGE,
        PRUPLE,
        YELLOW
    }

    public static Color getRandomColor() {
        return Arrays.asList(Color.values())
                .get((int) (Math.random() * (Color.values().length - 1)) + 1);
    }
    
    private Color color;
    private boolean markedForPop;

    public Candy(Color color) {
        this.color = color;
    }
    
    public Candy(Candy src) {
        this.color = src.color;
        this.markedForPop = false;
    }

    public Color getColor() {
        return color;
    }

    public boolean isMarkedForPop() {
        return markedForPop;
    }
    
    public boolean isEmpty() {
        return this.color == Color.EMPTY;
    }
    
    public void setMarkedForPop(boolean mark) {
        this.markedForPop = mark;
    }
    
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return color.toString().charAt(0) + "";
    }
    
    @Override
    public int compareTo(Candy o) {
        if (o.isEmpty()) {
            return -1;
        }
        return 0;
    }
}
