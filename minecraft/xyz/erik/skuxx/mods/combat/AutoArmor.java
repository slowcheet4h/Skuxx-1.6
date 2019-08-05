package xyz.erik.skuxx.mods.combat;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import xyz.erik.api.helpers.ErikTimer;
import xyz.erik.api.mod.Mod;
import xyz.erik.skuxx.event.Event;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventMotion;
import xyz.erik.skuxx.mods.Category;

public class AutoArmor
extends Mod
{

    private int[] helmet;
    private int[] chestplate;
    private int[] leggings;
    private int[] boots;
    public ErikTimer timer = new ErikTimer();

    public AutoArmor()
    {
        setCategory(Category.COMBAT);

        this.helmet = new int[] { 310, 306, 314, 302, 298 };
        this.chestplate = new int[] { 311, 307, 315, 303, 299 };
        this.leggings = new int[] { 312, 308, 316, 304, 300 };
        this.boots = new int[] { 313, 309, 317, 305, 301 };
    }

    @EventTarget
    public void eat(EventMotion event)
    {
        if (event.getMotion() == Event.Motion.BEFORE)
        {
            int armor = -1;
            if (this.timer.delay(250.0F))
            {
                if (getPlayer().inventory.armorInventory[0] == null)
                {
                    int[] boots;
                    int length = (boots = this.boots).length;
                    for (int i = 0; i < length; i++)
                    {
                        int item = boots[i];
                        if (findItem(item) != -1)
                        {
                            armor = findItem(item);
                            break;
                        }
                    }
                }
                if (getPlayer().inventory.armorInventory[1] == null)
                {
                    int[] leggings;
                    int length2 = (leggings = this.leggings).length;
                    for (int j = 0; j < length2; j++)
                    {
                        int item = leggings[j];
                        if (findItem(item) != -1)
                        {
                            armor = findItem(item);
                            break;
                        }
                    }
                }
                if (getPlayer().inventory.armorInventory[2] == null)
                {
                    int[] chestplate;
                    int length3 = (chestplate = this.chestplate).length;
                    for (int k = 0; k < length3; k++)
                    {
                        int item = chestplate[k];
                        if (findItem(item) != -1)
                        {
                            armor = findItem(item);
                            break;
                        }
                    }
                }
                if (getPlayer().inventory.armorInventory[3] == null)
                {
                    int[] helmet;
                    int length4 = (helmet = this.helmet).length;
                    for (int l = 0; l < length4; l++)
                    {
                        int item = helmet[l];
                        if (findItem(item) != -1)
                        {
                            armor = findItem(item);
                            break;
                        }
                    }
                }
                if (armor != -1)
                {
                    this.minecraft.playerController.windowClick(0, armor, 0, 1, getPlayer());
                    this.timer.reset();
                }
            }
        }
    }

    private int findItem(int id)
    {
        for (int index = 9; index < 45; index++)
        {
            ItemStack item = getPlayer().inventoryContainer.getSlot(index).getStack();
            if ((item != null) && (Item.getIdFromItem(item.getItem()) == id)) {
                return index;
            }
        }
        return -1;
    }
}
