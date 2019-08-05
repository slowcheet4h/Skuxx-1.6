package xyz.erik.skuxx.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import xyz.erik.api.command.Command;
import xyz.erik.api.helpers.Helper;

public class Invsee
extends Command{

    public Invsee() {
        super(new String[]{"invsee"},"See Inventory");
    }

    public String execute(String message, String[] split) {
        if (split.length == 0) {
            return "invsee <player>";
        }
        for(Object entity : Helper.mc().theWorld.loadedEntityList)
            if(entity instanceof EntityOtherPlayerMP)
            {
                EntityOtherPlayerMP player = (EntityOtherPlayerMP)entity;
                if(player.getName().equals(split[1]))
                {
                    Helper.mc().displayGuiScreen((GuiScreen)new GuiInventory(player));
                    return "Showing inventory of " + player.getName();
                }
            }

        return "Not found";
    }
}
