package audio;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import database.ContactList;


public class AudioSender implements AudioDataListener {
    Map<String, PeerConnection> peers = new ConcurrentHashMap<>();
    volatile boolean running;
    private int sequenceCounter = 0;
    private DatagramSocket peerSocket;
    // Inner Class: Peer Connection
    static class PeerConnection {
        public String peerIp;
        public InetAddress peerAddress;
        public int peerPort;
        public int failureCount;
    }
    //Connection Management
    public void addPeer(String peerId, String peerAddress, int peerPort ) {
        PeerConnection newPeer = new PeerConnection();
        try {
            newPeer.peerAddress = InetAddress.getByName(peerAddress);
            newPeer.peerPort = peerPort;
            peers.put(peerId, newPeer);
            newPeer.failureCount = 0;
        } catch (UnknownHostException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void removePeer(PeerConnection peer) {
        peers.values().remove(peer);
    }

    public void removePeer(String peerId) {
        peers.remove(peerId);
    }

    public int getPeerCount() {
        return peers.size();
    }

    public boolean isConnected(String peerId) {
        return peers.containsKey(peerId);
    }

    // AudioDataListener implementation
    @Override
    public void onAudioData(byte[] data, int bytesRead) {
        if (peers.isEmpty() || bytesRead == 0 || !running) return;
        byte[] audioCopy = Arrays.copyOf(data, bytesRead);
        byte[] packet = buildPacket(audioCopy, bytesRead);
        broadcast(packet);
    }

    //Sending Logic
    private void broadcast(byte[] packet){
        peers.values().forEach(peer -> {
           sendToPeer(peer, packet);
        });
    }

    private void sendToPeer(PeerConnection peer, byte[] packet){
        DatagramPacket newPacket = new DatagramPacket(packet, packet.length, peer.peerAddress, peer.peerPort);
        try {
            peerSocket.send(newPacket);
            peer.failureCount = 0;
        } catch (IOException e) {
            peer.failureCount++;
            System.err.println("Error: " + e.getMessage());;
            if (peer.failureCount > 50) {
                removePeer(peer);
            }
        }
    }

    //Lifecycle Management
    public void start() {
        try {
            peerSocket = new DatagramSocket();
            running = true;
            System.out.println("AudioSender started!");
            sequenceCounter = 0;
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    public void stop() {
        running = false;
        peerSocket.close();
        peers.clear();
    }

    //Packet Building
    public byte[] buildPacket(byte[] audioData, int length){
        ByteBuffer buffer = ByteBuffer.allocate(length + 12);
        buffer.putInt(sequenceCounter++);
        buffer.putLong(System.currentTimeMillis());
        buffer.put(audioData);
        return buffer.array();
    }
}
