package yusuf.game;

import yusuf.ai.AiBoard;
import yusuf.element.Pair;
import yusuf.element.PairToPair;
import yusuf.element.PointPosition;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by YUSUF on 9/15/15.
 */
public class GameBoard extends JPanel {

    private int draggingX;
    private int playerTurn;
    private int draggingY;
    private int draggingCircleIndex;
    private Color draggingColor;
    private MouseDrag mouseDrag;
    public AiBoard aiBoard;
    private boolean dragging = false;
    private int radius = 30;
    private int rectX = 50;
    private int rectY = 50;
    private int rectHeight = 500;
    private int rectWidth = 500;
    private PointPosition[] pointPositions;
    private ArrayList<PairToPair> winPos;
    private ArrayList<PairToPair> drawPos;
    private ArrayList<PairToPair> lossPos;

    public GameBoard() {
        this.pointPositions = new PointPosition[9];
        setBackground(Color.WHITE);
        this.mouseDrag = new MouseDrag();
        addMouseListener(this.mouseDrag);
        addMouseMotionListener(this.mouseDrag);
        this.setPointPosition();
        this.playerTurn = 1;
        repaint();
    }

    private final class MouseDrag extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent m) {
            if(playerTurn == 1) {
                dragging = isInsideCircle(m.getPoint());
            }
        }

        @Override
        public void mouseReleased(MouseEvent m) {
            if(dragging) {
                setInsideNewPos(m.getPoint());
            }
            dragging = false;
        }

        @Override
        public void mouseDragged(MouseEvent m) {
            if (dragging) {
                draggingX = m.getX();
                draggingY = m.getY();
            }
            repaint();
        }
    }

    public boolean isInsideCircle(Point point) {
        for(int i = 0; i < 9; i++) {
            if(this.pointPositions[i].isOccupied && point.x > this.pointPositions[i].posX - this.radius
                    && point.x < this.pointPositions[i].posX + this.radius && point.y > this.pointPositions[i].posY - this.radius
                    && point.y < this.pointPositions[i].posY + this.radius && this.pointPositions[i].player == 1) {
                this.pointPositions[i].isOccupied = false;
                this.draggingColor = this.pointPositions[i].color;
                this.draggingCircleIndex = i;
                return true;
            }
        }
        return false;
    }

    public void setInsideNewPos(Point point) {
        boolean newPos = false;
        ArrayList<Pair> moves = this.aiBoard.move.get(new Pair(this.pointPositions[draggingCircleIndex].row, this.pointPositions[draggingCircleIndex].col));
        for(int j = 0; j < moves.size(); j++) {
            int i = this.getPositionByRowCol(moves.get(j).x, moves.get(j).y);
            if(!this.pointPositions[i].isOccupied && i != draggingCircleIndex && point.x > this.pointPositions[i].posX - this.radius
                    && point.x < this.pointPositions[i].posX + this.radius && point.y > this.pointPositions[i].posY - this.radius
                    && point.y < this.pointPositions[i].posY + this.radius) {
                this.pointPositions[i].isOccupied = true;
                if(this.pointPositions[this.draggingCircleIndex].player == 1) {
                    this.pointPositions[i].color = Color.RED;
                    this.pointPositions[i].player = 1;
                } else {
                    this.pointPositions[i].color = new Color(38,215,44);
                    this.pointPositions[i].player = 2;
                }
                if(pointPositions[this.draggingCircleIndex].player == 1) {
                    this.aiBoard.board[pointPositions[this.draggingCircleIndex].row][pointPositions[this.draggingCircleIndex].col] = 0;
                    this.aiBoard.board[pointPositions[i].row][pointPositions[i].col] = 2;
                } else {
                    this.aiBoard.board[pointPositions[this.draggingCircleIndex].row][pointPositions[this.draggingCircleIndex].col] = 0;
                    this.aiBoard.board[pointPositions[i].row][pointPositions[i].col] = 4;
                }
                this.playerTurn = 2;
                newPos = true;
                break;
            }
        }
        pointPositions[this.draggingCircleIndex].isOccupied = !newPos;
        pointPositions[this.draggingCircleIndex].player = newPos ? 0 : pointPositions[this.draggingCircleIndex].player;
        if(newPos) {
            repaint();
            this.computerPlay();
        } else {
            repaint();
        }
    }

    public void computerPlay() {
        winPos = new ArrayList<PairToPair>();
        lossPos = new ArrayList<PairToPair>();
        drawPos = new ArrayList<PairToPair>();
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                if(this.aiBoard.board[i][j] == 3 || this.aiBoard.board[i][j] == 4) {
                    ArrayList<Pair> moves = this.aiBoard.move.get(new Pair(i,j));
                    int prevValue = this.aiBoard.board[i][j];
                    this.aiBoard.board[i][j] = 0;
                    for(int k = 0; k < moves.size(); k++) {
                        if(this.aiBoard.board[moves.get(k).x][moves.get(k).y] == 0) {
                            this.aiBoard.board[moves.get(k).x][moves.get(k).y] = 4;
                            int value = this.aiBoard.calculateBoardValueDFS(1, 0);
                            if(value == 2){
                                winPos.add(new PairToPair(new Pair(i,j), new Pair(moves.get(k).x, moves.get(k).y)));
                            } else if(value == 1) {
                                lossPos.add(new PairToPair(new Pair(i,j), new Pair(moves.get(k).x, moves.get(k).y)));
                            } else {
                                drawPos.add(new PairToPair(new Pair(i,j), new Pair(moves.get(k).x, moves.get(k).y)));
                            }
                            this.aiBoard.board[moves.get(k).x][moves.get(k).y] = 0;
                        }
                    }
                    this.aiBoard.board[i][j] = prevValue;
                }
            }
        }
        Random rand = new Random();
        int pos;
        if(winPos.size() > 0) {
            pos = rand.nextInt(winPos.size());
            this.computerMove(winPos.get(pos).fromPair, winPos.get(pos).toPair);
        } else if(drawPos.size() > 0) {
            pos = rand.nextInt(drawPos.size());
            this.computerMove(drawPos.get(pos).fromPair, drawPos.get(pos).toPair);
        } else {
            pos = rand.nextInt(lossPos.size());
            this.computerMove(lossPos.get(pos).fromPair, lossPos.get(pos).toPair);
        }
    }

    public void computerMove(Pair from, Pair to) {
        int fromIndex = this.getPositionByRowCol(from.x, from.y);
        this.pointPositions[fromIndex].isOccupied = false;
        this.pointPositions[fromIndex].player = 0;
        this.aiBoard.board[this.pointPositions[fromIndex].row][this.pointPositions[fromIndex].col] = 0;
        int toIndex = this.getPositionByRowCol(to.x, to.y);
        this.pointPositions[toIndex].isOccupied = true;
        this.pointPositions[toIndex].player = 0;
        this.pointPositions[toIndex].color = new Color(38,215,44);
        this.playerTurn = 1;
        this.aiBoard.board[this.pointPositions[toIndex].row][this.pointPositions[toIndex].col] = 4;
        repaint();
        this.checkWinner();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        //g.fillOval(x, y, this.diameter, this.diameter);
        g.drawRect(this.rectX, this.rectY, this.rectHeight, this.rectWidth);
        g.drawLine(this.rectX, this.rectY, this.rectX + this.rectWidth, this.rectY + this.rectHeight);
        g.drawLine((int)(this.rectX + this.rectWidth)/ 2 + this.rectX / 2, this.rectY, (int)(this.rectX + this.rectWidth)/ 2 + this.rectX / 2, this.rectY + this.rectHeight);
        g.drawLine(this.rectX + this.rectWidth, this.rectY, this.rectX , this.rectY + this.rectHeight);
        g.drawLine(this.rectX, (int)(this.rectY + this.rectHeight)/ 2 + this.rectY / 2, this.rectX + this.rectWidth, (int)(this.rectY + this.rectHeight)/ 2 + this.rectY / 2);

        for(int i = 0; i < 9; i++) {
            if(this.pointPositions[i].isOccupied) {
                g.setColor(this.pointPositions[i].color);
                this.drawCenteredCircle(g, this.pointPositions[i].posX, this.pointPositions[i].posY, this.radius);
            }
        }

        if(this.dragging) {
            g.setColor(this.draggingColor);
            this.drawCenteredCircle(g, this.draggingX, this.draggingY, this.radius);
        }
    }

    public void drawCenteredCircle(Graphics g, int x, int y, int r) {
        x = x - r;
        y = y - r;
        g.fillOval(x, y, 2*r, 2*r);
    }

    public void setPointPosition() {
        this.pointPositions[0] = new PointPosition();
        this.pointPositions[0].row = 0;
        this.pointPositions[0].col = 0;
        this.pointPositions[0].posX = this.rectX;
        this.pointPositions[0].posY = this.rectY;
        this.pointPositions[0].isOccupied = true;
        this.pointPositions[0].player = 1;
        this.pointPositions[0].color = new Color(255, 98, 108);

        this.pointPositions[1] = new PointPosition();
        this.pointPositions[1].row = 0;
        this.pointPositions[1].col = 1;
        this.pointPositions[1].posX = this.rectY + (int) (this.rectWidth/2);
        this.pointPositions[1].posY = this.rectY;
        this.pointPositions[1].isOccupied = true;
        this.pointPositions[1].player = 1;
        this.pointPositions[1].color = new Color(255, 98, 108);

        this.pointPositions[2] = new PointPosition();
        this.pointPositions[2].row = 0;
        this.pointPositions[2].col = 2;
        this.pointPositions[2].posX = this.rectX + this.rectWidth;
        this.pointPositions[2].posY = this.rectY;
        this.pointPositions[2].isOccupied = true;
        this.pointPositions[2].player = 1;
        this.pointPositions[2].color = new Color(255, 98, 108);

        this.pointPositions[3] = new PointPosition();
        this.pointPositions[3].row = 1;
        this.pointPositions[3].col = 0;
        this.pointPositions[3].posX = this.rectX;
        this.pointPositions[3].posY = this.rectY + (int) (this.rectHeight/2);
        this.pointPositions[3].isOccupied = false;

        this.pointPositions[4] = new PointPosition();
        this.pointPositions[4].row = 1;
        this.pointPositions[4].col = 1;
        this.pointPositions[4].posX = this.rectY + (int) (this.rectWidth/2);
        this.pointPositions[4].posY = this.rectY + (int) (this.rectHeight/2);
        this.pointPositions[4].isOccupied = false;

        this.pointPositions[5] = new PointPosition();
        this.pointPositions[5].row = 1;
        this.pointPositions[5].col = 2;
        this.pointPositions[5].posX = this.rectX + this.rectWidth;
        this.pointPositions[5].posY = this.rectY + (int) (this.rectHeight/2);
        this.pointPositions[5].isOccupied = false;

        this.pointPositions[6] = new PointPosition();
        this.pointPositions[6].row = 2;
        this.pointPositions[6].col = 0;
        this.pointPositions[6].posX = this.rectX;
        this.pointPositions[6].posY = this.rectY + this.rectHeight;
        this.pointPositions[6].isOccupied = true;
        this.pointPositions[6].player = 2;
        this.pointPositions[6].color = new Color(61, 133, 64);

        this.pointPositions[7] = new PointPosition();
        this.pointPositions[7].row = 2;
        this.pointPositions[7].col = 1;
        this.pointPositions[7].posX = this.rectY + (int) (this.rectWidth/2);
        this.pointPositions[7].posY =  this.rectY + this.rectHeight;
        this.pointPositions[7].isOccupied = true;
        this.pointPositions[7].player = 2;
        this.pointPositions[7].color = new Color(61, 133, 64);

        this.pointPositions[8] = new PointPosition();
        this.pointPositions[8].row = 2;
        this.pointPositions[8].col = 2;
        this.pointPositions[8].posX = this.rectX + this.rectWidth;
        this.pointPositions[8].posY =  this.rectY + this.rectHeight;
        this.pointPositions[8].isOccupied = true;
        this.pointPositions[8].player = 2;
        this.pointPositions[8].color = new Color(61, 133, 64);

    }

    public int getPositionByRowCol(int row, int col) {
        for(int i = 0; i < 9; i++) {
            if(this.pointPositions[i].row == row && this.pointPositions[i].col == col) {
                return i;
            }
        }
        return 0;
    }

    public void checkWinner() {
        int curValue = this.aiBoard.checkBoard();
        if(curValue > 0) {
            int playAgain = 0;
            int dialogButton = JOptionPane.YES_NO_OPTION;
            if(curValue == 1) {
                playAgain = JOptionPane.showConfirmDialog (null, "Wow You win. Do you like to play again","Game Over",dialogButton);
            } else if(curValue == 2) {
                playAgain = JOptionPane.showConfirmDialog (null, "Sorry You loss. Do you like to play again","Game Over",dialogButton);
            }
            if (playAgain == JOptionPane.YES_OPTION) {
                this.reSetGame();
                repaint();
            } else {
                System.exit(0);
            }
        }
    }

    public void reSetGame() {
        this.aiBoard.resetBoard();
        this.setPointPosition();
    }

}

