package corbaServer;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import service.BanqueImpl;

public class BanqueServer {
    public static void main(String[] args) {
        try {
            // a. Initialiser l'ORB (adresse locale forcée dans les références)
            Properties props = new Properties();
            props.put("com.sun.CORBA.ORBServerHost", "127.0.0.1");
            ORB orb = ORB.init(args, props);

            // b. Récupérer le RootPOA et activer le POAManager
            POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootPOA.the_POAManager().activate();

            // c. Configurer l'annuaire JNDI (lit jndi.sproperties)
            Context ctx = new InitialContext();

            // d. Créer le servant
            BanqueImpl banqueImpl = new BanqueImpl();

            // e. Enregistrer sa référence dans l'annuaire
            org.omg.CORBA.Object ref = rootPOA.servant_to_reference(banqueImpl);
            ctx.rebind("BanqueService", ref);

            System.out.println("Serveur Banque prêt, en attente des clients...");

            // f. Attendre les requêtes des clients
            orb.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}