package Test;

import FileManagement.RLEDecoder;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.objenesis.*;

/**
 * Created by tleirvik on 08.04.16.
 */
public class RLEDecoderTest {
    private RLEDecoder rleDecoder;
    @Before
    public void setUp() throws Exception {
        // Arrange
        BufferedReader bf = mock(BufferedReader.class);
        rleDecoder = new RLEDecoder(bf);
        // Mocking the contents of a RLE file
        when(bf.readLine()).thenReturn("#N Garden of Eden 5", "#O Nicolay Beluchenko",
                "#C www.conwaylife.com/wiki/index.php?title=Garden_of_Eden_5",
                "x = 11, y = 11, rule = b3/s23",
                "b3o2b2o3b$b2obobob3o$b3o2b5o$obobobobobo$4obobobob$4b3o4b$bobobob4o$ob",
                "obobobobo$5o2b3ob$3obobob2ob$3b2o2b3o!");
        // Act
        rleDecoder.decode();
        // Assert
        //System.out.println("Mock" + rleDecoder.getMetadata().getName());
    }

    @Test
    public void decode() throws Exception {

    }

    @Test
    public void getBoard() throws Exception {

    }

    @Test
    public void getMetadata() throws Exception {

    }

}