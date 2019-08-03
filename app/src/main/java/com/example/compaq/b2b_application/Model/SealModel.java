package com.example.compaq.b2b_application.Model;

public class SealModel {
    String seal,melting;

    public SealModel(String seal, String melting) {
        this.seal = seal;
        this.melting = melting;
    }

    public String getSeal() {
        return seal;
    }

    public void setSeal(String seal) {
        this.seal = seal;
    }

    public String getMelting() {
        return melting;
    }

    public void setMelting(String melting) {
        this.melting = melting;
    }
}
