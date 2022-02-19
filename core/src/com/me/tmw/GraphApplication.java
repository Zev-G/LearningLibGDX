package com.me.tmw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import javax.script.*;
import java.util.ArrayList;
import java.util.List;

public class GraphApplication implements Program {

    private static float triangleWidth = 10;
    private static float triangleHeight = 10;
    private static float lineWidth = 2;
    private static final float DRAW_TIME = 1;

    private final List<Vector2> points = new ArrayList<>();
    private ShapeRenderer renderer;
    private BitmapFont font;
    private SpriteBatch batch;

    private boolean useCode = false;
    private String code;
    private ScriptContext context;
    private ScriptEngineManager manager;
    private ScriptEngine scriptEngine;

    private boolean playing = false;

    @Override
    public void create() {
        System.setProperty("nashorn.args", "--language=es6");
        this.renderer = new ShapeRenderer();
        this.font = new BitmapFont();
        this.batch = new SpriteBatch();
        this.code = "f(x) = sin(x)";

        System.out.println(format(0.1f));

        if (useCode) {
            this.context = new SimpleScriptContext();
            this.manager = new ScriptEngineManager(getClass().getClassLoader());
            this.scriptEngine = this.manager.getEngineByName("JavaScript");
            this.context.setBindings(scriptEngine.createBindings(), ScriptContext.ENGINE_SCOPE);
        }

        float start = 0;
        float max = 100;
        float step = 0.1f;
//        for (float x = start; x < max; x += step) {
//            points.add(new Vector2(x, func(x)));
//        }
//        points.add(new Vector2(0, 0));

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.SPACE) {
                    playing = !playing;
                    return true;
                }
                return false;
            }
        });
    }

    private float func(float x) {
        if (useCode) {
            this.context.setAttribute("x", x, ScriptContext.ENGINE_SCOPE);
            try {
                Object result = scriptEngine.eval(code, context);
                return ((Number) result).floatValue();
            } catch (ScriptException e) {
                e.printStackTrace();
                return 0;
            }
        } else {
            return (float) Math.sin(x);
        }
    }

    private float time = 0;
    private float maxTextWidth = Integer.MIN_VALUE;

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        if (true) return;
        if (playing) {
            time += Gdx.graphics.getDeltaTime();
            points.add(new Vector2(time, func(time)));
        }

        if (points.isEmpty()) {
            batch.begin();
            writeRelativeToText("Press Space to begin animation", Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, 0.5f);
            batch.end();
            return;
        }

        renderer.begin(ShapeRenderer.ShapeType.Filled);

        float minX = Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE;
        float maxY = Float.MIN_VALUE;

        for (Vector2 point : points) {
            minX = Math.min(minX, point.x);
            minY = Math.min(minY, point.y);
            maxX = Math.max(maxX, point.x);
            maxY = Math.max(maxY, point.y);
        }


        float deltaX = maxX - minX;
        float deltaY = maxY - minY;
        if (deltaX == 0) {
            minX = 0;
            maxX = 2f * maxX;
            deltaX = maxX - minX;
        }
        if (deltaY == 0) {
            minY = 0;
            maxY = 2f * maxY;
            deltaY = maxY - minY;
        }

        String[] textPointsY = new String[10];
        String[] textPointsX = new String[10];
        for (int i = 1; i <= 10; i++) {
            float dec = i / 10f;
            String yAxis = textPointsY[i - 1] = format(dec * maxY + minY);
            maxTextWidth = Math.max(maxTextWidth, new GlyphLayout(font, yAxis).width);
            textPointsX[i - 1] = format(dec * maxX + minX);
        }

        Vector2 origin = new Vector2(maxTextWidth + 50, 50);

        Vector2 screenSize = new Vector2(Gdx.graphics.getWidth() - origin.x, Gdx.graphics.getHeight() - origin.y);
        Vector2 difScreenSize = new Vector2(screenSize.x - origin.x, screenSize.y - origin.y);

        renderer.setColor(Color.ORANGE);
        Vector2 lastPoint = null;
        int len = (int) (points.size() * Math.min(1, (time / DRAW_TIME)));
        for (int i = 0; i < len; i++) {
            Vector2 point = points.get(i);
            point = new Vector2(
                    (((point.x - minX) / deltaX) * difScreenSize.x) + origin.x,
                    ((((point.y - minY) / deltaY) * difScreenSize.y) + origin.y)
            );

            if (lastPoint != null) {
                renderer.rectLine(lastPoint, point, lineWidth);
            }
            lastPoint = point;
        }

        renderer.setColor(Color.WHITE);
        // Draw axis lines
        renderer.rectLine(origin, new Vector2(origin.x, screenSize.y + (origin.y / 2)), 2);
        renderer.rectLine(origin, new Vector2(screenSize.x, origin.y), 2);
        // Draw arrows
        renderer.triangle(
                origin.x - triangleWidth / 2, screenSize.y - (triangleHeight - 1) + (origin.y / 2),
                origin.x, screenSize.y + 1 + (origin.y / 2),
                origin.x + triangleWidth / 2, screenSize.y - (triangleHeight - 1) + (origin.y / 2)
        );
        renderer.triangle(
                screenSize.x - (triangleHeight - 1), origin.y + triangleHeight / 2,
                screenSize.x + 1, origin.y,
                screenSize.x - (triangleWidth - 1), origin.y - triangleWidth / 2
        );

        renderer.end();
        batch.begin();

        // Draw origin numbers
        if (minX == minY) {
            writeRelativeToText(format(minX), origin.x - 10, origin.y - 10, 1, 0);
        } else {
            String originX = format(minX);
            String originY = format(minY);
            writeRelativeToText(originY, origin.x - 10, origin.y, 1, -1);
            writeRelativeToText(originX, origin.x, origin.y -10, 0, 0);
        }
        // Draw text points
        for (int i = 1; i <= 10; i++) {
            float dec = i / 10f;
            // Draw y-axis text
            writeRelativeToText(textPointsY[i - 1], origin.x - 10, dec * (screenSize.y - origin.y) + origin.y, 1, -0.5f);
            // Draw x-axis text
            writeRelativeToText(textPointsX[i - 1], dec * (screenSize.x - origin.x) + origin.x, origin.y - 10, 1, 0);
        }

        String text =
                "Graph generated based on formula: " + code
                ;

        font.draw(batch, text, 50 + origin.x, Gdx.graphics.getHeight() - 10);

        batch.end();
    }

    private static final int PRECISION = 3;
    private static final double PRECISION_ROUND = Math.pow(10, PRECISION);
    private static final double SCI_NOTATION_AT_HIGH = 1e4;
    private static final double SCI_NOTATION_AT_LOW = -SCI_NOTATION_AT_HIGH;
    private String format(float num) {
        if (num == 0) return "0";
        num = round(num);
        if (num < SCI_NOTATION_AT_HIGH && num > SCI_NOTATION_AT_LOW) {
            return approximateString(num);
        }
        int digits = (int) (Math.log10((int) num));
        num = (float) (num * Math.pow(10, -digits));
        if (Math.abs(Math.round(num) - num) < 1e-2 && Math.round(num) == 10) {
            num /= 10;
            digits++;
        }
        return approximateString(num) + "e" + digits;
    }
    private String approximateString(float num) {
        if ((int) num == num) return String.valueOf((int) num);
        if (Math.abs(Math.round(num) - num) < 1e-2) return String.valueOf(Math.round(num));
        return String.valueOf(num);
    }
    private float round(float num) {
        if (num >= PRECISION_ROUND) return Math.round(num);

        int digits = (int) Math.log10(Math.abs(num));
        int alter = (PRECISION - 1) - digits;
        num *= Math.pow(10, alter);
        num = Math.round(num);
        num *= Math.pow(10, -alter);
        return num;
    }

    private void writeRelativeToText(String text, float x, float y, float multiplier) {
        writeRelativeToText(text, x, y, multiplier, multiplier);
    }
    private void writeRelativeToText(String text, float x, float y, float multiplierX, float multiplierY) {
        GlyphLayout layout = new GlyphLayout(font, text);
        font.draw(batch, text, x - (layout.width * multiplierX), y - (layout.height * multiplierY));
    }

}
