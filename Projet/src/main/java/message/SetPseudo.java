package message;

import networkManager.UDPSend;
import userList.ListeUser;
import userList.UserItem;

public class SetPseudo {
    public static int delaiAttenteMs = 2000;
    public static boolean ackPasOkRecu = false;

    public static boolean  connexionEtablie = false;
    public static boolean pseudoConnexion(){
        try{
            UserItem identity= new UserItem(ListeUser.getMyId(),ListeUser.getMyPseudo());
            UDPMessage pseudoConnexion = new UDPMessage(UDPControlType.Connexion,identity);
            UDPSend.envoyerBroadcast(pseudoConnexion);
            Thread.sleep(delaiAttenteMs);
            if(ackPasOkRecu){
                ackPasOkRecu = false;
                return false;
            }else{
                UDPMessage newUserConnected = new UDPMessage(UDPControlType.AckNewUserSurReseau, identity);
                UDPSend.envoyerBroadcast(newUserConnected);
                connexionEtablie = true;
                return true;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean changerPseudo(String newPseudo){
        UDPMessage pseudoConnexion;

        if(ListeUser.pseudoDisponible(newPseudo)){
            try {
                ListeUser.setMyPseudo(newPseudo);
                UserItem newIdentity= new UserItem(ListeUser.getMyId(),ListeUser.getMyPseudo());
                if(connexionEtablie){
                    pseudoConnexion = new UDPMessage(UDPControlType.ChangementPseudo,newIdentity);
                }else{
                    pseudoConnexion = new UDPMessage(UDPControlType.AckNewUserSurReseau,newIdentity);
                }
                UDPSend.envoyerBroadcast(pseudoConnexion);
                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }else{
            return false;
        }
    }
}
