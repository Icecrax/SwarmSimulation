package util;

import javax.swing.*;

/**
 * @author Steliyan Syarov 11712196
 * @date 30.10.2018
 *
 * @brief The class represents an air current or a wind.
 */
public class AirCurrent implements ITimeBased {
    private Vector[][] grid;
    private int screenResolution, columns, rows;

    private Timer timer;

    private boolean isWind;

    /**
     * POST: Creates a matrix with a certain number of columns and rows,
     * depending of the size of the window and the size of portions in which the window will be 'cut'.
     * Fills the matrix with vectors and starts a timer, which will turn periodically on/off the variable isWind.
     */
    public AirCurrent(){
        isWind = true;
        screenResolution = 15;
        columns = Util.getDimension().width / screenResolution;
        rows = Util.getDimension().height / screenResolution;
        grid = new Vector[columns][rows];
        fillGrid(grid);

        timer = new Timer(getDelay(), evt -> {
            if(timer.getDelay() == getDelay()){
                isWind = !isWind;
                timer.restart();
            }
        });
        timer.start();
    }

    /**
     * POST: fills the passed grid with vectors, determining the direction in which the wind is blowing
     * @param grid takes an empty [][][] matrix
     */
    private void fillGrid(Vector[][] grid){
        float xVar = 0;
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                float delta = Util.transform(Util.noise(xVar), 0, 1, 0, (float) Math.PI * 2);
                grid[i][j] = new Vector((float) Math.sin(delta), (float) Math.cos(delta), 0);
                xVar += 0.1;
            }
        }
    }

    /**
     * @param position the vector is not NULL
     * @return the corresponding, saved in the grid(matrix), vector, determining the direction in which the wind is blowing
     */
    public Vector lookup(Vector position) {
        int column = ensureRange((int)(position.getX()/screenResolution), columns-1);
        int row = ensureRange((int)(position.getY()/screenResolution), rows-1);
        return grid[column][row].getVector();
    }

    /**
     * @param value any integer
     * @param max max should be bigger than min
     * @return If the passed value exceeds the given bounds [min, max], it's being replaced by the exceeded bound
     */
    private int ensureRange(int value, int max) {
        return Math.min(Math.max(value, 0), max);
    }

    /**
     * Specify the delay in ms until the timer restarts
     * @return the delay in ms
     */
    @Override
    public int getDelay() {
        return 4000;
    }

    /**
     * @return the state of the wind (is it on or off)
     */
    public boolean isWind() {
        return isWind;
    }
}
