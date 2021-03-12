package yusuf.play;

import yusuf.ai.AiBoard;
import yusuf.game.GameBoard;

import javax.swing.JFrame;

/**
 * Created with IntelliJ IDEA.
 * User: Yusuf
 * Date: 11/5/13
 * Time: 11:19 PM
 * To change this template use File | Settings | File Templates.
 */
class Play extends JFrame {

    public static void main(String[] argv) {
        AiBoard aiBoard = new AiBoard();
        //aiBoard.calculateBoardValue(1);
        JFrame jFrame = new JFrame();
        jFrame.setSize(600, 630);
        GameBoard gameBoard = new GameBoard();
        gameBoard.aiBoard = aiBoard;
        jFrame.add(gameBoard);
        jFrame.setVisible(true);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /*for(int a=0; a < 5; a++) {
            for(int b=0; b < 5; b++) {
                for(int c =0; c < 5; c++) {
                    for(int d=0; d < 5; d++) {
                        for(int e=0; e < 5; e++) {
                            for(int f=0; f < 5; f++) {
                                for(int g=0; g < 5; g++) {
                                    for(int h=0; h < 5; h++) {
                                        for(int i=0; i < 5; i++) {
                                            for(int j = 1; j < 3; j ++) {
                                                println(aiBoard.boardValue[a][b][c][d][e][f][g][h][i][j]);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }*/
    }
}
