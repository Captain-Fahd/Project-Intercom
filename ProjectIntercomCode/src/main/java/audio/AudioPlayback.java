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
        audioQueue = new LinkedBlockingQueue<>(10);
        line = (SourceDataLine) AudioSystem.getLine(info);
        line.open(format);
        line.start();
        new Thread(this::runningLoop).start();
        }

    public void runningLoop() {
    }


}
