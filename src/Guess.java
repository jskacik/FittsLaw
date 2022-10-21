public class Guess {


    private int guessNum;
    private float elapsedTime;

    private int dotClockNum;

    private int missedClicks;

    public Guess()
    {
        this.guessNum = 0;
        this.elapsedTime = 0;
        this.dotClockNum = 0;
        this.missedClicks = 0;

    }

    public void incrementGuessNum(){
        guessNum += 1;
    }

    public void incrementMissedClicks(){ missedClicks += 1; }

    public float getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(float elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public void setMissedClicks(int missedClicks) {
        this.missedClicks = missedClicks;
    }

    public void setGuessNum(int guessNum){
        this.guessNum = guessNum;
    }
    public void resetGuess(){
        this.guessNum = 0;
        this.elapsedTime = 0;
        this.dotClockNum = 0;
        this.missedClicks = 0;
    }

    public void setGuess(int guessNum, int dotClockNum){
        this.guessNum = guessNum;
        this.dotClockNum = dotClockNum;
    }



    @Override
    public String toString() {
        return guessNum + ", " + elapsedTime + ", " + dotClockNum + ", " + missedClicks;
    }
}

