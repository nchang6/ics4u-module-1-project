import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class main {
    // creates the board
    grid board = new grid(8, 8);
    block currentBlock;
    
    // set up for the game
    JButton[][] preview = new JButton[4][4];
    JButton restart = new JButton("New Game");
    JLabel scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
    JLabel linesLabel = new JLabel("Lines cleared: 0", SwingConstants.CENTER);
    JLabel message = new JLabel("Click a board tile to place the gold square there.", SwingConstants.CENTER);
    
    // stores score and while running
    int score = 0;
    int totalLines = 0;
    boolean gameOver = false;

    public main() {

        // creates the window and layout
        JFrame window = new JFrame("Block Blast");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout(16, 12));

        //style the heading
        JLabel title = new JLabel("BLOCK BLAST", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(new Color(24, 36, 59));
        title.setPreferredSize(new Dimension(680, 60));
        window.add(title, BorderLayout.NORTH);

        // one shared listener
        BtnListener listener = new BtnListener();
        window.add(board.generatePanel(listener), BorderLayout.CENTER);

        // area for the instructions and preview shape
        JPanel sidePanel = new JPanel(new BorderLayout(0, 12));
        sidePanel.setPreferredSize(new Dimension(250, 400));

        // stacks the preview block with the info
        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 22));
        infoPanel.add(scoreLabel);
        infoPanel.add(linesLabel);
        infoPanel.add(new JLabel("YOUR BLOCK", SwingConstants.CENTER));
        sidePanel.add(infoPanel, BorderLayout.NORTH);

        // builds the 4x4 block which previews the pieces
        JPanel previewPanel = new JPanel(new GridLayout(4, 4, 3, 3));
        for (int row = 0; row < preview.length; row++) {
            for (int col = 0; col < preview[row].length; col++) {
                
                // no listener attached
                JPanel holder = new JPanel(new BorderLayout());
                preview[row][col] = new JButton();
                preview[row][col].setOpaque(true);
                preview[row][col].setBorderPainted(false);
                preview[row][col].setFont(new Font("Arial", Font.BOLD, 12));
                holder.add(preview[row][col]);
                previewPanel.add(holder);
            }
        }
        sidePanel.add(previewPanel, BorderLayout.CENTER);

        // adds a new game button and info
        JPanel helpPanel = new JPanel(new GridLayout(5, 1));
        helpPanel.add(new JLabel("GOLD = the tile you click", SwingConstants.CENTER));
        helpPanel.add(new JLabel("Fill a row or column to clear it.", SwingConstants.CENTER));
        helpPanel.add(new JLabel("1 point per square, 10 per line.", SwingConstants.CENTER));
        helpPanel.add(new JLabel("The whole block must fit.", SwingConstants.CENTER));
        restart.addActionListener(listener);
        helpPanel.add(restart);
        sidePanel.add(helpPanel, BorderLayout.SOUTH);
        window.add(sidePanel, BorderLayout.EAST);

        //start game and go!
        message.setPreferredSize(new Dimension(680, 44));
        window.add(message, BorderLayout.SOUTH);
        startGame();
        window.pack();
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    /**
     * application entry point
     *
     * @param args
     */
    public static void main(String[] args) {
        new main();
    }

    // starts the game
    public void startGame() {
        
        // clears board and resets variables
        board.reset();
        score = 0;
        totalLines = 0;
        gameOver = false;
        
        // resets messages
        scoreLabel.setText("Score: 0");
        linesLabel.setText("Lines cleared: 0");
        message.setText("Click a board tile to place the gold square there.");
        
        // generates new piece
        currentBlock = new block();
        updatePreview();
    }

    // draws current piece panel, highlights anchor in gold
    public void updatePreview() {
        // read the current shape and visit all preview positions
        int[][] shape = currentBlock.getShape();
        for (int row = 0; row < preview.length; row++) {
            for (int col = 0; col < preview[row].length; col++) {
                boolean filled = row < shape.length && col < shape[row].length  && shape[row][col] == 1;
                
                //resets visibility and updates based on fileld satus
                preview[row][col].setVisible(filled);
                preview[row][col].setText("");
                preview[row][col].setBackground(new Color(53, 132, 218));
                
                // highlights anchor block
                if (filled && row == currentBlock.getAnchorRow()
                        && col == currentBlock.getAnchorColumn()) {
                    preview[row][col].setBackground(new Color(245, 190, 45));
                    preview[row][col].setForeground(new Color(57, 39, 0));
                    preview[row][col].setText("+");
                }
            }
        }
    }

    /**
     * attempts the move
     * @param row board row to receive anchor
     * @param col board column to receive anchor
     */
    public void playTurn(int row, int col) {
        // stops on game over
        if (gameOver) {
            return;
        }
        // attempts placement if doesn't fit updates text
        if (!board.placeBlock(currentBlock, row, col)) {
            message.setText("That block won't fit there. Try another tile.");
            return;
        }

        // awards points on successful placement
        score += currentBlock.countSquares();
        int cleared = board.clearGrid();
        score += cleared * 10;
        totalLines += cleared;
        scoreLabel.setText("Score: " + score);
        linesLabel.setText("Lines cleared: " + totalLines);

        // feedback based on cleared lines
        if (cleared > 0) {
            message.setText("Lines cleared: " + cleared + "! Place your next block.");
        } else {
            message.setText("Block placed! Click a tile for the next gold square.");
        }

        // generates next block, checks if placeable
        currentBlock = new block();
        updatePreview();
        if (!board.hasSpace(currentBlock)) {
            gameOver = true;
            message.setText("Game over! No space for this block. Click New Game to restart.");
        }
    }

    public class BtnListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
    
            if (e.getSource() == restart) {
                startGame();
                return;
            }

            // find the object that triggered the event
            JButton[][] buttons = board.getButtons();
            for (int row = 0; row < buttons.length; row++) {
                for (int col = 0; col < buttons[row].length; col++) {
                    if (e.getSource() == buttons[row][col]) {
                        playTurn(row, col);
                        return;
                    }
                }
            }
        }
    }
}
