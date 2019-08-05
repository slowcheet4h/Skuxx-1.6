package xyz.erik.skuxx.mods.player;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.item.ItemStack;
import xyz.erik.api.config.vals.Bool;
import xyz.erik.api.config.vals.Int;
import xyz.erik.api.helpers.ErikTimer;
import xyz.erik.api.mod.Mod;
import xyz.erik.skuxx.event.Event;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventMotion;
import xyz.erik.skuxx.mods.Category;

public class ChestStealer
extends Mod {

    private ErikTimer timer = new ErikTimer();
    private Int delay = new Int("Delay", 150, 10, 250);
    public Bool drop = new Bool("Drop", false);

    public ChestStealer() {
        addSet(delay);
        addSet(drop);
        setCategory(Category.PLAYER);
    }

    @EventTarget
    private void onMotion(EventMotion event) {
        if (event.getMotion() != Event.Motion.BEFORE) return;
        final GuiChest chest = (GuiChest) this.minecraft.currentScreen;
        for (int index = 0; index < chest.getLowerChestInventory().getSizeInventory(); ++index) {
            final ItemStack stack = chest.getLowerChestInventory().getStackInSlot(index);
            if (stack != null) {
                if (this.timer.delay((long) this.delay.getValue())) {
                    if (!drop.isState()) {
                        this.minecraft.playerController.windowClick(chest.inventorySlots.windowId, index, 0, 1, this.minecraft.thePlayer);
                    } else {
                        this.minecraft.playerController.windowClick(chest.inventorySlots.windowId, index, 0, 4, this.minecraft.thePlayer);
                    }
                    this.timer.reset();
                }
            }
        }
    }
}
