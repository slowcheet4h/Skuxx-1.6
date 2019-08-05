package xyz.erik.skuxx.commands;

import net.minecraft.util.BlockPos;
import xyz.erik.api.command.Command;
import xyz.erik.skuxx.mods.player.SpeedBuilders;

public class Spdset
extends Command{


    public Spdset() {
        super(new String[]{"speedbuilderset","spdtest"},"Sets location for speed builders");
    }

    public String execute(String message, String[] split) {
        int x = Integer.parseInt(split[1].split("::")[0]);
        int y = Integer.parseInt(split[1].split("::")[1]);
        int z = Integer.parseInt(split[1].split("::")[2]);
        SpeedBuilders.middlePos = new BlockPos(x,y,z);
        return "Setted to x:" + x + " y:" + y + " z:" + z;
    }
}
