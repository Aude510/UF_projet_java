package chavardage.conversation;

import chavardage.message.TCPMessage;
import chavardage.message.TCPType;
import chavardage.message.WrongConstructorException;
import chavardage.networkManager.TCPReceiveData;
import chavardage.networkManager.TCPSendData;
import chavardage.userList.ListeUser;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.Assert.assertEquals;

public class ConversationTest {



    @Test
    public void conversationTest() throws WrongConstructorException {
        ListeUser.getInstance().setMyId(3); // j'ai l'id 3
        Conversation conversation = new Conversation(5); // je crée une conversation avec 5
        conversation.sendMessage("coucou");
        assertEquals(5, conversation.getDestinataireId());
        conversation.accept(new TCPMessage(3, "hola"));
        // on teste que les exceptions se lancent bien quand il faut
        System.out.println("3 conversationException sont attendues : ");
        conversation.accept(new TCPMessage(3, TCPType.OuvertureSession, 6));
        conversation.accept(new TCPMessage(3, TCPType.OuvertureSession, 6));
        conversation.accept(new TCPMessage(4, "ahah"));
        conversation.accept(new TCPMessage(3, TCPType.FermetureSession));
        conversation.accept(new TCPMessage(3, TCPType.FermetureSession));
        Conversation defaultConv = new Conversation();
        assertEquals(0, defaultConv.getDestinataireId());
        defaultConv.accept(new TCPMessage(3, TCPType.OuvertureSession, 6));
        assertEquals(6, defaultConv.getDestinataireId());
    }


    @Test
    public synchronized void acceptTest() throws IOException, WrongConstructorException {
        ListeUser.getInstance().setMyId(3);
        ServerSocket serverSocket = new ServerSocket(8476);
        Conversation conversation = new Conversation();
        Socket socketEnvoi = new Socket(InetAddress.getLocalHost(), 8476);
        Socket socketReception = serverSocket.accept();
        TCPReceiveData receiveData = new TCPReceiveData(socketReception);
        receiveData.setSubscriber(conversation);
        assertEquals(0,conversation.getDestinataireId());
        TCPSendData sendData = new TCPSendData(socketEnvoi);
        sendData.envoyer(new TCPMessage(3,TCPType.OuvertureSession,6));
        assertEquals(6,conversation.getDestinataireId());
    }

}
