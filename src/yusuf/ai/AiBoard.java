package yusuf.ai;

import yusuf.element.Pair;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Yusuf
 * Date: 11/5/13
 * Time: 11:04 PM
 * To change this template use File | Settings | File Templates.
 */
/**
 * Not moved player1 is 1
 * Moved player1 2
 * Not moved player2 is 3
 * Moved player2 is 4
 * board value 0 draw
 * board value 1 player1 win
 * board value 2 player2 win
**/
public class AiBoard {
    Boolean[][][][][][][][][][] markBoard;
    Boolean[][][][][][][][][][] visited;
    public int[][][][][][][][][][] boardValue;
    int[] visitedBoard;
    public int[][] board;
    public HashMap<Pair, ArrayList<Pair>> move;
    int maxDepth;

    public AiBoard() {
        this.boardValue = new int[5][5][5][5][5][5][5][5][5][3];
        this.markBoard = new Boolean[5][5][5][5][5][5][5][5][5][3];
        this.visited = new Boolean[5][5][5][5][5][5][5][5][5][3];
        this.visitedBoard = new int[90];
        this.board = new int[3][3];
        this.move = new HashMap<Pair, ArrayList<Pair>>();
        this.setMoves();
        this.board[0][0] = 1;
        this.board[0][1] = 1;
        this.board[0][2] = 1;
        this.board[2][0] = 3;
        this.board[2][1] = 3;
        this.board[2][2] = 3;
        this.maxDepth = 10;
    }

    public int calculateBoardValue(int player) {
        this.visited[this.board[0][0]][this.board[0][1]][this.board[0][2]][this.board[1][0]][this.board[1][1]][this.board[1][2]][this.board[2][0]][this.board[2][1]][this.board[2][2]][player] = true;
        //println(call++);
        if(this.markBoard[this.board[0][0]][this.board[0][1]][this.board[0][2]][this.board[1][0]][this.board[1][1]][this.board[1][2]][this.board[2][0]][this.board[2][1]][this.board[2][2]][player]) {

            return this.boardValue[this.board[0][0]][this.board[0][1]][this.board[0][2]][this.board[1][0]][this.board[1][1]][this.board[1][2]][this.board[2][0]][this.board[2][1]][this.board[2][2]][player];
        }

        int curValue = this.checkBoard();
        if(curValue > 0) {
            //println(call++);
            this.markBoard[this.board[0][0]][this.board[0][1]][this.board[0][2]][this.board[1][0]][this.board[1][1]][this.board[1][2]][this.board[2][0]][this.board[2][1]][this.board[2][2]][player] = true;
            this.boardValue[this.board[0][0]][this.board[0][1]][this.board[0][2]][this.board[1][0]][this.board[1][1]][this.board[1][2]][this.board[2][0]][this.board[2][1]][this.board[2][2]][player] = curValue;
            return curValue;
        }

        int playerValue1, playerValue2, moveValue, nextPlayer;
        if(player == 1) {
            playerValue1 = 1;
            playerValue2 = 2;
            moveValue = 2;
            nextPlayer = 2;
        } else {
            playerValue1 = 3;
            playerValue2 = 4;
            moveValue = 4;
            nextPlayer = 1;
        }
        int[] results = new int[200];
        int resultSize = 0;
        int vCount =0;
        for(int i=0; i < 3; i++ ) {
            for(int j=0; j < 3; j++) {
                if(this.board[i][j] == playerValue1 || this.board[i][j] == playerValue2) {
                    Pair p = new Pair(i,j);
                    ArrayList<Pair> moves = this.move.get(p);
                    p = null;
                    for(int k = 0; k < moves.size(); k++) {
                        if(this.board[moves.get(k).x][moves.get(k).y] == 0) {
                            int prevValue = this.board[i][j];
                            this.board[moves.get(k).x][moves.get(k).y] = moveValue;
                            this.board[i][j] = 0;
                            if(this.visited[this.board[0][0]][this.board[0][1]][this.board[0][2]][this.board[1][0]][this.board[1][1]][this.board[1][2]][this.board[2][0]][this.board[2][1]][this.board[2][2]][nextPlayer]) {
                                vCount++;
                            } else {
                                results[resultSize++] = this.calculateBoardValue(nextPlayer);
                            }
                            this.board[i][j] = prevValue;
                            this.board[moves.get(k).x][moves.get(k).y] = 0;
                        }
                    }
                }
            }
        }
        int currentValue;
        boolean drow = false;
        boolean currentWin = false;
        //println("vCount " + vCount);
        //println(resultSize);
        if(resultSize > 0) {
            for(int i = 0; i < resultSize; i++) {
                if(results[i] == player) {
                    currentValue = player;
                    currentWin = true;
                    break;
                } else if(results[i] == 0) {
                    drow = true;
                }
            }
            if(drow) {
                currentValue = 0;
            } else if (currentWin) {
                currentValue = player;
            } else {
                if(player == 1) {
                    currentValue = 2;
                } else {
                    currentValue = 0;
                }
            }
        } else {
            currentValue = 0;
        }


        //marking board value
        this.markBoard[this.board[0][0]][this.board[0][1]][this.board[0][2]][this.board[1][0]][this.board[1][1]][this.board[1][2]][this.board[2][0]][this.board[2][1]][this.board[2][2]][player] = true;
        this.boardValue[this.board[0][0]][this.board[0][1]][this.board[0][2]][this.board[1][0]][this.board[1][1]][this.board[1][2]][this.board[2][0]][this.board[2][1]][this.board[2][2]][player] = currentValue;
        return currentValue;
    }

