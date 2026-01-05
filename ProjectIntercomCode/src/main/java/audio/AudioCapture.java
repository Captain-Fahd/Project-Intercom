package audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

public class AudioCapture {
    private final AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
    private volatile boolean running;
    private final DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
    private AudioDataListener listener;
    private TargetDataLine line;

    public AudioCapture() throws Exception {
        if (!AudioSystem.isLineSupported(info)) {
            throw new Exception("Microphone not supported");
        }
    }

    public void startCapture(AudioDataListener listener) throws Exception {
        this.listener = listener;
        running = true;
        line = (TargetDataLine) AudioSystem.getLine(info);
        line.open(format);
        line.start();
        new Thread(this::runningLoop).start();
    }

    public void stopCapture() {
        running = false;
        line.stop();
        line.close();
    }

    private void runningLoop() {
        byte[] buffer = new byte[2048];
        while (running) {
            int bytesRead = line.read(buffer, 0, buffer.length);
            if (bytesRead > 0 && listener != null) {
                listener.onAudioData(buffer, bytesRead);
            }
        }
    }
}
