package xyz.erik.skuxx.commands;

import net.minecraft.network.play.client.C03PacketPlayer;
import xyz.erik.api.command.Command;
import xyz.erik.api.helpers.Helper;

public class Vclip
extends Command{


    public Vclip() {
        super(new String[]{"vclip","vc"},"Vertical Clip");
    }
    public String execute(String message, String[] split) {
        double block = Double.parseDouble(split[1]);

        Helper.player().setPositionAndUpdate(Helper.player().posX,Helper.player().posY + block,Helper.player().posZ);
        return "";
    }
}