    /* return the expected value of wining */

    public int calculateBoardValueDFS(int player, int depth) {

        int curValue = this.checkBoard();
        if(depth > this.maxDepth) {
            return -1;
        }
        if(curValue > 0) {
            return curValue;
        }
        int playerValue1, playerValue2, moveValue, nextPlayer;
        if(player == 1) {
            playerValue1 = 1;
            playerValue2 = 2;
            moveValue = 2;
            nextPlayer = 2;
        } else {
            playerValue1 = 3;
            playerValue2 = 4;
            moveValue = 4;
            nextPlayer = 1;
        }
        int[] results = new int[200];
        int resultSize = 0;
        int vCount =0;
        for(int i=0; i < 3; i++ ) {
            for(int j=0; j < 3; j++) {
                if(this.board[i][j] == playerValue1 || this.board[i][j] == playerValue2) {
                    Pair p = new Pair(i,j);
                    ArrayList<Pair> moves = this.move.get(p);
                    for(int k = 0; k < moves.size(); k++) {
                        if(this.board[moves.get(k).x][moves.get(k).y] == 0) {
                            int prevValue = this.board[i][j];
                            this.board[moves.get(k).x][moves.get(k).y] = moveValue;
                            this.board[i][j] = 0;
                            results[resultSize++] = this.calculateBoardValueDFS(nextPlayer, ++depth);
                            this.board[i][j] = prevValue;
                            this.board[moves.get(k).x][moves.get(k).y] = 0;
                        }
                    }
                }
            }
        }
        int currentValue;
        boolean drow = false;
        boolean currentWin = false;
        /*println("vCount " + vCount);
        println(resultSize);*/
        if(resultSize > 0) {
            for(int i = 0; i < resultSize; i++) {
                if(results[i] == player) {
                    currentValue = player;
                    currentWin = true;
                    break;
                } else if(results[i] == 0 || results[i] == -1) {
                    drow = true;
                }
            }
            if(currentWin) {
                currentValue = player;
            } else if (drow) {
                currentValue = 0;
            } else {
                if(player == 1) {
                    currentValue = 2;
                } else {
                    currentValue = 0;
                }
            }
        } else {
            currentValue = 0;
        }
        return currentValue;
    }

