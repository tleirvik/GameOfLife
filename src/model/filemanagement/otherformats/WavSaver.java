package model.filemanagement.otherformats;


import model.filemanagement.otherformats.data.WavData;
import model.filemanagement.otherformats.wav.WavFile;
import model.filemanagement.otherformats.wav.WavFileException;
import model.gameoflife.Statistics;

import java.io.File;
import java.io.IOException;

/**
 * This class saves a game board to sound file
 */
public class WavSaver {
    private WavFile wavFile;
    private final Statistics statistics;
    private final int iterations;
    private final int sampleRate;
    private final int channels;
    private final int durationInSeconds;
    private final int bits;
    private final File file;

    /**
     * Constructs a {@link WavSaver} object and collects statistics to generate sound
     * @param data A {@link WavData} object
     */
    public WavSaver(WavData data) {
        iterations = data.getIterations();
        sampleRate = data.getSamplerate(); //Samples per second
        durationInSeconds = data.getDurationInSeconds();
        channels = data.getChannels();
        bits = data.getBits();
        file = data.getFile();
        
        statistics = new Statistics(data.getGame(), data.getIterations());
    }

    /**
     * Method that saves the board to sound
     * @return true if the board is saved, otherwise false
     */
    public boolean saveSound() {
        long numFrames = (long) (durationInSeconds * sampleRate);
        try {
            wavFile = WavFile.newWavFile(file, channels, numFrames, bits, sampleRate);
            generateSound();
            wavFile.close();
        } catch (IOException ex) {
            return false;
        } catch (WavFileException ex) {
           return false;
        }
        return true;
    }

    /**
     * This method generates the sound from the board statistics
     * @throws IOException If an unspecified I/O error occures
     * @throws WavFileException If an unspecified {@link WavFileException}
     */
    private void generateSound() throws IOException, WavFileException {
        double[][]buffer = new double[wavFile.getNumChannels()][100];

        long numFrames = wavFile.getNumFrames();

                        
        long framesPerIteration = numFrames / iterations;// 1764000 / 20 = 88200 frames per iterasjon                        
        long frameCounter = 0;


        for(int i = 0; i < iterations; i++) {

            int numberOfLivingCells = statistics.getNumberOfLivingCells()[i]; //Viktigst
            int geometrics = statistics.getGeometrics()[i]; //Nest viktigst??
            int diffLivingCells = statistics.getDiffInLivingCells()[i]; //Droppe?
            int similarity = statistics.getSimilarity()[i]; //Droppe?
            
            // LOOP TIL 88200 FRAMES ER SKREVET
            while (frameCounter < framesPerIteration) {
                long remaining = wavFile.getFramesRemaining();

                // determine how many frames to write, up to maximum of the buffer size
                int toWrite = (remaining > 100) ? 100 : (int) remaining;

                // fill the buffer, one tone per channel
                for (int bufferIndex = 0; bufferIndex < toWrite; bufferIndex++, frameCounter++) {
                    buffer[0][bufferIndex] = Math.sin(2.0 * Math.PI * 300 + (geometrics / numberOfLivingCells) * frameCounter / sampleRate);
                    buffer[1][bufferIndex] = Math.sin(2.0 * Math.PI * 200 + (similarity - diffLivingCells) * frameCounter / sampleRate);
                }

                // write the buffer
                wavFile.writeFrames(buffer, toWrite);
            }
            frameCounter = 0;
        }
    }
}
