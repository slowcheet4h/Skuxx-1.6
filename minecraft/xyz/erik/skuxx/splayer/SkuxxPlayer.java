package xyz.erik.skuxx.splayer;

import net.minecraft.entity.player.EntityPlayer;

public class SkuxxPlayer {

    private boolean sleeping = false;
    private boolean isChild = false;
    private CapeTYPE cape = null;
    private boolean haveEar = false;
    private String name = "";
    private boolean isRiding = false;
    private String ingameName = "";
    public SkuxxPlayer() {}

    public enum CapeTYPE {
        DURACAPE("duracape.png"),
        ATATURK("ataturk.png"),
        ERIKCAPE("aka.png"),
        DOGUSCAPE("semocape.png"),
        DONERCAPE("donercape.png"),
        CITY("city.png"),
        NOTFOUND("404-hdcape.png"),
        ANIMEGIRL("animegirl.png"),
        EPIC("epic.png"),
        KOM("kom.png"),
        NIKE("nikecape.png"),
        SKULLCAPE("skullcape.png"),
        SKYCAPE("skycape.png"),
        WING("wing.png"),
        doksanyedi("97.png"),
        SAGA("saga.png"),
        ASTRAL("astral.png"),
        TRIANGLE("triangle.png"),
        PLAYME("playme.png"),
        MOON("mooncape.png"),
        BATMAN("batman.png"),
        AYI("ayicape.png"),
        KAYRA("kayracape.png");
        CapeTYPE(String fileName) {
            this.fileName = fileName;
        }
        public String fileName;
    }


    public void set(String ingameName, boolean isChild, boolean haveEar, boolean isRiding, CapeTYPE cape) {
        this.ingameName = ingameName;
        this.isChild = isChild;
        this.isRiding = isRiding;
        this.haveEar = haveEar;
        this.isChild = isChild;
        this.cape = cape;
    }

    public void setSleeping(boolean sleeping) {
        this.sleeping = sleeping;
    }

    public boolean isSleeping() {
        return sleeping;
    }

    public void setChild(boolean child) {
        isChild = child;
    }

    public String getName() {
        return name;
    }

    public String getIngameName() {
        return ingameName;
    }

    public void setIngameName(String ingameName) {
        this.ingameName = ingameName;
    }

    public CapeTYPE getCape() {
        return cape;
    }

    public void setCape(CapeTYPE cape) {
        this.cape = cape;
    }

    public boolean isHaveEar() {
        return haveEar;
    }

    public boolean isRiding() {
        return isRiding;
    }

    public void setRiding(boolean riding) {
        isRiding = riding;
    }

    public void setHaveEar(boolean haveEar) {
        this.haveEar = haveEar;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChild() {
        return isChild;
    }
}
