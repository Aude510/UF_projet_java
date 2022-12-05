package Message;

import UserList.ListeUser;
import NetworkManager.UdpSend;
import UserList.UserItem;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.InetAddress;

public class SetPseudo {
    public static int delaiAttenteMs = 2000;
    public static boolean ackPasOkRecu = false;

    public static boolean pseudoConnexion(String newPseudo){
        try{
            UserItem identity= new UserItem(ListeUser.getUser(0).getId(),newPseudo, InetAddress.getLocalHost());
            UDPMessage pseudoConnexion = new UDPMessage(UDPControlType.Connexion,identity);
            ByteArrayOutputStream bstream = new ByteArrayOutputStream();
            ObjectOutput oo = new ObjectOutputStream(bstream);
            oo.writeObject(pseudoConnexion);
            byte[] sentMessage = bstream.toByteArray();
            UdpSend.envoyerBroadcast(sentMessage);
            Thread.sleep(delaiAttenteMs);
            if(ackPasOkRecu){
                ackPasOkRecu = false;
                return false;
            }else{
                UDPMessage newUserConnected = new UDPMessage(UDPControlType.AckNewUserSurReseau);
                oo.writeObject(newUserConnected);
                byte[] ackNewUser = bstream.toByteArray();
                UdpSend.envoyerBroadcast(ackNewUser);
                return true;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
}