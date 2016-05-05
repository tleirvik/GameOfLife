import Model.GameOfLife.Boards.Board;
import Model.GameOfLife.Boards.DynamicBoard;
import Model.GameOfLife.GameOfLife;
import Model.GameOfLife.MetaData;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by tleirvik on 15.04.2016.
 */
public class DynamicBoardTest {
    GameOfLife gol;
    DynamicBoard db;
    MetaData metaData = new MetaData();
    byte[][] inputArraySmallExploder = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    };

    @Before
    public void setUp() throws Exception {
        gol = new GameOfLife();
        db = new DynamicBoard(10, 10);
        metaData.setAuthor("Ola Nordmann");
        metaData.setComment("Kommentar");
        metaData.setName("Pattern name");
        String[] s = new String[2];
        s[0] = "B3";
        s[1] = "S23";
        metaData.setRuleString(s);
        gol.loadGame(inputArraySmallExploder, metaData, Board.BoardType.DYNAMIC);

        assertTrue(gol instanceof GameOfLife);
        assertTrue(gol.getBoard() instanceof Board);
    }

    @Test
    public void constructorTest1() throws Exception {
        // Arrange
        DynamicBoard db;

        // Act
        db = new DynamicBoard(10, 10);

        // Assert
        assertTrue(db instanceof DynamicBoard);
        assertEquals(10, db.getRows());
        assertEquals(10, db.getColumns());
        assertEquals("0000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000", db.toString());
    }

    @Test
    public void constructorTest2() throws Exception {
        // Arrange
        DynamicBoard db;

        // Act
        db = new DynamicBoard(inputArraySmallExploder, metaData);

        // Assert
        assertTrue(db instanceof DynamicBoard);
        assertEquals(11, db.getRows());
        assertEquals(11, db.getColumns());
        assertEquals("0000000000000000000000000000000000000000000000000100000000011100000000101000000000100000000" +
                "000000000000000000000000000000", db.toString());
        assertEquals("Ola Nordmann", db.getMetaData().getAuthor());
        assertEquals("Kommentar", db.getMetaData().getComment());
        assertEquals("Pattern name", db.getMetaData().getName());
    }


    @Test
    public void getRows() throws Exception {
        // Arrange

        // Act
        int rows = db.getRows();

        // Assert
        assertEquals(10, rows);
    }

    @Test
    public void getColumns() throws Exception {
        // Arrange

        // Act
        int columns = db.getColumns();

        // Assert
        assertEquals(10, columns);
    }

    @Test
    public void getMetaData() throws Exception {
        // Arrange

        // Act
        db.getMetaData().setAuthor("Ola Nordmann");
        db.getMetaData().setComment("Kommentar");
        db.getMetaData().setName("Pattern name");
        String[] s = new String[2];
        s[0] = "B3";
        s[1] = "S23";
        db.getMetaData().setRuleString(s);

        // Assert
        assertEquals("Ola Nordmann", db.getMetaData().getAuthor());
        assertEquals("Pattern name", db.getMetaData().getName());
        assertEquals("Kommentar", db.getMetaData().getComment());
        assertEquals("B3", db.getMetaData().getRuleString()[0]);
        assertEquals("S23", db.getMetaData().getRuleString()[1]);
    }

    @Test
    public void getCellAliveState() throws Exception {
        // Arrange

        byte cellState;

        // Act

        db.setCellAliveState(7,7,(byte)1);

        // Assert
        assertEquals(0, db.getCellAliveState(8, 8));
    }
    @Test
    public void setCellAliveState() throws Exception {
        // Arrange
        db = new DynamicBoard(10, 10);

        // Act
        db.setCellAliveState(1,1, (byte)1);

        // Assert
        assertEquals((byte)1, db.getCellAliveState(1,1));
    }

    @Test
    public void setCellAliveStateDynamic() throws Exception {
        // Arrange
        byte toSet = 1;
        gol.newEmptyGame(10, 10, Board.BoardType.DYNAMIC);
        db = new DynamicBoard(10, 10);

        // Act
        byte verify = db.getCellAliveState(3,3);
        db.setCellAliveState(150,150, toSet);

        // Assert
        assertEquals(0, verify);
        assertEquals(db.getCellAliveState(150,150), (byte)1);
        assertEquals(151, db.getRows());
        assertEquals(151, db.getColumns());
        db = new DynamicBoard(10, 10);
        db.setCellAliveState(2985,8433, toSet);
        assertEquals(db.getCellAliveState(2985,8433), (byte)1);
    }

    @Test
    public void resetBoard() throws Exception {
        // Arrange
        String startingBoardDynamic = "0000000000000000000000000000000000000000000000000100000000011100000" +
                "000101000000000100000000000000000000000000000000000000";
        gol = new GameOfLife();
        gol.loadGame(inputArraySmallExploder, metaData, Board.BoardType.DYNAMIC);

        // Act
        String compareBefore = gol.getBoard().toString();
        gol.update();
        String compareAfter = gol.getBoard().toString();
        gol.resetGame();
        String compareAfterReset = gol.getBoard().toString();

        // Assert
        assertEquals(compareBefore, compareBefore);
        assertNotEquals(compareAfter, compareBefore);
        assertEquals(startingBoardDynamic, compareAfterReset);

    }

    @Test
    public void setFirstGeneration() throws Exception {
        // Arrange
        String startingBoardDynamic = "0000000000000000000000000000000000000000000001000000001110000000101000000" +
                "001000000000000000000000000";
        gol = new GameOfLife();
        gol.loadGame(inputArraySmallExploder, metaData, Board.BoardType.DYNAMIC);

        // Act
        gol.setFirstGeneration();
        gol.update();

        // Assert
        assertNotEquals(startingBoardDynamic, gol.toString());
        gol.resetGame();
        assertEquals(startingBoardDynamic, gol.getBoard().toString());
    }

    @Test
    public void testClone() throws Exception {
        // Arrange
        db = new DynamicBoard(inputArraySmallExploder, metaData);
        String startingBoardDynamic = "00000000000000000000000000000000000000000000000001000000000111000000001010" +
                "00000000100000000000000000000000000000000000000";

        // Act
        DynamicBoard cloneBoard = db.clone();

        String compare = cloneBoard.toString();

        // Assert
        assertNotEquals(cloneBoard.hashCode(), db.hashCode());
        assertEquals(cloneBoard.toString(), db.toString());
        assertEquals(startingBoardDynamic, cloneBoard.toString());
        assertEquals("Ola Nordmann", db.getMetaData().getAuthor());
        assertEquals("Pattern name", db.getMetaData().getName());
        assertEquals("Kommentar", db.getMetaData().getComment());
        assertEquals("B3", db.getMetaData().getRuleString()[0]);
        assertEquals("S23", db.getMetaData().getRuleString()[1]);
    }

    @Test
    public void testToString() throws Exception {
        // Arrange
        db = new DynamicBoard(inputArraySmallExploder, new MetaData());
        String verify = "00000000000000000000000000000000000000000000000001000000000111000000001010000000001000000" +
                "00000000000000000000000000000000";

        // Act
        String compare = db.toString();

        // Assert
        assertEquals(verify, compare);
    }

    @Test
    public void testGetBoundingBox() {
        // Arrange
        gol = new GameOfLife();
        int[] testBoundingBox = new int[4];
        testBoundingBox[0] = 4;
        testBoundingBox[1] = 7;
        testBoundingBox[2] = 4;
        testBoundingBox[3] = 6;

        // Act
        gol.loadGame(inputArraySmallExploder, metaData, Board.BoardType.DYNAMIC);

        // Assert
        assertArrayEquals(testBoundingBox, gol.getBoard().getBoundingBox());
    }

    @Test
    public void testExpandingEdgesTop() {
        // Arrange
        gol = new GameOfLife();
        gol.loadGame(inputArraySmallExploder, new MetaData(), Board.BoardType.DYNAMIC);
        String verify = "000000000000000000000000000000000000000000000000000000000001110000000010100000000101000" +
                "000000100000000000000000000000000000000000000";

        // Act
        gol.setCellAliveState(0, 5, (byte)1);
        gol.update();
        // Assert
        assertEquals(verify, gol.getBoard().toString());

    }

    @Test
    public void testExpandingEdgesBottom() {
        // Arrange
        gol = new GameOfLife();
        gol.loadGame(inputArraySmallExploder, new MetaData(), Board.BoardType.DYNAMIC);
        String verify = "000000000000000000000000000000000000000000000000111000000001010000000010100000000010" +
                "000000000000000000000000000000000000000000000000";

        // Act
        gol.setCellAliveState(10, 5, (byte)1);
        gol.update();

        // Assert
        assertEquals(verify, gol.getBoard().toString());

    }
    @Test
    public void testExpandingEdgesLeft() {
        // Arrange
        gol = new GameOfLife();
        gol.loadGame(inputArraySmallExploder, new MetaData(), Board.BoardType.DYNAMIC);
        String verifyLeft = "00000000000000000000000000000000000000000000000000000111000000000101000000000101000000" +
                "0000100000000000000000000000000000000000000000";

        // Act
        gol.setCellAliveState(5, 0, (byte)1);
        gol.update();
        System.out.println(gol.getRows() + " : " + gol.getColumns());
        System.out.println(gol.getBoard().toString());

        // Assert
        assertEquals(verifyLeft, gol.getBoard().toString());

    }

    @Test
    public void testExpandingEdgesRight() {
        // Arrange
        gol = new GameOfLife();
        gol.loadGame(inputArraySmallExploder, new MetaData(), Board.BoardType.DYNAMIC);
        String verifyRight = "00000000000000000000000000000000000000000000000000001110000000001010000000001" +
                "0100000000001000000000000000000000000000000000000000000";

        // Act
        gol.setCellAliveState(5, 10, (byte)1);
        gol.update();

        // Assert
        assertEquals(verifyRight, gol.getBoard().toString());
    }

    @Test
    public void testExpandingEdges() {
        // Arrange
        gol = new GameOfLife();
        gol.loadGame(inputArraySmallExploder, new MetaData(), Board.BoardType.DYNAMIC);
        String verify = "00000000000000000000000000000000000000000000000000000000000000000000001110000000000101000000" +
                "00001010000000000010000000000000000000000000000000000000000000000000000000000";

        // Act
        gol.setCellAliveState(0, 5, (byte)1);
        gol.setCellAliveState(10, 5, (byte)1);
        gol.setCellAliveState(5, 0, (byte)1);
        gol.setCellAliveState(5, 10, (byte)1);

        gol.update();

        // Assert
        assertEquals(verify, gol.getBoard().toString());
        assertEquals(13, gol.getRows());
        assertEquals(13, gol.getColumns());
    }
}