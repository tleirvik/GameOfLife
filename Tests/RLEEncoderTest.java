import Model.FileManagement.Encoders.Encoder;
import Model.FileManagement.Encoders.RLEEncoder;
import Model.GameOfLife.Boards.Board;
import Model.GameOfLife.GameOfLife;
import Model.GameOfLife.MetaData;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by tleirvik on 03.05.2016.
 */
public class RLEEncoderTest {
    File f;
    Encoder encoder;
    GameOfLife gol;
    MetaData metaData;
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
    public void setUp() {
        metaData = new MetaData();
        metaData.setAuthor("Ola Nordmann");
        metaData.setComment("Kommentar");
        metaData.setName("Brett");
        String[] ruleString = {
                "3","23"
        };
        metaData.setRuleString(ruleString);
        f = new File("temp.rle");
        gol = new GameOfLife();
        gol.loadGame(inputArraySmallExploder, metaData, Board.BoardType.FIXED);
        assertTrue(gol instanceof GameOfLife);
        assertTrue(gol.getBoard() instanceof Board);

    }

    /**
     *  Using reflection to access the objects private methods
     * @throws Exception
     */
    @Test
    public void encode() throws Exception {
        // Arrange
        encoder =  new RLEEncoder(gol, f);
        String RLEString = "11b$11b$11b$11b$5bo5b$4b3o4b$4bobo4b$5bo5b$11b$11b$11b$!";

        // Act
        Method method = RLEEncoder.class.getDeclaredMethod("encodeBoard");
        method.setAccessible(true);
        method.invoke(encoder);
        // Assert

        /*Field field = encoder.getClass().getDeclaredField();
        field.setAccessible(true);
        StringBuilder test = (StringBuilder) field.get(encoder);*/
        assertEquals(RLEString, encoder.toString());
    }

    @Test
    public void encodeFileTest() throws Exception {
        // Arrange
        encoder =  new RLEEncoder(gol, f);
        Reader r = new FileReader(f);
        // Act
        encoder.encode();
        // Assert
        try (BufferedReader reader = new BufferedReader(r)) {
            assertEquals("#N Brett", reader.readLine());
            assertEquals("#O Ola Nordmann", reader.readLine());
            assertEquals("#C Kommentar", reader.readLine());
            assertEquals("x = 11, y = 11, rule = B3/S23", reader.readLine());
            assertEquals("11b$11b$11b$11b$5bo5b$4b3o4b$4bobo4b$5bo5b$11b$11b$11b$!", reader.readLine());
        } catch (FileNotFoundException fnfE) {
            System.out.println("Finner ikke filen.");
        } catch (IOException ioE) {
            ioE.printStackTrace();
        }
    }
}