    public int checkBoard() {
        if((this.board[0][0] == this.board[0][1]) && (this.board[0][1] == this.board[0][2]) && (this.board[0][0] == 2)) {
            return 1;
        } else if((this.board[0][0] == this.board[0][1]) && (this.board[0][1] == this.board[0][2]) && (this.board[0][0] == 4)) {
            return 2;
        } else if((this.board[0][0] == this.board[1][0]) && (this.board[1][0] == this.board[2][0]) && (this.board[0][0] == 2)) {
            return 1;
        } else if((this.board[0][0] == this.board[1][0]) && (this.board[1][0] == this.board[2][0]) && (this.board[0][0] == 4)) {
            return 2;
        } else if((this.board[0][0] == this.board[1][1]) && (this.board[1][1] == this.board[2][2]) && (this.board[0][0] == 2)) {
            return 1;
        } else if((this.board[0][0] == this.board[1][1]) && (this.board[1][1] == this.board[2][2]) && (this.board[0][0] == 4)) {
            return 2;
        } else if((this.board[0][1] == this.board[1][1]) && (this.board[1][1] == this.board[2][1]) && (this.board[0][1] == 2)) {
            return 1;
        } else if((this.board[0][1] == this.board[1][1]) && (this.board[1][1] == this.board[2][1]) && (this.board[0][1] == 4)) {
            return 2;
        } else if((this.board[0][2] == this.board[1][1]) && (this.board[1][1] == this.board[2][0]) && (this.board[0][2] == 2)) {
            return 1;
        } else if((this.board[0][2] == this.board[1][1]) && (this.board[1][1] == this.board[2][0]) && (this.board[0][2] == 4)) {
            return 2;
        } else if((this.board[0][2] == this.board[1][2]) && (this.board[1][2] == this.board[2][2]) && (this.board[0][2] == 2)) {
            return 1;
        } else if((this.board[0][2] == this.board[1][2]) && (this.board[1][2] == this.board[2][2]) && (this.board[0][2] == 4)) {
            return 2;
        } else if((this.board[1][0] == this.board[1][1]) && (this.board[1][1] == this.board[1][2]) && (this.board[1][0] == 2)) {
            return 1;
        } else if((this.board[1][0] == this.board[1][1]) && (this.board[1][1] == this.board[1][2]) && (this.board[1][0] == 4)) {
            return 2;
        } else if((this.board[2][0] == this.board[2][1]) && (this.board[2][1] == this.board[2][2]) && (this.board[2][0] == 2)) {
            return 1;
        } else if((this.board[2][0] == this.board[2][1]) && (this.board[2][1] == this.board[2][2]) && (this.board[2][0] == 4)) {
            return 2;
        } else {
            return 0;
        }
    }

    public void resetBoard() {
        this.board[0][0] = 1;
        this.board[0][1] = 1;
        this.board[0][2] = 1;
        this.board[1][0] = 0;
        this.board[1][1] = 0;
        this.board[1][2] = 0;
        this.board[2][0] = 3;
        this.board[2][1] = 3;
        this.board[2][2] = 3;
    }

    private void setMoves() {
        ArrayList<Pair> m = new ArrayList<Pair>();
        m.add(new Pair(1,0));
        m.add(new Pair(1,1));
        m.add(new Pair(0,1));
        this.move.put(new Pair(0,0), m);
        ArrayList<Pair> m1 = new ArrayList<Pair>();
        m1.add(new Pair(0,0));
        m1.add(new Pair(1,1));
        m1.add(new Pair(0,2));
        this.move.put(new Pair(0,1), m1);
        ArrayList<Pair> m2 = new ArrayList<Pair>();
        m2.add(new Pair(0,1));
        m2.add(new Pair(1,1));
        m2.add(new Pair(1,2));
        this.move.put(new Pair(0,2), m2);
        ArrayList<Pair> m3 = new ArrayList<Pair>();
        m3.add(new Pair(0,0));
        m3.add(new Pair(1,1));
        m3.add(new Pair(2,0));
        this.move.put(new Pair(1,0), m3);
        ArrayList<Pair> m4 = new ArrayList<Pair>();
        m4.add( new Pair(0,0));
        m4.add( new Pair(0,1));
        m4.add( new Pair(0,2));
        m4.add( new Pair(1,0));
        m4.add( new Pair(1,2));
        m4.add( new Pair(2,0));
        m4.add( new Pair(2,1));
        m4.add( new Pair(2,2));
        this.move.put(new Pair(1,1), m4);
        ArrayList<Pair> m5 = new ArrayList<Pair>();
        m5.add( new Pair(0,2));
        m5.add( new Pair(1,1));
        m5.add( new Pair(2,2));
        this.move.put(new Pair(1,2), m5);
        ArrayList<Pair> m6 = new ArrayList<Pair>();
        m6.add( new Pair(1,0));
        m6.add( new Pair(1,1));
        m6.add( new Pair(2,1));
        this.move.put(new Pair(2,0), m6);
        ArrayList<Pair> m7 = new ArrayList<Pair>();
        m7.add( new Pair(2,0));
        m7.add( new Pair(1,1));
        m7.add( new Pair(2,2));
        this.move.put(new Pair(2,1), m7);
        ArrayList<Pair> m8 = new ArrayList<Pair>();
        m8.add( new Pair(2,1));
        m8.add( new Pair(1,1));
        m8.add( new Pair(1,2));
        this.move.put(new Pair(2,2), m8);
    }
}
