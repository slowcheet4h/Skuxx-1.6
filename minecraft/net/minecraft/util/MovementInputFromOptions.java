package net.minecraft.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.GameSettings;
import org.lwjgl.input.Keyboard;
import xyz.erik.api.helpers.Helper;
import xyz.erik.skuxx.Skuxx;

public class MovementInputFromOptions extends MovementInput
{
    private final GameSettings gameSettings;
    private static final String __OBFID = "CL_00000937";

    public MovementInputFromOptions(GameSettings p_i1237_1_)
    {
        this.gameSettings = p_i1237_1_;
    }

    public void updatePlayerMoveState()
    {
        if ((Skuxx.getInstance().getModManager().getMod("Inventory").getState()) && (!(Minecraft.getMinecraft().currentScreen instanceof GuiChat)))
        {
            this.moveStrafe = 0.0F;
            this.moveForward = 0.0F;
            if (Keyboard.isKeyDown(this.gameSettings.keyBindForward.getKeyCode())) {
                this.moveForward += 1.0F;
            }
            if (Keyboard.isKeyDown(this.gameSettings.keyBindSneak.getKeyCode())) {
                Helper.player().setSneaking(true);
            }
            if (Keyboard.isKeyDown(this.gameSettings.keyBindBack.getKeyCode())) {
                this.moveForward -= 1.0F;
            }
            if (Keyboard.isKeyDown(this.gameSettings.keyBindLeft.getKeyCode())) {
                this.moveStrafe += 1.0F;
            }
            if (Keyboard.isKeyDown(this.gameSettings.keyBindRight.getKeyCode())) {
                this.moveStrafe -= 1.0F;
            }
            this.jump = Keyboard.isKeyDown(this.gameSettings.keyBindJump.getKeyCode());
            this.sneak = this.gameSettings.keyBindSneak.pressed;

            if (this.sneak)
            {
                this.moveStrafe = ((float)(this.moveStrafe * 0.3D));
                this.moveForward = ((float)(this.moveForward * 0.3D));
            }
        }
        else
        {
            this.moveStrafe = 0.0F;
            this.moveForward = 0.0F;
            if (this.gameSettings.keyBindForward.isKeyDown()) {
                this.moveForward += 1.0F;
            }
            if (this.gameSettings.keyBindBack.isKeyDown()) {
                this.moveForward -= 1.0F;
            }
            if (this.gameSettings.keyBindLeft.isKeyDown()) {
                this.moveStrafe += 1.0F;
            }
            if (this.gameSettings.keyBindRight.isKeyDown()) {
                this.moveStrafe -= 1.0F;
            }
            this.jump = this.gameSettings.keyBindJump.isKeyDown();
            this.sneak = this.gameSettings.keyBindSneak.isKeyDown();
            if (this.sneak)
            {
                this.moveStrafe = ((float)(this.moveStrafe * 0.3D));
                this.moveForward = ((float)(this.moveForward * 0.3D));
            }
        }
    }
}
