package nutriscore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
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

    private long updatePeriod = 10000;

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
     * Change the nutriscore by adding or removing one.
     *
     * @param mustIncrease if true, add one; else, remove one.
     */
    public void increaseValue(boolean mustIncrease) {
        if (mustIncrease) {
            this.nutriscore++;
        } else {
            this.nutriscore--;
        }
        // Keep the nutriscore between 0 and max.
        nutriscore = Math.max(0, Math.min(MAX_SCORE, nutriscore));
        notifyObservers();
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
     *
     * @param o
     */
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    /**
     * Remove an observer
     *
     * @param o
     */
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

    /**
     * This task generates a random number and sets the model
     */
    private static class MyTask extends TimerTask {

        private NutriModel model;
        private URL url;

        public MyTask(NutriModel modelParam) {
            model = modelParam;
            try {
                url = new URL("https://www.random.org/integers/?num=1&min=0&max="
                        + MAX_SCORE
                        + "&col=1&base=10&format=plain&rnd=new");

            } catch (MalformedURLException e) {
                System.out.println("Malformed URL at task creation");
            }
        }

        @Override
        public void run() {
            int valFromServer = performSingleRequest();
            try {
                model.setValue(valFromServer);
            } catch (IncorrectScoreException ex) {
                Logger.getLogger(NutriModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        /**
         * Request a value from the given URL and return it as an integer.
         *
         * @return a random integer obtained from the URL, or -1 in case of an
         * error.
         */
        private int performSingleRequest() {

            int result = -1;

            try {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + conn.getResponseCode());
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));

                String output;
                while ((output = br.readLine()) != null) {
                    result = Integer.parseInt(output);
                }

                conn.disconnect();
            } catch (IOException e) {
                System.out.println("IO exception");
            }
            return result;
        }
    }
}
