package com.ihm.tp1;

/**
 *
 * @author yahya0411
 */
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Demineur {
    private class MineBtn extends JButton {
        int r;
        int c;

        public MineBtn(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }

    int tileSize = 70;
    int numRows = 8;
    int numCols = numRows;
    int boardWidth = numCols * tileSize;
    int boardHeight = numRows * tileSize;
    
    JFrame frame = new JFrame("Demineur");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();

    int mineCount = 10;
    MineBtn[][] board = new MineBtn[numRows][numCols];
    ArrayList<MineBtn> mineList;
    Random random = new Random();

    int tilesClicked = 0; //goal is to click all tiles except the ones containing mines
    boolean gameOver = false;

    Demineur() {
        // frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textLabel.setFont(new Font("Arial", Font.BOLD, 25));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Demineur: " + Integer.toString(mineCount));
        textLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(numRows, numCols)); //8x8
        frame.add(boardPanel);

        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                MineBtn tile = new MineBtn(r, c);
                board[r][c] = tile;

                tile.setFocusable(false);
                tile.setMargin(new Insets(0, 0, 0, 0));
                tile.setFont(new Font("Arial Unicode MS", Font.PLAIN, 45));
                // tile.setText("💣");
                tile.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (gameOver) {
                            return;
                        }
                        MineBtn tile = (MineBtn) e.getSource();

                        //left click
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            if (tile.getText() == "") {
                                if (mineList.contains(tile)) {
                                    revelerMines();
                                }
                                else {
                                    testMine(tile.r, tile.c);
                                }
                            }
                        }
                        //right click
                        else if (e.getButton() == MouseEvent.BUTTON3) {
                            if (tile.getText() == "" && tile.isEnabled()) {
                                tile.setText("🚩");
                            }
                            else if (tile.getText() == "🚩") {
                                tile.setText("");
                            }
                        }
                    } 
                });

                boardPanel.add(tile);
                
            }
        }

        frame.setVisible(true);

        setMines();
    }

    void setMines() {
        mineList = new ArrayList<MineBtn>();
        int mineGauche = mineCount;
        while (mineGauche > 0) {
            int r = random.nextInt(numRows); //0-7
            int c = random.nextInt(numCols);

            MineBtn tile = board[r][c]; 
            if (!mineList.contains(tile)) {
                mineList.add(tile);
                mineGauche -= 1;
            }
        }
    }

    void revelerMines() {
        for (int i = 0; i < mineList.size(); i++) {
            MineBtn tile = mineList.get(i);
            tile.setText("💣");
        }

        gameOver = true;
        textLabel.setText("Game Over 😢 ");
    }

    void testMine(int r, int c) {
        if (r < 0 || r >= numRows || c < 0 || c >= numCols) {
            return;
        }

        MineBtn tile = board[r][c];
        if (!tile.isEnabled()) {
            return;
        }
        tile.setEnabled(false);
        tilesClicked += 1;

        int minesExsist = 0;

        //top 3
        minesExsist += ChercheMine(r-1, c-1);  //top left
        minesExsist += ChercheMine(r-1, c);    //top
        minesExsist += ChercheMine(r-1, c+1);  //top right

        //left and right
        minesExsist += ChercheMine(r, c-1);    //left
        minesExsist += ChercheMine(r, c+1);    //right

        //bottom 3
        minesExsist += ChercheMine(r+1, c-1);  //bottom left
        minesExsist += ChercheMine(r+1, c);    //bottom
        minesExsist += ChercheMine(r+1, c+1);  //bottom right

        if (minesExsist > 0) {
            tile.setText(Integer.toString(minesExsist));
        }
        else {
            tile.setText("");
            
            //top 3
            testMine(r-1, c-1);    //top left
            testMine(r-1, c);      //top
            testMine(r-1, c+1);    //top right

            //left and right
            testMine(r, c-1);      //left
            testMine(r, c+1);      //right

            //bottom 3
            testMine(r+1, c-1);    //bottom left
            testMine(r+1, c);      //bottom
            testMine(r+1, c+1);    //bottom right
        }

        if (tilesClicked == numRows * numCols - mineList.size()) {
            gameOver = true;
            textLabel.setText("Gagner 😍!");
        }
    }

    int ChercheMine(int r, int c) {
        if (r < 0 || r >= numRows || c < 0 || c >= numCols) {
            return 0;
        }
        if (mineList.contains(board[r][c])) {
            return 1;
        }
        return 0;
    }
}

