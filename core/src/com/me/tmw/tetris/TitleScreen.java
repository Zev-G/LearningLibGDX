package com.me.tmw.tetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class TitleScreen extends ScreenAdapter {

    private final Tetris game;

    private final String text = "TETRIS";
    private GlyphLayout layout;

    private float time = 0;

    public TitleScreen(Tetris game) {
        this.game = game;
        layout = new GlyphLayout(game.font, text);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.SPACE) {
                    game.setScreen(new GameScreen(game));
                }
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        time += delta;
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.font.getData().setScale(5);
        writeOnScreen(text, 0.5f, 0.75f);
        if (time % 1 < 0.6) {
            game.font.getData().setScale(2);
            writeOnScreen("Press SPACE to play", 0.5f, 0.5f);
        }
        game.batch.end();
    }

    private void writeOnScreen(String text, float xPercent, float yPercent) {
        GlyphLayout layout = new GlyphLayout(game.font, text);
        game.font.draw(game.batch, text,
                (Gdx.graphics.getWidth() * xPercent) - (layout.width) / 2,
                (Gdx.graphics.getHeight() * yPercent) - (layout.height) / 2
        );
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

}
