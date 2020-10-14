package nutriscore;

import java.io.IOException;
import javax.swing.JFrame;

/**
 *
 * @author arthu
 */
public class Nutriscore {

    public static void main(String[] args) throws IOException, IncorrectScoreException {

        JFrame frame = new JFrame();

        NutriModel model = new NutriModel();
        MyPanel panel = new MyPanel(model);

        frame.setContentPane(panel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(MyPanel.defaultWidth, MyPanel.defaultHeight);
        frame.setVisible(true);

    }

}
