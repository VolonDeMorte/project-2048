package com.javarush.task.jdk13.task53.task5301;

import com.javarush.engine.cell.*;

public class Game2048 extends Game {
    private static final int SIDE = 4;
    private int[][] gameField = new int[SIDE][SIDE];
    private boolean isGameStopped = false;

    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
        drawScene();
        setTurnTimer(5);
    }

    @Override
    public void onKeyPress(Key key) {
        if (isGameStopped) {
            if (key == Key.SPACE) {
                isGameStopped = false;
                createGame();
                drawScene();
            } else {
                return;
            }
        }

        if (!canUserMove()) {
            gameOver();
            return;
        }

        if (key == Key.LEFT) {
            moveLeft();
            drawScene();
        } else if (key == Key.RIGHT) {
            moveRight();
            drawScene();
        } else if (key == Key.UP) {
            moveUp();
            drawScene();
        } else if (key == Key.DOWN) {
            moveDown();
            drawScene();
        } else {
            return;
        }
        drawScene();
    }

    private void createGame() {
        gameField = new int[SIDE][SIDE];
        createNewNumber();
        createNewNumber();
    }

    private void drawScene() {
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                setCellColoredNumber(x, y, gameField[y][x]);
            }
        }
    }

    private void createNewNumber() {
        if (getMaxTileValue() >= 2048) {
            win();
            return;
        }
        while (true) {
            int x = getRandomNumber(SIDE);
            int y = getRandomNumber(SIDE);
            if (gameField[y][x] == 0) {
                gameField[y][x] = getRandomNumber(10) == 9 ? 4 : 2;
                setCellColoredNumber(x, y, gameField[y][x]);
                break;
            }
        }
    }

    private void setCellColoredNumber(int x, int y, int value) {
        setCellValueEx(x, y, getColorByValue(value), value == 0 ? "" : Integer.toString(value));
    }

    private Color getColorByValue(int value) {
        switch (value) {
            case 0:
                return Color.DARKGRAY;
            case 2:
                return Color.GREEN;
            case 4:
                return Color.CYAN;
            case 8:
                return Color.AQUAMARINE;
            case 16:
                return Color.DEEPPINK;
            case 32:
                return Color.LIGHTCORAL;
            case 64:
                return Color.MEDIUMPURPLE;
            case 128:
                return Color.SPRINGGREEN;
            case 256:
                return Color.PLUM;
            case 512:
                return Color.PERU;
            case 1024:
                return Color.SILVER;
            case 2048:
                return Color.GOLD;
            default:
                return Color.NONE;
        }
    }

    private boolean compressRow(int[] row) {
        boolean result = false;
        int currentZero = 0;
        for (int i = 0; i < row.length; i++) {
            if (row[i] != 0) {
                if (i != currentZero) {
                    row[currentZero] = row[i];
                    row[i] = 0;
                    currentZero++;
                    result = true;
                } else {
                    currentZero++;
                }
            }
        }
        return result;
    }

    private boolean mergeRow(int[] row) {
        boolean result = false;
        for (int i = 1; i < row.length; i++) {
            if (row[i] != 0 && row[i - 1] == row[i]) {
                row[i - 1] += row[i];
                row[i] = 0;
                result = true;
                i++;
            }
        }
        return result;
    }

    private void moveLeft() {
        boolean isNewNumberNeed = false;
        for (int i = 0; i < gameField.length; i++) {
            boolean a = compressRow(gameField[i]);
            boolean b = mergeRow(gameField[i]);
            boolean c = compressRow(gameField[i]);
            if (a || b || c) {
                isNewNumberNeed = true;
            }
        }
        if (isNewNumberNeed) {
            createNewNumber();
        }
    }

    private void moveRight() {
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    }

    private void moveUp() {
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
    }

    private void moveDown() {
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }

    private void rotateClockwise() {
        int[][] result = new int[SIDE][SIDE];
        for (int i = 0; i < gameField.length; i++) {
            for (int j = 0; j < gameField.length; j++) {
                result[i][j] = gameField[SIDE - j - 1][i];
            }
        }
        gameField = result;
    }

    private int getMaxTileValue() {
        int maxTileValue = gameField[0][0];
        for (int i = 0; i < gameField.length; i++) {
            for (int j = 0; j < gameField.length; j++) {
                if (gameField[i][j] > maxTileValue) {
                    maxTileValue = gameField[i][j];
                }
            }
        }
        return maxTileValue;
    }

    private void win() {
        showMessageDialog(Color.FUCHSIA, "Congratulations!", Color.DARKGRAY, 50);
        isGameStopped = true;
    }

    private void gameOver() {
        showMessageDialog(Color.FUCHSIA, "GameOver ((", Color.DARKGRAY, 50);
        isGameStopped = true;
    }

    private boolean canUserMove() {
        for (int i = 0; i < gameField.length; i++) {
            for (int j = 0; j < gameField.length; j++) {
                if (gameField[i][j] == 0) {
                    return true;
                }
                if (j > 0 && (gameField[i][j - 1] == gameField[i][j] ||
                        gameField[j - 1][i] == gameField[j][i])) {
                    return true;
                }
            }
        }
        return false;
    }
}
