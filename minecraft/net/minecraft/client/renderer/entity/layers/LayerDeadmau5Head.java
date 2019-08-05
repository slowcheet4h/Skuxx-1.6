package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;
import xyz.erik.skuxx.Skuxx;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LayerDeadmau5Head implements LayerRenderer
{
    private final RenderPlayer field_177208_a;
    private static final String __OBFID = "CL_00002421";
    public static List<String> ears = new ArrayList<>();
    public LayerDeadmau5Head(RenderPlayer p_i46119_1_)
    {
        this.field_177208_a = p_i46119_1_;
    }

    public void func_177207_a(AbstractClientPlayer p_177207_1_, float p_177207_2_, float p_177207_3_, float p_177207_4_, float p_177207_5_, float p_177207_6_, float p_177207_7_, float p_177207_8_)
    {
        if (((Skuxx.getSkuxxUser(p_177207_1_.getName()) != null && Skuxx.getSkuxxUser(p_177207_1_.getName()).isHaveEar()) || (p_177207_1_ instanceof EntityPlayerSP && Skuxx.getInstance().getSkuxxPlayer().isHaveEar())) && !p_177207_1_.isInvisible())
        {
            this.field_177208_a.bindTexture(p_177207_1_.getLocationSkin());

            for (int var9 = 0; var9 < 2; ++var9)
            {
                float var10 = p_177207_1_.prevRotationYaw + (p_177207_1_.rotationYaw - p_177207_1_.prevRotationYaw) * p_177207_4_ - (p_177207_1_.prevRenderYawOffset + (p_177207_1_.renderYawOffset - p_177207_1_.prevRenderYawOffset) * p_177207_4_);
                float var11 = p_177207_1_.prevRotationPitch + (p_177207_1_.rotationPitch - p_177207_1_.prevRotationPitch) * p_177207_4_;
                GlStateManager.pushMatrix();
                float addY = 0;
                if (p_177207_1_.isChild()) {
                    GL11.glScalef(0.5f,0.5f,0.5f);
                    addY = 1.3f;
                }

                GlStateManager.rotate(var10, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(var11, 1.0F, 0.0F, 0.0F);
                GlStateManager.translate(0.375F * (float)(var9 * 2 - 1), 0.0F, 0.0F);
                GlStateManager.translate(0.0F, -0.375F+addY, 0.0F);
                GlStateManager.rotate(-var11, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(-var10, 0.0F, 1.0F, 0.0F);
                float var12 = 1.2f;
                GlStateManager.scale(var12, var12, var12);
                this.field_177208_a.func_177136_g().func_178727_b(0.0625F);
                GlStateManager.popMatrix();
            }
        }
    }

    private boolean isHaveEar(String name) {
        for(String str : ears) {
            if (str.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
    //denicem
    private String getColor(String name) {
        for(String str : ears) {
            String tmp = str.split("::")[0];
            if (tmp.equalsIgnoreCase(name)) {
               return str.split("::")[1];
            }
        }
        return "black";
    }


    public boolean shouldCombineTextures()
    {
        return true;
    }

    public void doRenderLayer(EntityLivingBase p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_)
    {
        this.func_177207_a((AbstractClientPlayer)p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
    }
}
