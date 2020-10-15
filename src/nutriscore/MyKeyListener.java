package nutriscore;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author arthu
 */
public class MyKeyListener implements KeyListener {

    NutriModel model;

    public MyKeyListener(NutriModel m) {
        model = m;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                model.increaseValue(false);
                break;
            case KeyEvent.VK_RIGHT:
                model.increaseValue(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
