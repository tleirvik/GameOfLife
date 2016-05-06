package controller;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import model.gameoflife.GameOfLife;

/**
 * This is the draw interface used in our Game Of Life implementation
 */
public interface Draw {

    default void draw(GameOfLife gol, GraphicsContext gc, Color[] colors, double offset_X
            , double offset_Y, double cellSize, boolean drawGrid) {
        final Canvas canvas = gc.getCanvas();
        final double start_X = Math.round(offset_X);
        final double start_Y = Math.round(offset_Y);
        final int rows = gol.getRows();
        final int columns = gol.getColumns();
        final double boardWidth = cellSize * gol.getColumns();
        final double boardHeight = cellSize * gol.getRows();
        final double canvasWidth = canvas.getWidth();
        final double canvasHeight = canvas.getHeight();

        gc.clearRect(0, 0, canvasWidth, canvasHeight);

        gc.setFill(colors[0]); //stdBackgroundColor)
        gc.fillRect(0, 0, canvasWidth, canvasHeight);

        gc.setFill(colors[1]); // stdBoardColor
        gc.fillRect(start_X, start_Y, boardWidth, boardHeight);


        int rowStart = (int) ((0 - (offset_Y - boardHeight)) /
                cellSize) - gol.getRows();

        int rowEnd = (int) ((gc.getCanvas().getHeight() - (offset_Y -
                boardHeight)) / cellSize) - gol.getRows();

        int columnStart = (int) ((0 - (offset_X - boardWidth)) /
                cellSize) - gol.getColumns();

        int columnEnd = (int) ((gc.getCanvas().getWidth() - (offset_X -
                boardWidth)) / cellSize) - gol.getColumns();

        int startRow = 0;
        int endRow = rows;
        if (rowStart > 0) {
            startRow = rowStart;
        }
        if (rowEnd < rows) {
            endRow = rowEnd + 1;
        }

        int startCol = 0;
        int endCol = columns;
        if (columnStart > 0) {
            startCol = columnStart;
        }
        if (columnEnd < columns) {
            endCol = columnEnd + 1;
        }

        gc.setFill(colors[2]); // stdAliveCellColor
        for (int row = startRow; row < endRow; row++) {
            for (int col = startCol; col < endCol; col++ ) {
                if (gol.getCellAliveState(row, col) == 1) {
                    gc.fillRect(start_X + cellSize * col, start_Y + cellSize * row, cellSize, cellSize);
                }
            }
        }

        if (drawGrid) {
            gc.setLineWidth(1); //stdGridLineWidth
            gc.setStroke(colors[3]); //stdGridColor
            final int boardRowLength = gol.getRows();
            final int boardColumnLength = gol.getColumns();

            double end_X = start_X + boardWidth;
            double end_Y = start_Y + boardHeight;

            for (int y = 0; y <= boardRowLength; y++) {
                gc.strokeLine(start_X, start_Y + (cellSize * y),
                        end_X, start_Y + (cellSize * y));
            }

            for (int x = 0; x <= boardColumnLength; x++) {
                gc.strokeLine(start_X + (cellSize * x),
                        start_Y, start_X + (cellSize * x), end_Y);
            }
        }
    }
}


