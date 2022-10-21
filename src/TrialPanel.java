import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class TrialPanel extends JPanel implements MouseListener{
    private ArrayList<Dot> dots;
    private ArrayList<Boolean> isVisible;
    private int size = 20;
    private int numDots = 12;

    private int MAX_GUESSES = 24;
    private int interval;
    private Random random;

    private boolean hasUnique;

    private boolean hasStarted;
    private int width = 1000;
    private int height = 1000;

    private int targetSize;
    private int testRadius;

    private long startTime;

    private long stopTime;

    private boolean timerHasStarted;
    private boolean timerHasEnded;

    private SubjectInfo currentSubject;

    private int dotClockNum;


    MainForm formListener;

    Timer timer;

    /*Map<String, String> myMap = new HashMap<String, String>() {{
        put("foo", "bar");
        put("key", "value");
        //etc
    }};*/
    private Map<Integer, Integer[]> clockOpposites = new HashMap<Integer, Integer[]> () {{
       put(1, new Integer[]{8, 7, 6});
       put(2, new Integer[]{9, 8, 7});
       put(3, new Integer[]{10, 9, 8});
       put(4, new Integer[]{11, 10, 9});
       put(5, new Integer[]{12, 11, 10});
       put(6, new Integer[]{11, 12, 1});
       put(7, new Integer[]{12, 1, 2});
       put(8, new Integer[]{1, 2, 3});
       put(9, new Integer[]{2, 3, 4});
       put(10, new Integer[]{3, 4, 5});
       put(11, new Integer[]{4, 5, 6});
       put(12, new Integer[]{5, 6, 7});
    }};

    private ArrayList<Integer> alreadyUsedClockNums;
    private ArrayList<Integer> angles = new ArrayList<Integer>();

    private Point center = new Point(width/2, height/2);

    public TrialPanel(int targetSize, int testRadius) {
        setBackground(Color.GRAY);
        setPreferredSize(new Dimension(width,height));
        addMouseListener(this);
        dots = new ArrayList<Dot>();
        isVisible = new ArrayList<Boolean>();
        interval = 150;
        random = new Random();
        angles.add(0);
        angles.add(30);
        angles.add(60);
        //angles.add(90);
        this.targetSize = targetSize;
        this.testRadius = testRadius;
        this.hasStarted = false;
        this.timerHasStarted = false;
        this.timerHasEnded = false;
        alreadyUsedClockNums = new ArrayList<Integer>();
        setDots();


    }

    public void reset(){
        dots = new ArrayList<Dot>();
        isVisible = new ArrayList<Boolean>();
        interval = 150;
        random = new Random();
        this.hasStarted = false;
        this.timerHasStarted = false;
        this.timerHasEnded = false;
        alreadyUsedClockNums = new ArrayList<Integer>();
        setDots();
    }

    public int getTargetSize() {
        return targetSize;
    }

    public void setTargetSize(int targetSize) {
        this.targetSize = targetSize;
        clearDots();
        setDots();
        repaint();
    }

    public int getTestRadius() {
        return testRadius;
    }

    public void setTestRadius(int testRadius) {
        this.testRadius = testRadius;
        clearDots();
        setDots();
        repaint();
    }

    public void clearDots(){
        dots.clear();
    }

    private void setDots(){
        int angleModifier = 30;
        int angle = 0;
        int clockNum = 1;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 4; j++){
                //System.out.println(clockNum);
                Dot tempDot = new Dot(calculateDotCenter(testRadius, angle), targetSize, Color.BLUE);
                tempDot.setClockNum(clockNum);
                clockNum+=1;
                dots.add(tempDot);
                isVisible.add(false);
                angle += angleModifier;

            }
        }
    }
    private Point calculateDotCenter(int testRadius, int angle){
        double x = this.center.x + (testRadius * Math.cos(Math.toRadians(angle)));
        double y = this.center.y + (testRadius * Math.sin(Math.toRadians(angle)));
        return new Point((int) x, (int) y);
    }

    public void startTrial(){
        if(!hasStarted) {
            int firstDotIndex = getRandomDotIndex();
            isVisible.set(firstDotIndex, true);
            dots.get(firstDotIndex).incrementNumTimesShow();
            //currentSubject.currentGuess.setGuess(currentSubject.numGuesses, dots.get(firstDotIndex).getClockNum());
            hasStarted = true;
        }
        repaint();

    }

    public void runTrial(SubjectInfo currentSubject){
        this.currentSubject = currentSubject;
        currentSubject.incrementNumGuesses();
        startTrial();

    }

    private void continueTrial(MouseEvent e){
        Point mouseClick = e.getPoint();
        for(int i = 0; i < dots.size(); i++){
            if(isVisible.get(i)){
                System.out.println(dots.get(i).isInside(mouseClick));
                if(dots.get(i).isInside(mouseClick)){
                    currentSubject.incrementAccurateClicks();
                        if (!timerHasStarted) {
                            timerHasStarted = true;
                            startTime = System.currentTimeMillis();
                        } else {
                            timerHasStarted = false;
                            stopTime = System.currentTimeMillis();
                            currentSubject.currentGuess.setElapsedTime(stopTime - startTime);
                            currentSubject.recordGuess(dots.get(i).getClockNum());
                            currentSubject.incrementNumGuesses();
                            if(currentSubject.numGuesses <= MAX_GUESSES) {
                                nextVisibleDot(dots.get(i));
                                repaint();
                                timerHasStarted = true;
                                startTime = System.currentTimeMillis();
                            }
                            else {
                                updateListener();
                            }
                        }
                    }
                else{
                    currentSubject.incrementMissedClicks();
                    currentSubject.currentGuess.incrementMissedClicks();
                }
                }

            }
        }


    public void addListener(MainForm form){
        formListener = form;
    }

    public void updateListener(){
        clearDots();
        repaint();
        formListener.updateSubjectInfo(currentSubject);
    }

    private int getRandomDotIndex(){
        return random.nextInt(dots.size());
    }

    @Override
    protected synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(int i = 0; i < dots.size(); i++){
            if(isVisible.get(i)){
                dots.get(i).paint(g);
            }
        }
        /*for (Dot d:dots){
            d.paint(g);
        }*/
        repaint();

    }

    private void nextVisibleDot(Dot dot){
        int currentClockNum = dot.getClockNum();
        int nextClockNum = getNextClockNum(currentClockNum);
        for (int i = 0; i < dots.size(); i++){
            if(dots.get(i).getClockNum() != nextClockNum){
                isVisible.set(i, false);
            }
            else{
                isVisible.set(i, true);
                dots.get(i).incrementNumTimesShow();
                if(dots.get(i).getNumTimesShown() >= 3){
                    alreadyUsedClockNums.add(dots.get(i).getClockNum());
                }
            }
        }
    }

    private int getNextClockNum(int currentClockNum){
        Integer[] possibleClockNums = clockOpposites.get(currentClockNum);
        int nextClockNum = possibleClockNums[random.nextInt(possibleClockNums.length)];

        if(alreadyUsedClockNums.contains(nextClockNum)){
            int validClockNum = 0;
            for(Integer cNum: possibleClockNums){
                if(!alreadyUsedClockNums.contains(cNum)){
                    validClockNum = cNum;
                }

            }
            if(validClockNum != 0){
                nextClockNum = validClockNum;
            }
            else{
                nextClockNum = getNextClockNum(currentClockNum +1);
            }
        }
        return  nextClockNum;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        //continueTrial(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //System.out.println("Press!");
        continueTrial(e);

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //System.out.println("Released!");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //System.out.println("Entered!");
        //continueTrial(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //System.out.println("Exited!");
    }
}
