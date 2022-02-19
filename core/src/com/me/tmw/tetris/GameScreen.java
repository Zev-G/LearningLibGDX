package com.me.tmw.tetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GameScreen extends ScreenAdapter {

    private static final float BLOCK_SIZE = 20;
    private static final float BLOCK_SPACING = 2;
    private static final Color[] COLORS = { Color.CYAN, Color.YELLOW, Color.PURPLE, Color.GREEN, Color.BLUE, Color.RED, Color.ORANGE };

    private final Tetris game;
    private final GameBoard board = new GameBoard();

    public GameScreen(Tetris game) {
        this.game = game;
        game.shapeRenderer.setAutoShapeType(true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        game.batch.begin();

        // Important values
        float totalWidth = (BLOCK_SIZE + BLOCK_SPACING) * 10;
        float totalHeight = (BLOCK_SIZE + BLOCK_SPACING) * 20;

        // Generate lines
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        game.shapeRenderer.setColor(Color.GRAY);
//        for (int x = 0; x <= 10; x++) {
//            float pos = (BLOCK_SIZE + BLOCK_SPACING) * x;
//            game.shapeRenderer.rectLine(new Vector2(pos, 0), new Vector2(pos, totalHeight), 1f);
//        }
//        for (int y = 0; y <= 20; y++) {
//            float pos = (BLOCK_SIZE + BLOCK_SPACING) * y;
//            game.shapeRenderer.rectLine(new Vector2(0, pos), new Vector2(totalWidth, pos), 1f);
//        }

        // Place blocks
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 20; y++) {
                int placed = board.placed[x][y];

                if (placed != -1) {
                    float posX = (BLOCK_SIZE + BLOCK_SPACING) * x + BLOCK_SPACING;
                    float posY = (BLOCK_SIZE + BLOCK_SPACING) * y + BLOCK_SPACING;

                    Color color = COLORS[placed];
                    game.shapeRenderer.setColor(color);
                    game.shapeRenderer.rect(posX, posY, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }
        game.shapeRenderer.end();

//        game.batch.end();
    }

}
