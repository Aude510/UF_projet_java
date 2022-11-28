package NetworkManager;
import java.net.*;

public class UdpSend {
    private static InetAddress broadcastAddress;

    static {
        try {
            broadcastAddress = InetAddress.getByName("255.255.255.255");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    private static DatagramSocket socketSend;
    private static DatagramPacket sendPacket;

    public static void envoyerBroadcast(byte[] message){
        try {
            socketSend = new DatagramSocket();
            socketSend.setBroadcast(true);
            sendPacket = new DatagramPacket(message, message.length);
            sendPacket.setAddress(broadcastAddress);
            sendPacket.setPort(ThreadUdpReceive.receivePort);
            socketSend.send(sendPacket);
            socketSend.close();
        }catch (Exception e){e.printStackTrace();}
    }

    public static void envoyerUnicast(byte[] message, InetAddress receiverAddress){
        try {
            socketSend = new DatagramSocket();
            sendPacket = new DatagramPacket(message,message.length);
            sendPacket.setAddress(receiverAddress);
            sendPacket.setPort(ThreadUdpReceive.receivePort);
            socketSend.send(sendPacket);
            socketSend.close();
        }catch (Exception e){e.printStackTrace();}
    }

}