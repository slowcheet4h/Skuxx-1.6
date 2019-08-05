package xyz.erik.api.helpers;

import com.sun.org.apache.regexp.internal.RE;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockWall;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import optifine.FontUtils;
import xyz.erik.api.font.*;
import xyz.erik.api.player.Player;
import xyz.erik.skuxx.cfg.SkuxxConfig;

import java.awt.*;
import java.util.List;

public class Helper {
    private static FontUtil segaui = null;
    private static FontUtil mainFont = null;
    private static Minecraft minecraft = Minecraft.getMinecraft();
    private static FontUtil century = null;
    private static FontUtil minecraftFont = new FontUtil("Minecraft",0,16);
    private static FontUtil Comfortaa;
    private static NahrFont guiFont = null;
    private static FontUtil alternative = null;
    public static Minecraft mc() {
        return minecraft;
    }
    private static TextHelper text = null;
    private static NahrFont consolasFont = new NahrFont("font", new Font("Consolas", Font.PLAIN, 18), 18, 0.0F, true, true);
    private static FontUtil cool = null;
    private static FontUtil renderFont = null;
    private static FontUtil mainMenuFont = null;
    private static FontUtil smallFont = null;
    private static NahrFont skuxxFont = null;
    private static FontUtil courerer = new FontUtil("Courier New",Font.BOLD,20);
    private static FontUtil courererSmall = new FontUtil("Courier New",Font.BOLD,16);
    private static NahrFont font;

    public static NahrFont font()
    {
        return font == null ? (font = new NahrFont("HUD", new Font("redmodfont", 19, 19), 19, 0.0F, true, true)) : font;
    }

    public static FontUtil getMainMenuFont() {
        return mainMenuFont == null ? (mainMenuFont =  new FontUtil("Segoe UI",Font.BOLD,72)) : mainMenuFont;
    }

    public static FontUtil getCool() {
        return cool == null ? cool = new FontUtil("Calibri",Font.PLAIN,16) : cool;
    }


    public static FontUtil getMainFont() {
        return mainFont;
    }

    private static DefaultFontRenderer defaultFont = (DefaultFontRenderer) VFontRenderer.createFontRenderer(VFontRenderer.FontObjectType.DEFAULT);

    public static FontUtil getCourererSmall() {
        return courererSmall;
    }

    public static float[] getFacingRotations(int x, int y, int z, EnumFacing facing)
    {
        Entity temp = new EntitySnowball(minecraft.theWorld);
        temp.posX = (x + 0.5D);
        temp.posY = (y + 0.5D);
        temp.posZ = (z + 0.5D);
        temp.posX += facing.getDirectionVec().getX() * 0.25D;
        temp.posY += facing.getDirectionVec().getY() * 0.25D;
        temp.posZ += facing.getDirectionVec().getZ() * 0.25D;
        return getRotationsAtLocation(Location.CHEST,temp);
    }
    public static float wrapAngleTo180(float angle)
    {
        angle %= 360.0F;
        if (angle >= 180.0F) {
            angle -= 360.0F;
        }
        if (angle < -180.0F) {
            angle += 360.0F;
        }
        return angle;
    }


    public static float[] getAverageRotations(final List<EntityLivingBase> targetList) {
        double posX = 0.0;
        double posY = 0.0;
        double posZ = 0.0;
        for (final Entity ent : targetList) {
            posX += ent.posX;
            posY += ent.boundingBox.maxY - 2.0;
            posZ += ent.posZ;
        }
        posX /= targetList.size();
        posY /= targetList.size();
        posZ /= targetList.size();
        return new float[] { getRotationFromPosition(posX, posZ, posY)[0], getRotationFromPosition(posX, posZ, posY)[1] };
    }

