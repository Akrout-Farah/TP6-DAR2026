package service;

import corbaBanque.*;
import java.util.HashMap;
import java.util.Map;

public class BanqueImpl extends IBanqueRemotePOA {

    private static final double TAUX_EUR_DT = 3.40;

    private final Map<Integer, Compte> comptes = new HashMap<>();

    @Override
    public void creerCompte(Compte cpte) {
        comptes.put(cpte.code, new Compte(cpte.code, cpte.solde));
    }

    @Override
    public void verser(float mt, int code) {
        Compte c = comptes.get(code);
        if (c != null && mt > 0) {
            c.solde += mt;
        }
    }

    @Override
    public void retirer(float mt, int code) {
        Compte c = comptes.get(code);
        if (c != null && mt > 0 && c.solde >= mt) {
            c.solde -= mt;
        }
    }

    @Override
    public Compte getCompte(int code) {
        Compte c = comptes.get(code);
        return (c != null) ? c : new Compte(-1, 0f);
    }

    @Override
    public Compte[] getComptes() {
        return comptes.values().toArray(new Compte[0]);
    }

    @Override
    public double conversion(float mt) {
        return mt * TAUX_EUR_DT;
    }
}
