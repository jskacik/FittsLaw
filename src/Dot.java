import java.awt.*;

public class Dot {
    protected int radius;
    protected Color color;
    protected double x;
    protected double y;

    protected int clockNum;

    protected int numTimesShown;

    public Dot(Point center) {
        this.x = center.x;
        this.y = center.y;
        radius = 5;
        color = Color.BLUE;
        clockNum = 0;
        numTimesShown = 0;
    }

    public int getClockNum() {
        return clockNum;
    }

    public void incrementNumTimesShow(){
        numTimesShown += 1;
    }

    public int getNumTimesShown() {
        return numTimesShown;
    }

    public void setClockNum(int clockNum) {
        this.clockNum = clockNum;
    }

    public Dot(Point center, int radius, Color color){
        this.x = center.x;
        this.y = center.y;
        this.radius = radius;
        this.color = color;
    }

    public Point getCenter() {
        return new Point((int)x, (int)y);
    }

    public void setCenter(Point p){
        x = p.x;
        y = p.y;
    }

    public int getTop(){
        return (int) y -radius;
    }
    public int getBottom(){
        return (int) y + radius;
    }
    public int getLeft(){
        return (int) x - radius;
    }
    public int getRight(){
        return (int) x +radius;
    }

    public Rectangle getRegion(){
        return  new Rectangle(getTop(),getLeft(), 2*radius, 2 *radius);
    }

    void paint(Graphics g){
        g.setColor(color);
        g.fillOval(getLeft(), getTop(), radius*2, radius*2 );
    }

    public boolean isInside(Point p){
        Point center = new Point((int)x,(int)y);
        return p.distance(center)<radius;
    }

    public void setColor(Color c){
        this.color = c;
    }
}