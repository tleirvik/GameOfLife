/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.FileManagement.OtherFormats;

import Model.GameOfLife.GameOfLife;
import Model.GameOfLife.Statistics;
import Wav.WavFile;
import Wav.WavFileException;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author stianreistadrogeberg
 */
public class WavSaver {
    private final WavFile wavFile;
    private final Statistics statistics;
    
    public WavSaver(GameOfLife game, int iterations, File f, int sampleRate, int channels, 
            int durationInSeconds, int bits) throws IOException, WavFileException {
        statistics = new Statistics(game, iterations);//Lag en tråd som gjør dette?
        
        f = new File("Wav.wav");
        sampleRate = 44100; //Samples per second
        durationInSeconds = 40;
        channels = 2;
        bits = 16;
        
        long numFrames = (long) (durationInSeconds * sampleRate);// 44100 * 40 = 1764000
                
        wavFile = WavFile.newWavFile(f, channels, numFrames, bits, sampleRate);
        
        //Join tråd her?
        generateSound();
        wavFile.close();
    }
    
    private void generateSound() throws IOException, WavFileException {
        // a buffer of 100 frames
        double[][]buffer = new double[wavFile.getNumChannels()][100];

        int iterations = statistics.getIterations();
        long numFrames = wavFile.getNumFrames(); //antallet som må skrives for å fylle x antall sekunder med lyd
                                                 //Dette skrives i indelte blokker på 100 frames
        
        int sampleRate = 44100;//Fiks hardkodingen
                                                 
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
