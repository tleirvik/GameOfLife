/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.FileManagement.OtherFormats.Data;

import Model.GameOfLife.GameOfLife;
import java.io.File;

/**
 *
 * @author Robin
 */
public class WavData {
    private final GameOfLife game;
    private final int iterations;
    private final int samplerate;
    private final int channels;
    private final int durationInSeconds;
    private final int bits;
    private final File file;
    
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
     * @return the game
     */
    public GameOfLife getGame() {
        return game;
    }

    /**
     * @return the iterations
     */
    public int getIterations() {
        return iterations;
    }

    /**
     * @return the samplerate
     */
    public int getSamplerate() {
        return samplerate;
    }

    /**
     * @return the channels
     */
    public int getChannels() {
        return channels;
    }

    /**
     * @return the durationInSeconds
     */
    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    /**
     * @return the bits
     */
    public int getBits() {
        return bits;
    }

    /**
     * @return the file
     */
    public File getFile() {
        return file;
    }
}
