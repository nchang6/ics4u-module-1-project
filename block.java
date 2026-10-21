/**
 * Author: Noah Chang
 * Due date: Oct 21, 2026
 * Description: My own version of the game Block Blast
 */

public class block {

    //Stores the shape of the block as a 2D integer array
    private int[][] shape;

    //Records coords of "anchor"
    private int anchorRow;
    private int anchorColumn;

    public block() {
        // Chooses an integer to determine the shape
        int type = (int) (Math.random() * 12);

        // Define all twelve pieces using one array per row
        if (type == 0) {
            shape = new int[][] {{1}};
        } else if (type == 1) {
            shape = new int[][] {{1, 1}};
        } else if (type == 2) {
            shape = new int[][] {{1}, {1}};
        } else if (type == 3) {
            shape = new int[][] {{1, 1, 1}};
        } else if (type == 4) {
            shape = new int[][] {{1}, {1}, {1}};
        } else if (type == 5) {
            shape = new int[][] {{1, 1}, {1, 1}};
        } else if (type == 6) {
            shape = new int[][] {{1, 0}, {1, 0}, {1, 1}};
        } else if (type == 7) {
            shape = new int[][] {{0, 1}, {0, 1}, {1, 1}};
        } else if (type == 8) {
            shape = new int[][] {{1, 1, 1}, {0, 1, 0}};
        } else if (type == 9) {
            shape = new int[][] {{0, 1, 1}, {1, 1, 0}};
        } else if (type == 10) {
            shape = new int[][] {{1, 1, 1, 1}};
        } else {
            shape = new int[][] {{1}, {1}, {1}, {1}};
        }

        // Scans for first occupied piece, saves as "anchor"
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] == 1) {
                    anchorRow = row;
                    anchorColumn = col;
                    return;
                }
            }
        }
    }

    //Returns the shape that is the 2D array
    public int[][] getShape() {
        return shape;
    }

    //Returns anchor row
    public int getAnchorRow() {
        return anchorRow;
    }

    //Returns anchor column
    public int getAnchorColumn() {
        return anchorColumn;
    }


    //Returns number of filled squares
    public int countSquares() {
        int count = 0;
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                count += shape[row][col];
            }
        }
        return count;
    }
}
