
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static org.junit.Assert.*;

/**

 */
public class RLEDecoderTest {
    // Hardcoded a RLE file for encoding. An alternative would be to use a mocking framework and mock the file
    private File file;
   // private RLEDecoder rleDecoder;
    //private MetaData metadata = rleDecoder.getMetadata();

    /**
     *
     * @throws Exception If the test fails
     */
    @Before
    public void setUp() throws Exception {
        // Arrange

        // Act
        // file = new File("C:\\Users\\tleirvik\\Dropbox\\gardenofeden5.rle");
         file = new File("/home/tleirvik/Dropbox/gardenofeden5.rle");
        // Assert
        assertNotNull(file);
        assertTrue(file.canRead());
    }

    /**
     *
     * @throws Exception If the test fails
     */
    @Test
    public void decode() throws Exception {
        // Arrange
        byte[] verify1stRow = {0, 1, 1, 1, 0, 0, 1, 1, 0, 0, 0};
        byte[] verify6thRow = {0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0};
        byte[] verifyLastRow = {0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0};

        // Act
        BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
        /*rleDecoder = new RLEDecoder(reader);
        rleDecoder.decode();
        byte[][] board = rleDecoder.getBoard();*/

        // Assert
        /*assertArrayEquals(verify1stRow, board[0]);
        assertArrayEquals(verify6thRow, board[5]);
        assertArrayEquals(verifyLastRow, board[10]);*/
    }

    /**
     * This method tests the getBoard()-method and validates that ve get the board that is read from the RLE decoder
     *
     * @throws Exception If the test fails
     */
    @Test
    public void getBoard() throws Exception {
        // Arrange
        byte[] verifyLastRow = {0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0};

        // Act
        decode();
        //byte[][] board = rleDecoder.getBoard();

        // Assert
        //assertArrayEquals(verifyLastRow, board[10]);
    }

    /**
     * Comment må sjekkes. Lager noen tomme char's på slutten.
     * @throws Exception If the test fails
     */
    @Test
    public void getMetadata() throws Exception {
        // Arrange
        String title = "Garden of Eden 5";
        String author = "Nicolay Beluchenko";
        String comment = "Was the smallest-known Garden of Eden in Conway's Game of Life until\n" +
                "it was surpassed by Garden of Eden 6 in December 2011.\n" +
                "www.conwaylife.com/wiki/index.php?title=Garden_of_Eden_5";
        // Act
        decode();
        //MetaData metadata = rleDecoder.getMetadata();
        // Assert
       // assertNotNull(metadata);
        //assertEquals(title, metadata.getName());
       // assertEquals(author, metadata.getAuthor());
        // assertEquals(comment, metadata.getComment());
    }

}