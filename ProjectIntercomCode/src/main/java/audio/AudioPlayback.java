package audio;

import javax.sound.sampled.*;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AudioPlayback {
    private final AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
    private volatile boolean running;
    DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
    private SourceDataLine line;
    private BlockingQueue<byte[]> audioQueue;

    public AudioPlayback(BlockingQueue<byte[]> audioQueue) throws Exception {
        this.audioQueue = audioQueue;
        if (!AudioSystem.isLineSupported(info)) {
            throw new Exception("Speaker not supported");
        }
    }



    public void playAudio() throws Exception {
        running = true;
        line = (SourceDataLine) AudioSystem.getLine(info);
        line.open(format);
        line.start();
        new Thread(this::runningLoop).start();
        }

    private void runningLoop() {
        while (running) {
            try{
                byte[] audioData = audioQueue.take();
                line.write(audioData, 0, audioData.length);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void stopPlayback() {
        running = false;
        if (line != null) {
            line.stop();
            line.close();
        }
    }


}
