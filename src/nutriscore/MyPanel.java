package nutriscore;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;

/**
 *
 * @author arthu
 */
public class MyPanel extends JPanel implements Observer {

    public static final int defaultHeight = 400;
    public static final int defaultWidth = 800;

    private NutriModel model;

    public MyPanel(NutriModel newModel) {
        super();
        model = newModel;
        model.registerObserver(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.gray);
        int width = g.getClipBounds().width;
        int height = g.getClipBounds().height;
        g.fillRect(0, 0, width, height);

        Color innerColor;
        innerColor = switch (model.getValue()) {
            case 0 ->
                Color.blue;
            case 1 ->
                Color.green;
            case 2 ->
                Color.yellow;
            case 3 ->
                Color.orange;
            case 4 ->
                Color.red;
            default ->
                Color.white;
        };

        g.setColor(innerColor);
        int offset = 30;
        g.fillRect(offset, offset, width - 2 * offset, height - 2 * offset);
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("Panel.update: object is " + o
                + ", new value is " + arg);
        repaint();
    }

}
