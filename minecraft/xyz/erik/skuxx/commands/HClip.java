package xyz.erik.skuxx.commands;

import net.minecraft.util.BlockPos;
import xyz.erik.api.command.Command;
import xyz.erik.api.helpers.Helper;

public class HClip
extends Command{


    public HClip() {
        super(new String[]{"hclip"},"Horizontal Clip");
    }

    public String execute(String message, String[] split) {
        if (split.length < 1) {
            return "hclip <blocks>";
        }
        double blocks = Double.parseDouble(split[1]);
        double x = Math.cos(Math.toRadians(Helper.player().rotationYaw + 90.0F));
        double z = Math.sin(Math.toRadians(Helper.player().rotationYaw + 90.0F));
        BlockPos pos = new BlockPos(Helper.player().posX + (1.0D * blocks * x + 0.0D * blocks * z), Helper.player().posY, Helper.player().posZ + (1.0D * blocks * z - 0.0D * blocks * x));
     //   Helper.blinkToPos(new double[]{Helper.player().posX,Helper.player().posY,Helper.player().posZ},pos,0.0);
        Helper.player().setPositionAndUpdate(pos.getX(),pos.getY(),pos.getZ());
        return String.format("Teleported %s &e%s&7 block(s).", new Object[] { blocks < 0.0D ? "back" : "forward", Double.valueOf(blocks) });
    }
}
