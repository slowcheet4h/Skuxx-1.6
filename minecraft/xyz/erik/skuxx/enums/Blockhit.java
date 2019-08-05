package xyz.erik.skuxx.enums;

public enum Blockhit
{
    Skuxx(1),Exeter(2),Sigma(3);
    int id;
    Blockhit(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
