package nutriscore;

import java.awt.Color;
import java.awt.Font;
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

    // Distance between the colored boxes and the frame limit
    private static final int outerBorder = 20;
    // Roundedness of the selected rectangle
    private static double arcPercentage = 0.372;
    // Width of the selected contour
    private static double innerBorderPercentage = 0.05;

    private NutriModel model;

    private final String NUTRISCORE_TEXT = "NUTRI-SCORE";
    private int nutriX, nutriY;

    // Colors used for the letters A to E
    private Color colorsList[] = {new Color(5, 116, 24),
        new Color(150, 192, 30),
        new Color(252, 224, 0),
        new Color(235, 149, 0),
        new Color(213, 34, 1)};
    private Color colorText = new Color(109, 109, 109);
    private Color colorBackground = Color.white;
    private String lettersList[] = {"A", "B", "C", "D", "E"};

    public MyPanel(NutriModel newModel) {
        super();
        model = newModel;
        model.registerObserver(this);
        nutriX = 100;
        nutriY = 100;

        this.addKeyListener(new MyKeyListener(model));
    }

    @Override
    public void paintComponent(Graphics g) {

        // Erase everything
        g.setColor(Color.white);
        g.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);

        paintText(g);

        int width = (g.getClipBounds().width - 2 * outerBorder) / 5;
        int panelHeight = g.getClipBounds().height;
        int height = 2 * panelHeight / 3;

        paintFiveRectangles(g, width, panelHeight, height);

        paintSelectedRectangle(g, width, panelHeight, height);

    }

    /**
     * Paint the only selected rectangle, with a specific border.
     *
     * @param g
     * @param width
     * @param panelHeight
     * @param height
     */
    private void paintSelectedRectangle(Graphics g, int width, int panelHeight, int height) {
        int currentValue = model.getValue();

        int arcLength = (int) (width * arcPercentage);
        int x = outerBorder + model.getValue() * width + width / 3 - arcLength;
        int y = panelHeight / 3 + width / 6 - arcLength;

        int totalHeight = height - outerBorder - width / 3 + 2 * arcLength;
        int totalWidth = width / 3 + 2 * arcLength;

        // Outer part of the rectangle
        g.setColor(Color.white);
        g.fillRoundRect(x, y, totalWidth, totalHeight, arcLength * 2, arcLength * 2);

        // Central part of the rectangle
        int dx = (int) (width * innerBorderPercentage);
        x += dx;
        y += dx;
        totalWidth -= 2 * dx;
        totalHeight -= 2 * dx;
        g.setColor(colorsList[currentValue]);
        g.fillRoundRect(x, y, totalWidth, totalHeight, arcLength * 2 - dx, arcLength * 2 - dx);
        // Letter
        g.setColor(Color.white);
        x = outerBorder + currentValue * width + width / 2;
        y = panelHeight / 3 + height / 2;
        g.drawString(lettersList[currentValue], x, y);
    }

    /**
     * Paint all the rectangles in their non-selected version.
     */
    private void paintFiveRectangles(Graphics g, int width, int panelHeight, int height) {

        int x, y;

        for (int colorIndex = 0; colorIndex < 5; colorIndex++) {
            Color currentColor = colorsList[colorIndex];
            g.setColor(currentColor);
            x = outerBorder + colorIndex * width;
            y = panelHeight / 3;
            g.fillRect(x, y, width, height - outerBorder);
            // Letter
            g.setColor(currentColor.brighter().brighter());
            x += width / 2;
            y += height / 2;
            g.drawString(lettersList[colorIndex], x, y);
        }
    }

    /**
     * Paint the text
     *
     * @param g
     */
    private void paintText(Graphics g) {
        Font font = new Font("ArabicMedium", Font.BOLD, 50);
        g.setFont(font);
        g.setColor(colorText);
        g.drawString(NUTRISCORE_TEXT, nutriX, nutriY);
    }

    @Override
    public void update(Observable o, Object arg) {
        repaint();
    }

}
