import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SubjectInfo {
    private String subjectID;
    private ArrayList<Guess> guessList;
    public Guess currentGuess;
    public int numGuesses;
    private int accurateClick;
    private int missedClick;

    private int targetSize;
    private int testRadius;

    private double indexOfDifficulty;

    private double averageMotionTime;

    @Override
    public String toString() {
        return subjectID + ", " + targetSize + ", " + testRadius + ", " + indexOfDifficulty + ", " + averageMotionTime + ", "  + accurateClick + ", " + missedClick + "\n";
    }
    // "subjectID, targetSize, testRadius, indexOfDifficulty, averageMotionTime, accurateClicks, missedClicks\n"

    public SubjectInfo() {
        subjectID = "";
        targetSize = 0;
        testRadius = 0;
        accurateClick = 0;
        missedClick = 0;
        guessList = new ArrayList<Guess>();
        currentGuess = new Guess();
        numGuesses = 0;
    }

    public SubjectInfo(String subjectID, int targetSize, int testRadius){
        this.subjectID = subjectID;
        this.targetSize = targetSize;
        this.testRadius = testRadius;
        accurateClick = 0;
        missedClick = 0;
        guessList = new ArrayList<Guess>();
        currentGuess = new Guess();
        numGuesses = 0;
    }

    public void resetSubject(){
        subjectID = "";
        targetSize = 0;
        testRadius = 0;
        accurateClick = 0;
        missedClick = 0;
        guessList.clear();
        currentGuess.resetGuess();
        numGuesses = 0;
    }

    public void saveSubjectInfo(){
        try{
            File file = new File("trial-results.txt");
            File file2 = new File("guess-records.txt");
            FileWriter fileWriter = new FileWriter(file, true);
            FileWriter file2Writer = new FileWriter(file2, true);

            if(file.length() == 0){
                FileWriter title1Writer = new FileWriter(file);
                title1Writer.write("subjectID, targetSize, testRadius, indexOfDifficulty, averageMotionTime, accurateClicks, missedClicks\n");
                title1Writer.close();
            }
            if(file2.length() == 0){
                FileWriter title2Writer = new FileWriter(file2);
                title2Writer.write("subjectID, guessNum, elapsedTime, dotClockNum, missedClicks\n");
                title2Writer.close();
            }
            fileWriter.write(this.toString());
            for(Guess guess: guessList){
                file2Writer.write(subjectID + ", " + guess.toString() + "\n");
            }
            fileWriter.close();
            file2Writer.close();
        }catch (IOException e){
            System.out.println("An error occured.");
            e.printStackTrace();
        }
    }

    public void calculateIndexOfDifficulty(){
        indexOfDifficulty = log2((2 * testRadius)/targetSize);
    }

    public double getIndexOfDifficulty() {
        return indexOfDifficulty;
    }

    public double getAverageMotionTime() {
        return averageMotionTime;
    }

    private double log2(float x){
        return Math.log(x)/Math.log(2);
    }

    public void calulateAverageMotionTime(){
        float motionTimeSum = 0;
        for(Guess guess: guessList){
            motionTimeSum += guess.getElapsedTime();
        }
        averageMotionTime = motionTimeSum/numGuesses;
    }

    public void incrementNumGuesses(){
        numGuesses += 1;
    }

    public void incrementAccurateClicks() { accurateClick += 1; }

    public void incrementMissedClicks() { missedClick += 1; }


    public void recordGuess(int dotClockNum){
        currentGuess.setGuess(numGuesses, dotClockNum);
        //currentGuess.setMissedClicks(missedClick);
        guessList.add(currentGuess);
        currentGuess = new Guess();
        currentGuess.resetGuess();
    }



}