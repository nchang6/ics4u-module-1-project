import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

public class grid {
    // keeps occupancy separate from display
    private int cellSize = 50;
    private int[][] cells;
    private JButton[][] buttons;

    //constructor sets # of rows and columns
    public grid(int rows, int columns) {
        cells = new int[rows][columns];
        buttons = new JButton[rows][columns];
    }

    /**
     * Checks to ensure piece can be placed on the board
     * @param piece testing piece
     * @param clickedRow board row for anchor
     * @param clickedColumn board column for anchor
     * @return true if can be placed
     */
    public boolean canPlace(block piece, int clickedRow, int clickedColumn) {

        //duplicates the 2D array "shape"
        int[][] shape = piece.getShape();

        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] == 1) {
                    // translates shape coords into board coords
                    int boardRow = clickedRow + row - piece.getAnchorRow();
                    int boardCol = clickedColumn + col - piece.getAnchorColumn();

                    // rejects out of bounds squares 
                    if (boardRow < 0 || boardRow >= cells.length || boardCol < 0 || boardCol >= cells[0].length) {
                        return false;
                    }

                    // rejects if overlap with occupied
                    if (cells[boardRow][boardCol] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * validates the block can be placed then places; does not clear rows 
     * @param piece piece to place
     * @param clickedRow board row for anchor
     * @param clickedColumn board column for anchor
     * @return returns true if placed and false if not
     */
    public boolean placeBlock(block piece, int clickedRow, int clickedColumn) {

        // check board first
        if (!canPlace(piece, clickedRow, clickedColumn)) {
            return false;
        }

        // same logic as previous function
        int[][] shape = piece.getShape();
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] == 1) {

                    int boardRow = clickedRow + row - piece.getAnchorRow();
                    int boardCol = clickedColumn + col - piece.getAnchorColumn();

                    // mutate the board only after validation
                    cells[boardRow][boardCol] = 1;
                }
            }
        }
        updatePanel();
        return true;
    }

    /**
     * tries every board placement
     * @param piece piece to place
     * @return true if legal placement exists
     */
    public boolean hasSpace(block piece) {
        // try every board cell as an anchor
        for (int row = 0; row < cells.length; row++) {
            for (int col = 0; col < cells[row].length; col++) {
                if (canPlace(piece, row, col)) {
                    return true;
                }
            }
        }
        return false;
    }

    
    /**
     * finds full rows and columns and clears
     *
     * @return completed rows and columns
     */
    public int clearGrid() {
        // temp arrays to know which to clear
        boolean[] fullRows = new boolean[cells.length];
        boolean[] fullColumns = new boolean[cells[0].length];
        int lines = 0;

        // check every row and column before clearing anything
        for (int row = 0; row < cells.length; row++) {
            fullRows[row] = checkFill(cells[row]);
            if (fullRows[row]) {
                lines++;
            }
        }

        // copies column as arow then checks if full
        int[] temp = new int[cells.length];
        for (int col = 0; col < cells[0].length; col++) {
            for (int row = 0; row < cells.length; row++) {
                temp[row] = cells[row][col];
            }
            fullColumns[col] = checkFill(temp);
            if (fullColumns[col]) {
                lines++;
            }
        }

        // clears full rows and columns
        for (int row = 0; row < cells.length; row++) {
            for (int col = 0; col < cells[row].length; col++) {
                if (fullRows[row] || fullColumns[col]) {
                    cells[row][col] = 0;
                }
            }
        }
        updatePanel();
        return lines;
    }

    /**
     * checks if full pretty self explanatory
     * @param values array to inspect
     * @return true if array full
     */
    private boolean checkFill(int[] values) {
        // A single zero proves the line is incomplete. Reaching the end means
        // there are no empty entries, so the line is full.
        for (int i = 0; i < values.length; i++) {
            if (values[i] == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * empties every cell
     */
    public void reset() {
        for (int row = 0; row < cells.length; row++) {
            for (int col = 0; col < cells[row].length; col++) {
                cells[row][col] = 0;
            }
        }
        updatePanel();
    }

    /**
     * Creates a panel with one button per board cell
     * Stores each button in the matching array position
     *
     * @param listener shared action listener
     * @return board panel ready to add
     */
    public JPanel generatePanel(ActionListener listener) {

        // configures the panel
        JPanel panel = new JPanel(
        new GridLayout(cells.length, cells[0].length, 3, 3));
        panel.setBackground(new Color(24, 36, 59));
        panel.setPreferredSize(new Dimension(
                cells[0].length * cellSize, cells.length * cellSize));

        // creates the buttons and registers them so they correspond to the cells
        for (int row = 0; row < cells.length; row++) {
            for (int col = 0; col < cells[row].length; col++) {
                buttons[row][col] = new JButton();
                buttons[row][col].setOpaque(true);
                buttons[row][col].setBorderPainted(false);
                buttons[row][col].addActionListener(listener);
                panel.add(buttons[row][col]);
            }
        }
        updatePanel();
        return panel;
    }

    /**
     * returns the array of buttons
     * @return board buttons
     */
    public JButton[][] getButtons() {
        return buttons;
    }

    public void updatePanel() {
        // renders occupancy as colors
        for (int row = 0; row < cells.length; row++) {
            for (int col = 0; col < cells[row].length; col++) {
                if (buttons[row][col] != null) {
                    if (cells[row][col] == 0) {
                        buttons[row][col].setBackground(new Color(225, 233, 244));
                    } else {
                        buttons[row][col].setBackground(new Color(53, 132, 218));
                    }
                }
            }
        }
    }
}
