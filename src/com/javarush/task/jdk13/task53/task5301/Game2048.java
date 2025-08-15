package com.javarush.task.jdk13.task53.task5301;

import com.javarush.engine.cell.*;

public class Game2048 extends Game {
    private static final int SIDE = 6;
    private int[][] gameField;
    private boolean isGameStopped = false;
    private int score = 0;

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
                return;
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
        } else if (key == Key.RIGHT) {
            moveRight();
        } else if (key == Key.UP) {
            moveUp();
        } else if (key == Key.DOWN) {
            moveDown();
        } else {
            return;
        }
        drawScene();
    }

    private void createGame() {
        gameField = new int[SIDE][SIDE];
        createNewNumber();
        createNewNumber();
        score = 0;
        setScore(score);
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
                break;
            }
        }
    }

    private void setCellColoredNumber(int x, int y, int value) {
        setCellValueEx(x, y, getColorByValue(value), value == 0 ? "" : Integer.toString(value));
    }

    private Color getColorByValue(int value) {
        return switch (value) {
            case 0 -> Color.DARKGRAY;
            case 2 -> Color.GREEN;
            case 4 -> Color.CYAN;
            case 8 -> Color.AQUAMARINE;
            case 16 -> Color.DEEPPINK;
            case 32 -> Color.LIGHTCORAL;
            case 64 -> Color.MEDIUMPURPLE;
            case 128 -> Color.SPRINGGREEN;
            case 256 -> Color.PLUM;
            case 512 -> Color.PERU;
            case 1024 -> Color.SILVER;
            case 2048 -> Color.GOLD;
            default -> Color.NONE;
        };
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
                score += row[i - 1];
                setScore(score);
                i++;
            }
        }
        return result;
    }

    private void moveLeft() {
        boolean isNewNumberNeed = false;
        for (int[] row : gameField) {
            boolean isCompressed = compressRow(row);
            boolean isMerged = mergeRow(row);
            isCompressed = compressRow(row);
            if (isCompressed || isMerged) {
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
        for (int y = 0; y < gameField.length; y++) {
            for (int x= 0; x < gameField.length; x++) {
                result[y][x] = gameField[SIDE - x - 1][y];
            }
        }
        gameField = result;
    }

    private int getMaxTileValue() {
        int maxTileValue = gameField[0][0];
        for (int y = 0; y < gameField.length; y++) {
            for (int x = 0; x < gameField.length; x++) {
                if (gameField[y][x] > maxTileValue) {
                    maxTileValue = gameField[y][x];
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
        for (int y = 0; y < gameField.length; y++) {
            for (int x = 0; x < gameField.length; x++) {
                if (gameField[y][x] == 0) {
                    return true;
                }
                if (x > 0 && (gameField[y][x - 1] == gameField[y][x] ||
                        gameField[x - 1][y] == gameField[x][y])) {
                    return true;
                }
            }
        }
        return false;
    }
}
