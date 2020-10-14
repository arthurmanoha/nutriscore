package nutriscore;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author arthu
 */
public class NutriModel extends Observable {

    // The nutriscore; value from 0 to 4 represent scores between A and D.
    private int nutriscore;
    private static int MAX_SCORE = 4;

    private ArrayList<Observer> observers;

    private long updatePeriod = 1000;

    /**
     * Constructor with a given score.
     *
     * @param value the score.
     */
    public NutriModel(int value) {
        this.nutriscore = value;
        this.observers = new ArrayList<>();

        Timer timer = new Timer();
        timer.schedule(new MyTask(this), updatePeriod, updatePeriod);
    }

    /**
     * Default constructor.
     *
     */
    public NutriModel() {
        this(0);
    }

    public int getValue() {
        return this.nutriscore;
    }

    /**
     * Set the score to a given value.
     *
     * @param newVal the new score.
     * @throws IncorrectScoreException and keep previous value when the
     * parameter is not a valid score.
     */
    public void setValue(int newVal) throws IncorrectScoreException {
        if (newVal < 0 || newVal > 4) {
            throw new IncorrectScoreException();
        }
        this.nutriscore = newVal;
        notifyObservers();
    }

    /**
     * Add an observer
     */
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    public void unregisterObserver(Observer o) {
        observers.remove(o);
    }

    /**
     * Tell all the observers that a change happened.
     */
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update(this, this.nutriscore);
        }
    }

    private static class MyTask extends TimerTask {

        private NutriModel model;

        public MyTask(NutriModel modelParam) {
            model = modelParam;
        }

        @Override
        public void run() {
            int val = new Random().nextInt(MAX_SCORE + 2);
            System.out.println("Timer task generated value " + val);
            try {
                model.setValue(val);
            } catch (IncorrectScoreException ex) {
                Logger.getLogger(NutriModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
