package audio;

@FunctionalInterface
public interface AudioDataListener {
    void onAudioData(byte[] data, int bytesRead);
}
