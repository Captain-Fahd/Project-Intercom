package audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

public class AudioCapture {
    private AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
    private volatile boolean running;
    private DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
    private TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);

    public AudioCapture() throws Exception {
        if (!AudioSystem.isLineSupported(info)) {
            throw new Exception("Microphone not supported");
        }
        line.open(format);
        line.start();
    }

}
