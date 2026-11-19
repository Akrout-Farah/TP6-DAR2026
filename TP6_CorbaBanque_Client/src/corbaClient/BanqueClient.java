package corbaClient;

import corbaBanque.Compte;
import corbaBanque.IBanqueRemote;
import corbaBanque.IBanqueRemoteHelper;
import javax.naming.Context;
import javax.naming.InitialContext;

public class BanqueClient {

    private static void afficher(Compte c) {
        System.out.println("  Compte " + c.code + " : solde = " + c.solde);
    }

    public static void main(String[] args) {
        try {
            Context ctx = new InitialContext();
            org.omg.CORBA.Object ref =
                    (org.omg.CORBA.Object) ctx.lookup("BanqueService");
            IBanqueRemote stub = IBanqueRemoteHelper.narrow(ref);

            System.out.println("1) Creation de deux comptes");
            stub.creerCompte(new Compte(1, 1000f));
            stub.creerCompte(new Compte(2, 500f));
            afficher(stub.getCompte(1));
            afficher(stub.getCompte(2));

            System.out.println("2) Versement de 200 sur le compte 1");
            stub.verser(200f, 1);
            afficher(stub.getCompte(1));

            System.out.println("3) Retrait de 300 sur le compte 1");
            stub.retirer(300f, 1);
            afficher(stub.getCompte(1));

            System.out.println("4) Retrait de 5000 sur le compte 2 (solde insuffisant, refuse)");
            stub.retirer(5000f, 2);
            afficher(stub.getCompte(2));

            System.out.println("5) Liste de tous les comptes");
            for (Compte c : stub.getComptes()) {
                afficher(c);
            }

            System.out.println("6) Conversion de 100 euros en dinars");
            System.out.println("  100 EUR = " + stub.conversion(100f) + " DT");

            System.out.println("7) Consultation d'un compte inexistant (code 99)");
            Compte inconnu = stub.getCompte(99);
            if (inconnu.code == -1) {
                System.out.println("  Compte introuvable");
            }

            ctx.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
