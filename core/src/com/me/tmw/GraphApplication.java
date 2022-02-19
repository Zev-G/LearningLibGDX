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

        if (useCode) {
            this.context = new SimpleScriptContext();
            this.manager = new ScriptEngineManager(getClass().getClassLoader());
            this.scriptEngine = this.manager.getEngineByName("JavaScript");
            this.context.setBindings(scriptEngine.createBindings(), ScriptContext.ENGINE_SCOPE);
        }

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.SPACE) {
                    playing = !playing;
                    return true;
                } else if (keycode == Input.Keys.C) {
                    drawTime = 0;
                    points.clear();
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
//            return (float) Math.pow(x - 20, 2) + 12;
            return (float) Math.sin(x);
        }
    }

    private float drawTime = 0;
    private float rawTime = 0;
    private float maxTextWidth = 0;

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        if (true) return;
        float deltaTime = Gdx.graphics.getDeltaTime();
        if (playing && rawTime >= 1) {
            points.add(new Vector2(drawTime, func(drawTime)));
            drawTime += deltaTime;
        } else if (!playing && points.isEmpty()) {
            batch.begin();
            writeRelativeToText("Press Space to begin animation", Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, 0.5f);
            batch.end();
            return;
        }

        rawTime += deltaTime;

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
            if (!points.isEmpty()) {
                String yAxis = textPointsY[i - 1] = format(dec * maxY + minY);
                maxTextWidth = Math.max(maxTextWidth, new GlyphLayout(font, yAxis).width);
                textPointsX[i - 1] = format(dec * maxX + minX);
            } else {
                textPointsX[i - 1] = "0";
                textPointsY[i - 1] = "0";
            }
        }

        Vector2 origin = new Vector2(Math.max(maxTextWidth + 50, 100), 50);

        Vector2 ogScreenSize = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Vector2 screenSize = new Vector2(ogScreenSize.x - origin.x, ogScreenSize.y - origin.y);
        Vector2 difScreenSize = new Vector2(screenSize.x - origin.x, screenSize.y - origin.y);

        Color color = new Color(Color.ORANGE);
        renderer.setColor(color);
        Vector2 lastPoint = null;
        int len = (int) (points.size() * Math.min(1, (drawTime / DRAW_TIME)));
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
        Vector2 lineSize = new Vector2(screenSize.x, screenSize.y + (origin.y / 2));
        if (rawTime < 1) {
            float mult = Math.min(1, rawTime);
            lineSize = new Vector2(lineSize.x * mult, lineSize.y * mult);
        }
        // Draw axis lines
        renderer.rectLine(origin, new Vector2(origin.x, lineSize.y), 2);
        renderer.rectLine(origin, new Vector2(lineSize.x, origin.y), 2);
        // Draw arrows
        renderer.triangle(
                origin.x - triangleWidth / 2, lineSize.y - (triangleHeight - 1),
                origin.x, lineSize.y,
                origin.x + triangleWidth / 2, lineSize.y - (triangleHeight - 1)
        );
        renderer.triangle(
                lineSize.x - (triangleHeight - 1), origin.y + triangleHeight / 2,
                lineSize.x + 1, origin.y,
                lineSize.x - (triangleWidth - 1), origin.y - triangleWidth / 2
        );

        renderer.end();
        batch.begin();

        // Draw origin numbers
        if (rawTime >= 0.4) {
            if (points.isEmpty()) {
                writeRelativeToText("0", origin.x - 10, origin.y, 1, -1);
                writeRelativeToText("0", origin.x, origin.y - 10, 0, 0);
            } else {
                if (minX == minY) {
                    writeRelativeToText(format(minX), origin.x - 10, origin.y - 10, 1, 0);
                } else {
                    String originX = format(minX);
                    String originY = format(minY);
                    writeRelativeToText(originY, origin.x - 10, origin.y, 1, -1);
                    writeRelativeToText(originX, origin.x, origin.y - 10, 0, 0);
                }
            }
        }
        // Draw text points
        for (int i = 1, m = (int) (10 * Math.min(rawTime - 0.5, 1)); i <= m; i++) {
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
    private String format(double num) {
        if (num == 0) return "0";
        num = round(num);
        int digits = (int) (Math.log10(Math.abs(num)) - 1);
        if (Math.abs(digits) < PRECISION) {
            return approximateString(num);
        }
        if (num > 1) digits++;
        num = num * Math.pow(10, -digits);
//        if (Math.abs(Math.round(num) - num) < 1e-2 && Math.round(num) == 10) {
//            num /= 10;
//            digits++;
//        }
        return approximateString(num) + "e" + digits;
    }
    private String approximateString(double num) {
        if ((int) num == num) return String.valueOf((int) num);
        if (Math.abs(num - round(num, 4)) < 1e-13) return String.valueOf(round(num));
        return String.valueOf(num);
    }
    private double round(double num) {
        return round(num, PRECISION);
    }
    private double round(double num, int precision) {
        if (num < 1) precision++;
        int digits = (int) Math.log10(Math.abs(num));
        int alter = (precision - 1) - digits;
        double pow = Math.pow(10, alter);
        num *= pow;
        num = Math.round(num);
        num /= pow;
        return num;
    }

    private void writeRelativeToText(String text, float x, float y, float multiplier) {
        writeRelativeToText(text, x, y, multiplier, multiplier);
    }
    private void writeRelativeToText(String text, float x, float y, float multiplierX, float multiplierY) {
        if (text == null) return;
        GlyphLayout layout = new GlyphLayout(font, text);
        font.draw(batch, text, x - (layout.width * multiplierX), y - (layout.height * multiplierY));
    }

}
