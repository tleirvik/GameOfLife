
import Model.FileManagement.Decoders.Decoder;
import Model.FileManagement.Decoders.RLEDecoder;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**

 */
public class RLEDecoderTest {
    // Hardcoded a RLE file for encoding. An alternative would be to use a mocking framework and mock the file
    private File file;
    private BufferedReader reader;
   // private RLEDecoder rleDecoder;
    //private MetaData metadata = rleDecoder.getMetadata();
// TODO: 05.05.2016 Sjekk om denne filen bør testes 
    /**
     *
     * @throws Exception If the test fails
     */
    @Before
    public void setUp() throws Exception {
        // Arrange

        // Act
        // file = new File("C:\\Users\\tleirvik\\Dropbox\\gardenofeden5.rle");
        file = new File("/Users/tleirvik/Dropbox/gardenofeden5.rle");
        // Assert
        assertNotNull(file);
        assertTrue(file.canRead());
        reader = new BufferedReader(new FileReader(file.getAbsolutePath()));

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
        Decoder decoder = new RLEDecoder(reader);
        // Act
        decoder.decode();
        /*Method method = RLEEncoder.class.getDeclaredMethod("encodeBoard");
        method.setAccessible(true);
        method.invoke(decoder);
*/
        Field field = decoder.getClass().getDeclaredField("board");
        field.setAccessible(true);
        byte[][] board = (byte[][]) field.get("board");
        System.out.println(board.getClass());
        // Assert

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