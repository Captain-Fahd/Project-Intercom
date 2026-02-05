package audio;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AudioTest {
    public static void main(String[] args){
        try {
            System.out.println("Creating shared audio queue...");
            BlockingQueue<byte[]> audioQueue = new LinkedBlockingQueue<>(10);

            System.out.println("Creating AudioPlayback and AudioCapture...");
            AudioPlayback playback = new AudioPlayback(audioQueue);
            AudioCapture capture = new AudioCapture();

            System.out.println("Starting playback...");
            playback.playAudio();

            System.out.println("Starting capture...");
            // Give capture a listener that puts audio into the queue
            capture.startCapture((data, bytesRead) -> {
                try {
                    // Copy the audio data
                    byte[] audioChunk = new byte[bytesRead];
                    System.arraycopy(data, 0, audioChunk, 0, bytesRead);
                    // Put it in the queue for playback
                    audioQueue.put(audioChunk);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

            System.out.println("Loopback active! Speak into your microphone.");
            System.out.println("You should hear yourself with a slight delay.");
            System.out.println("Running for 10 seconds...");

            // Let it run for 10 seconds
            Thread.sleep(10000);

            System.out.println("Stopping...");
            capture.stopCapture();
            playback.stopPlayback();

            System.out.println("Test complete!");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
