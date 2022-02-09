package com.me.tmw.tetris;

public class GameBoard {

    int[][] placed = new int[10][20];

    public GameBoard() {
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 20; y++) {
                placed[x][y] = (int) (Math.random() * 6 - 1);
            }
        }
    }

}