    public static float[] getRotationFromPosition(final double x, final double z, final double y) {
        final double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
        final double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
        final double yDiff = y - Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight();
        final double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        final float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793));
        return new float[] { yaw, pitch };
    }

    public static float[] getRotationsAtLocation(Location loc, final Entity entity) {
        final double positionX = entity.posX - minecraft.thePlayer.posX;
        final double positionZ = entity.posZ - minecraft.thePlayer.posZ;
        double locationMath = 0.0;
        switch (loc) {
            case HEAD://HEAD
                locationMath = 1.3;
                break;
            case CHEST: //BODY
                locationMath = 1.3;
                break;
            case LEG://LEGS
                locationMath = 2.9;
                break;
            case FEET: //FEET
                locationMath = 4.0;
                break;
            case SPECIAL:
                locationMath = 3.45;
                break;
            default:
                locationMath = 1.3;
                break;
        }
        final double positionY = entity.posY + entity.getEyeHeight() / locationMath - (minecraft.thePlayer.posY +minecraft.thePlayer.getEyeHeight());
        final double positions = MathHelper .sqrt_double(positionX * positionX + positionZ * positionZ);
        final float yaw = (float)(Math.atan2(positionZ, positionX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(positionY, positions) * 180.0 / 3.141592653589793));
        return new float[] { yaw -(SkuxxConfig.reverseHead ? -180 : 0), pitch - (SkuxxConfig.reverseHead ? -180 : 0)};
    }

    public static NahrFont getSkuxxFont() {
        return skuxxFont == null ? (skuxxFont = new NahrFont("HUD", new Font("Verdana", 19, 19), 19, 0.0F, true, true)) : skuxxFont;
    }

    public static DefaultFontRenderer getFont() {

        return defaultFont;
    }


    public static NahrFont getConsolasFont() {
        return consolasFont;
    }

    public static FontUtil getMinecraftFont() {
        return minecraftFont;
    }

    public static float[] getBlockRotation(BlockPos pos) {
        EntitySnowball entity = new EntitySnowball(mc().theWorld);
        entity.setPosition(pos.getX(),pos.getY(),pos.getZ());
        final double positionX = entity.posX - minecraft.thePlayer.posX;
        final double positionZ = entity.posZ - minecraft.thePlayer.posZ;

        double locationMath = 1.3;
        final double positionY = entity.posY + entity.getEyeHeight() / locationMath - (minecraft.thePlayer.posY +minecraft.thePlayer.getEyeHeight());
        final double positions = MathHelper .sqrt_double(positionX * positionX + positionZ * positionZ);
        final float yaw = (float)(Math.atan2(positionZ, positionX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(positionY, positions) * 180.0 / 3.141592653589793));
        return new float[] { yaw, pitch };
    }



    public static float getDistanceBetweenAngles(float angle1, float angle2)
    {
        float angle = Math.abs(angle1 - angle2) % 360.0F;
        if (angle > 180.0F) {
            angle = 360.0F - angle;
        }
        return angle;
    }

    public static FontUtil getArrayFont() {
        return renderFont == null ? renderFont = new FontUtil("Verdana",0,18) : renderFont;
    }
    public static FontUtil getSmallFont() {
        return smallFont == null ? smallFont = new FontUtil("Verdana",0,16) : smallFont;
    }
    public static FontUtil ui=null;
    public static FontUtil getUIFont() {
        return ui == null ? ui = new FontUtil("Segoe UI",0,14) : ui;
    }

    public static Block getBlock(double x, double y, double z){
        return mc().theWorld.getBlockState(new BlockPos(x,y,z)).getBlock();
    }

    public static Block getBlock(BlockPos pos) {
        return getBlock(pos.getX(),pos.getY(),pos.getZ());
    }

    public static TextHelper getFontHelper() {
        return text == null ? text = new TextHelper(mc().fontRendererObj) : text;
    }

    public static FontUtil getCourerer() {
        return courerer;
    }

    public static NahrFont getGuiFont() {
        return guiFont == null ? guiFont = new NahrFont("Gui",new Font("Segoe UI",18,18),18,0,true,true) : guiFont;
    }

    public static Block getBlockBelowPlayer(double height)
    {
        return getBlock(minecraft.thePlayer.posX, minecraft.thePlayer.posY - height, minecraft.thePlayer.posZ);
    }

    public static void damagePlayer()
    {
        for (int index = 0; index < 80; index++)
        {
            minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(minecraft.thePlayer.posX, minecraft.thePlayer.posY + 0.049D, minecraft.thePlayer.posZ, false));
            minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(minecraft.thePlayer.posX, minecraft.thePlayer.posY, minecraft.thePlayer.posZ, false));
        }

        player().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(player().posX, player().posY, player().posZ, true));
    }

    public static int getGroundDistance() {
        int dist = 0;
        for(int i = 0; i < 10; i++) {
            BlockPos blockP = new BlockPos(player().posX,player().posY - i,player().posZ);
            Block block = mc().theWorld.getBlockState(blockP).getBlock();
            if (block instanceof BlockAir || block instanceof BlockLiquid) {
                dist++;
            } else {
                return i;
            }
        }
        return dist;
    }

    private static FontUtil verdana = null;
    public static FontUtil getVerdana() {
        return verdana == null ? verdana = new FontUtil("Verdana",0,18) : verdana;
    }

    public static float getMiddleOfAngles(float yaw1, float yaw2) {
        return ((yaw1+yaw2)%360)/2;
    }


    public static Player player() {
        return minecraft.thePlayer;
    }

    public static FontUtil getSegaui() {
        return segaui == null ? segaui = new FontUtil("Segoe UI",Font.PLAIN,19) : segaui;
    }
    private static FontUtil segaui2 = null;
    public static FontUtil getSegaui2() {
        return segaui2 == null ? segaui2 = new FontUtil("Segoe UI",Font.PLAIN,20) : segaui2;
    }

    public static boolean MovementInput() {
        return Helper.mc().gameSettings.keyBindForward.getIsKeyPressed() || Helper.mc().gameSettings.keyBindBack.getIsKeyPressed() || Helper.mc().gameSettings.keyBindLeft.getIsKeyPressed() || Helper.mc().gameSettings.keyBindRight.getIsKeyPressed() || Helper.mc().gameSettings.keyBindSneak.getIsKeyPressed();
    }


    public static FontUtil getZonaPro() {
        return mainFont == null ? mainFont = new FontUtil("Zona Pro",Font.BOLD,20) : mainFont;
    }
    public static FontUtil getAlternativeFont() {
        return alternative == null ? alternative = new FontUtil("Walkway Rounded",0,21) : alternative;
    }

    public static FontUtil getComfortaFont() {
        return Comfortaa == null ? Comfortaa = new FontUtil("Comfortaa",0,18) : Comfortaa;
    }

    public FontUtil getCenturyFont() {
        return century == null ? century = new FontUtil("Century Gothic",0,20) : century;
    }


    public static void blinkToPos(double[] startPos, BlockPos endPos, double slack)
    {
        double curX = startPos[0];
        double curY = startPos[1];
        double curZ = startPos[2];
        double endX = endPos.getX() + 0.5D;
        double endY = endPos.getY() + 1.0D;
        double endZ = endPos.getZ() + 0.5D;

        double distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
        int count = 0;
        while (distance > slack)
        {
            distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
            if (count > 120) {
                break;
            }
            double diffX = curX - endX;
            double diffY = curY - endY;
            double diffZ = curZ - endZ;

            double offset = (count & 0x1) == 0 ? 0.4D : 0.25D;
            if (diffX < 0.0D) {
                if (Math.abs(diffX) > offset) {
                    curX += offset;
                } else {
                    curX += Math.abs(diffX);
                }
            }
            if (diffX > 0.0D) {
                if (Math.abs(diffX) > offset) {
                    curX -= offset;
                } else {
                    curX -= Math.abs(diffX);
                }
            }
            if (diffY < 0.0D) {
                if (Math.abs(diffY) > 0.25D) {
                    curY += 0.25D;
                } else {
                    curY += Math.abs(diffY);
                }
            }
            if (diffY > 0.0D) {
                if (Math.abs(diffY) > 0.25D) {
                    curY -= 0.25D;
                } else {
                    curY -= Math.abs(diffY);
                }
            }
            if (diffZ < 0.0D) {
                if (Math.abs(diffZ) > offset) {
                    curZ += offset;
                } else {
                    curZ += Math.abs(diffZ);
                }
            }
            if (diffZ > 0.0D) {
                if (Math.abs(diffZ) > offset) {
                    curZ -= offset;
                } else {
                    curZ -= Math.abs(diffZ);
                }
            }
            Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(curX, curY, curZ, true));
            count++;
        }
    }



}
