package model.filemanagement.otherformats.data;

import java.io.File;
import model.gameoflife.GameOfLife;

/**
 * This class generates a sound interpretation of a game board
 *
 * @see model.filemanagement.otherformats.WavSaver
 */
public class WavData {
    private final GameOfLife game;
    private final int iterations;
    private final int samplerate;
    private final int channels;
    private final int durationInSeconds;
    private final int bits;
    private final File file;

    /**
     * Creates an object of {@link WavData} with the specified parameters
     * @param game The {@link GameOfLife} to use
     * @param iterations The number of iterations
     * @param sampleRate The sample rate to use
     * @param channels The number of channels to use
     * @param durationInSeconds The duation in seconds
     * @param bits The bit rate
     * @param file The file to use
     */
    public WavData(GameOfLife game, int iterations, int sampleRate, int channels, 
            int durationInSeconds, int bits, File file) {
        this.game = game;
        this.iterations = iterations;
        this.samplerate = sampleRate;
        this.channels = channels;
        this.durationInSeconds = durationInSeconds;
        this.bits = bits;
        this.file = file;
    }

    /**
     * Returns the {@link GameOfLife} object
     * @return The game
     */
    public GameOfLife getGame() {
        return game;
    }

    /**
     * The number of iterations
     * @return the iterations
     */
    public int getIterations() {
        return iterations;
    }

    /**
     * Returns the sample rate
     * @return the samplerate
     */
    public int getSamplerate() {
        return samplerate;
    }

    /**
     * Returns the number of channels
     * @return the channels
     */
    public int getChannels() {
        return channels;
    }

    /**
     * Returns the duration in seconds
     * @return the durationInSeconds
     */
    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    /**
     * Returns the bit rate used
     * @return the bits
     */
    public int getBits() {
        return bits;
    }

    /**
     * Returns the file
     * @return the file
     */
    public File getFile() {
        return file;
    }
}
