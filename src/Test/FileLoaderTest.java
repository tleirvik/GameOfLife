package Test;

import FileManagement.FileLoader;
import GameOfLife.FixedBoard;
import GameOfLife.MetaData;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.io.File;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by tleirvik on 08.04.16.
 */
public class FileLoaderTest {
    private FileLoader fl;
    @Before
    public void setUp() throws Exception {
        fl = new FileLoader();
    }

    @Test
    public void readGameBoardFromDisk() {
        // Arrange

        // Act
        //fl.readGameBoardFromDisk(testFile);
        // Assert

    }

    @Test
    public void readGameBoardFromURL() throws Exception {

    }

    @Test
    public void getMetadata() throws Exception {
        // Arrange
        FixedBoard fb = new FixedBoard(10, 10);
        // Act
        // MetaData md = fb.getMetaData();
        //fl.getMetadata().setName("Test");
        // Assert
        //assertTrue(md instanceof MetaData);
        //assertEquals(md.getName(), "Test");
    }
    @Test
    public void getBoard() throws Exception {

    }

}