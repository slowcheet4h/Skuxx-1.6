package xyz.erik.skuxx.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.layers.LayerDeadmau5Head;
import xyz.erik.api.command.Command;
import xyz.erik.skuxx.Skuxx;

public class SetEar
extends Command{


    public SetEar() {
        super(new String[]{"se","setear"},"GET EARS");
    }

    public String execute(String message, String[] split) {

        boolean state = Boolean.parseBoolean(split[1]);
        Skuxx.getInstance().getSkuxxPlayer().setHaveEar(state);
        Skuxx.getInstance().updateSkuxxPlayer();
        return "";
    }
}
