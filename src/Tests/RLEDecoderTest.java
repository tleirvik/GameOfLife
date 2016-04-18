package Tests;

import FileManagement.RLEDecoder;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static org.junit.Assert.*;

/**
 * Created by tleirvik on 17.04.16.
 */
public class RLEDecoderTest {
    // Velg en annen fil
    private File file = new File("/home/tleirvik/Downloads/gardenofeden5.rle");
    private RLEDecoder rleDecoder;

    @Before
    public void setUp() throws Exception {
        // System.out.println(file.canRead());

    }

    @Test
    public void decode() throws Exception {
        // Arrange

        // Act
        BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
        rleDecoder = new RLEDecoder(reader);
        rleDecoder.decode();
        byte[][] board = rleDecoder.getBoard();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
        byte[] testArrayLine0 = {0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0};
        // Assert
        // assertArrayEquals(testArray, board[0]);
        // assertEquals(testArray, 000000);[
    }

    @Test
    public void getBoard() throws Exception {

    }

    @Test
    public void getMetadata() throws Exception {

    }

}