package backend.recorder;

import backend.util.R;

import javax.sound.sampled.*;
import java.io.*;
 
/**
 * A sample program is to demonstrate how to record sound in Java
 *
 */
public class SoundRecorder {
    // record duration, in milliseconds
    //static final long RECORD_TIME = 3000;
 
    // path of the wav file
    File wavFile = new File(R.RECORD_PATH);
    
    // format of audio file
    AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
 
    // the line from which audio data is captured
    TargetDataLine line;

    // type of Audio for Google STT request
    public final static int GOOGLE = 1;
    public final static int IBM = 2;

    /**
     * Defines an audio format
     */
    AudioFormat getAudioFormatGoogle() {
        float sampleRate = 16000;
        int sampleSizeInBits = 8;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
                                             channels, signed, bigEndian);
        return format;
    }

    AudioFormat getAudioFormatIBM() {
        float sampleRate = 48000;
        int sampleSizeInBits = 16;
        int channels = 2;
        int frameSize = 4;
        float frameRate = 48000;
        boolean bigEndian = false;
        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                sampleRate, sampleSizeInBits, channels, frameSize, frameRate, bigEndian);

        return format;
    }

    /**
     * Captures the sound and record into a WAV file
     */
    public void start(int type) {
        try {
            AudioFormat format = (type == GOOGLE) ? getAudioFormatGoogle() : getAudioFormatIBM();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
 
            // checks if system supports the data line
            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Line not supported");
                System.exit(0);
            }
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();   // start capturing
 
            System.out.println("\nRecorder:");
 
            AudioInputStream ais = new AudioInputStream(line);
 
            System.out.println("Start recording...");
 
            // start recording
            AudioSystem.write(ais, fileType, wavFile);
 
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
 
    /**
     * Closes the target data line to finish capturing and recording
     */
    public void stop() {
        line.stop();
        line.close();
        System.out.println("Finished");
    }
 
    /**
     * Entry to run the program
     */
//    public static void main(String[] args) {
//        final SoundRecorder recorder = new SoundRecorder();
//
//        // creates a new thread that waits for a specified
//        // of time before stopping
//        Thread stopper = new Thread(new Runnable() {
//            public void run() {
//                try {
//                    Thread.sleep(RECORD_TIME);
//                } catch (InterruptedException ex) {
//                    ex.printStackTrace();
//                }
//                recorder.stop();
//            }
//        });
//
//        stopper.start();
//
//        // start recording
//        recorder.start();
//    }

}