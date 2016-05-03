/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.FileManagement.OtherFormats;

import Model.FileManagement.OtherFormats.Data.WavData;
import Model.GameOfLife.Statistics;
import Wav.WavFile;
import Wav.WavFileException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author stianreistadrogeberg
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
    
    public WavSaver(WavData data) {
        iterations = data.getIterations();
        sampleRate = data.getSamplerate(); //Samples per second
        durationInSeconds = data.getDurationInSeconds();
        channels = data.getChannels();
        bits = data.getBits();
        file = data.getFile();
        
        statistics = new Statistics(data.getGame(), data.getIterations());//Lag en tråd som gjør dette?
        
        
        
        
    }
    
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
    
    private void generateSound() throws IOException, WavFileException {
        double[][]buffer = new double[wavFile.getNumChannels()][100];

        long numFrames = wavFile.getNumFrames(); //antallet som må skrives for å fylle x antall sekunder med lyd
                                                 //Dette skrives i indelte blokker på 100 frames
                        
        long framesPerIteration = numFrames / iterations;// 1764000 / 20 = 88200 frames per iterasjon                        
        long frameCounter = 0;

        //I løpet av en iterasjon av DENNE for-loopen må man ha skrevet 88200 frames
        for(int i = 0; i < iterations; i++) {
            //PER GENERASJON HAR MAN DISSE VARIABLENE Å LAGE LYD FRA
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
                    buffer[0][bufferIndex] = Math.sin(2.0 * Math.PI * 400 + numberOfLivingCells * frameCounter / sampleRate);
                    buffer[1][bufferIndex] = Math.sin(2.0 * Math.PI * 500 + geometrics * frameCounter / sampleRate);
                }

                // write the buffer
                wavFile.writeFrames(buffer, toWrite);
            }
            frameCounter = 0;
        }
        
    }
    
}
