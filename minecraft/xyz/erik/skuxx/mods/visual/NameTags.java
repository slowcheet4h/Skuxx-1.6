package xyz.erik.skuxx.mods.visual;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import optifine.MathUtils;
import org.lwjgl.opengl.GL11;
import xyz.erik.api.config.vals.Bool;
import xyz.erik.api.config.vals.Double;
import xyz.erik.api.font.FontUtil;
import xyz.erik.api.friend.FriendManager;
import xyz.erik.api.helpers.Helper;
import xyz.erik.api.mod.Mod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Session;
import xyz.erik.api.utils.FlexibleArray;
import xyz.erik.api.utils.RenderUtil;
import xyz.erik.skuxx.Skuxx;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.*;
import xyz.erik.skuxx.mods.Category;

import java.text.DecimalFormat;
import java.util.Iterator;

public class NameTags
        extends Mod {

    private boolean health = true;

    private boolean armor = true;

    private boolean ping = true;
    private RenderItem itemRender;
    private Double scaling = new Double("Scale",0.003,0.001,0.01);
    private Double width = new Double("Width",1.6,1.0,5);
    public NameTags() {
        this.mc = Minecraft.getMinecraft();
        setCategory(Category.VISUAL);
    }

    Minecraft mc;

    @EventTarget
    private void renderName(NametagRenderEvent event) {
        event.setCancelled(true);
    }


    @EventTarget
    private void specialRender(SpecialRenderEvent event) {
        if (getPlayer() != null)
            for(Object o : getWorld().playerEntities) {
                Entity entity = (Entity)o;
                if (entity instanceof EntityPlayer && entity != getPlayer()) {
                    double x = NameTags.this.interpolate(entity.lastTickPosX, entity.posX, event.getPartialTicks()) - NameTags.this.minecraft.getRenderManager().renderPosX;
                    double y = NameTags.this.interpolate(entity.lastTickPosY, entity.posY, event.getPartialTicks()) - NameTags.this.minecraft.getRenderManager().renderPosY;
                    double z = NameTags.this.interpolate(entity.lastTickPosZ, entity.posZ, event.getPartialTicks()) - NameTags.this.minecraft.getRenderManager().renderPosZ;
                    NameTags.this.renderNameTag((EntityPlayer)entity, x, y, z, event.getPartialTicks());

                }
            }
    }


    private void renderNameTag(EntityPlayer player, double x, double y, double z, float delta)
    {
        double tempY = y;
        tempY += (player.isSneaking() ? 0.5D : 0.7D);

        Entity camera = this.minecraft.getRenderViewEntity();
        double originalPositionX = camera.posX;
        double originalPositionY = camera.posY;
        double originalPositionZ = camera.posZ;
        camera.posX = interpolate(camera.prevPosX, camera.posX, delta);
        camera.posY = interpolate(camera.prevPosY, camera.posY, delta);
        camera.posZ = interpolate(camera.prevPosZ, camera.posZ, delta);

        double distance = camera.getDistance(x + this.minecraft.getRenderManager().viewerPosX, y + this.minecraft.getRenderManager().viewerPosY, z + this.minecraft.getRenderManager().viewerPosZ);
        int width = this.minecraft.fontRendererObj.getStringWidth(getDisplayName(player)) / 2;
        double scale = 0.0018D + (float)scaling.getValue() * distance;
        if (distance <= 8.0D) {
            scale = 0.0245D;
        }
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0F, -1500000.0F);
        GlStateManager.disableLighting();
        GlStateManager.translate((float)x, (float)tempY + 1.4F, (float)z);
        GlStateManager.rotate(-this.minecraft.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(this.minecraft.getRenderManager().playerViewX, this.minecraft.gameSettings.thirdPersonView == 2 ? -1.0F : 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(-scale, -scale, scale);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();

        GlStateManager.disableAlpha();
        RenderUtil.drawBorderedRectReliant(-width - 2, -(this.minecraft.fontRendererObj.FONT_HEIGHT + 1), width + 2.0F, 1.5F, (float)this.width.getValue(), 1996488704, 1426063360);
        GlStateManager.enableAlpha();

        this.minecraft.fontRendererObj.drawStringWithShadow(getDisplayName(player), -width, -(this.minecraft.fontRendererObj.FONT_HEIGHT - 1), getDisplayColour(player));
        if (armor)
        {
            GlStateManager.pushMatrix();
            int xOffset = 0;
            for (int index = 3; index >= 0; index--)
            {
                ItemStack stack = player.inventory.armorInventory[index];
                if (stack != null) {
                    xOffset -= 8;
                }
            }
            if (player.getCurrentEquippedItem() != null)
            {
                xOffset -= 8;
                ItemStack renderStack = player.getCurrentEquippedItem().copy();
                if ((renderStack.hasEffect()) && (((renderStack.getItem() instanceof ItemTool)) || ((renderStack.getItem() instanceof ItemArmor)))) {
                    renderStack.stackSize = 1;
                }
                renderItemStack(renderStack, xOffset, -26);
                xOffset += 16;
            }
            for (int index = 3; index >= 0; index--)
            {
                ItemStack stack = player.inventory.armorInventory[index];
                if (stack != null)
                {
                    ItemStack armourStack = stack.copy();
                    if ((armourStack.hasEffect()) && (((armourStack.getItem() instanceof ItemTool)) || ((armourStack.getItem() instanceof ItemArmor)))) {
                        armourStack.stackSize = 1;
                    }
                    renderItemStack(armourStack, xOffset, -26);
                    xOffset += 16;
                }
            }
            GlStateManager.popMatrix();
        }
        camera.posX = originalPositionX;
        camera.posY = originalPositionY;
        camera.posZ = originalPositionZ;

        GlStateManager.enableDepth();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.disablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0F, 1500000.0F);
        GlStateManager.popMatrix();
    }

    private void renderItemStack(ItemStack stack, int x, int y)
    {
        GlStateManager.pushMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.clear(256);

        RenderHelper.enableStandardItemLighting();
        this.minecraft.getRenderItem().zLevel = -150.0F;

        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableAlpha();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableAlpha();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        this.minecraft.getRenderItem().renderItemIntoGUI(stack, x, y);
        this.minecraft.getRenderItem().renderItemOverlays(this.minecraft.fontRendererObj, stack, x, y);

        this.minecraft.getRenderItem().zLevel = 0.0F;
        RenderHelper.disableStandardItemLighting();

        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        GlStateManager.disableDepth();
        renderEnchantmentText(stack, x, y);
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        GlStateManager.popMatrix();
    }

    private void renderEnchantmentText(ItemStack stack, int x, int y)
    {
        int enchantmentY = y - 24;
        if ((stack.getEnchantmentTagList() != null) && (stack.getEnchantmentTagList().tagCount() >= 6))
        {
            this.minecraft.fontRendererObj.drawString("god", x * 2, enchantmentY, -43177);
            return;
        }
        int color = -5592406;
        if ((stack.getItem() instanceof ItemArmor))
        {
            int protectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180310_c.effectId, stack);
            int projectileProtectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180308_g.effectId, stack);
            int blastProtectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack);
            int fireProtectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack);
            int thornsLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack);
            int featherFallingLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180309_e.effectId, stack);
            int unbreakingLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
            if (protectionLevel > 0)
            {
                this.minecraft.fontRendererObj.drawString("pr" + protectionLevel, x * 2, enchantmentY, color);
                enchantmentY += 8;
            }
            if (unbreakingLevel > 0)
            {
                this.minecraft.fontRendererObj.drawString("un" + unbreakingLevel, x * 2, enchantmentY, color);
                enchantmentY += 8;
            }
            if (projectileProtectionLevel > 0)
            {
                this.minecraft.fontRendererObj.drawString("pp" + projectileProtectionLevel, x * 2, enchantmentY, color);
                enchantmentY += 8;
            }
            if (blastProtectionLevel > 0)
            {
                this.minecraft.fontRendererObj.drawString("bp" + blastProtectionLevel, x * 2, enchantmentY, color);
                enchantmentY += 8;
            }
            if (fireProtectionLevel > 0)
            {
                this.minecraft.fontRendererObj.drawString("fp" + fireProtectionLevel, x * 2, enchantmentY, color);
                enchantmentY += 8;
            }
            if (thornsLevel > 0)
            {
                this.minecraft.fontRendererObj.drawString("tho" + thornsLevel, x * 2, enchantmentY, color);
                enchantmentY += 8;
            }
            if (featherFallingLevel > 0)
            {
                this.minecraft.fontRendererObj.drawString("ff" + featherFallingLevel, x * 2, enchantmentY, color);
                enchantmentY += 8;
            }
            if (this.armor &&
                    (stack.getMaxDamage() - stack.getItemDamage() < stack.getMaxDamage()))
            {
                this.minecraft.fontRendererObj.drawString(stack.getMaxDamage() - stack.getItemDamage() + "", x * 2, enchantmentY + 2, 39321);
                enchantmentY += 8;
            }
        }
        if ((stack.getItem() instanceof ItemBow))
        {
            int powerLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
            int punchLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
            int flameLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack);
            if (powerLevel > 0)
            {
                this.minecraft.fontRendererObj.drawString("po" + powerLevel, x * 2, enchantmentY, color);
                enchantmentY += 8;
            }
            if (punchLevel > 0)
            {
                this.minecraft.fontRendererObj.drawString("pu" + punchLevel, x * 2, enchantmentY, color);
                enchantmentY += 8;
            }
            if (flameLevel > 0)
            {
                this.minecraft.fontRendererObj.drawString("fl" + flameLevel, x * 2, enchantmentY, color);
                enchantmentY += 8;
            }
        }
        if ((stack.getItem() instanceof ItemPickaxe))
        {
            int efficiencyLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack);
            int fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, stack);
            if (efficiencyLevel > 0)
            {
                this.minecraft.fontRendererObj.drawString("ef" + efficiencyLevel, x * 2, enchantmentY, color);
                enchantmentY += 8;
            }
            if (fortuneLevel > 0)
            {
                this.minecraft.fontRendererObj.drawString("fo" + fortuneLevel, x * 2, enchantmentY, color);
                enchantmentY += 8;
            }
        }
        if ((stack.getItem() instanceof ItemAxe))
        {
            int sharpnessLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180314_l.effectId, stack);
            int fireAspectLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180313_o.effectId, stack);
            int efficiencyLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack);
            if (sharpnessLevel > 0)
            {
                this.minecraft.fontRendererObj.drawString("sh" + sharpnessLevel, x * 2, enchantmentY, color);
                enchantmentY += 8;
            }
            if (fireAspectLevel > 0)
            {
                this.minecraft.fontRendererObj.drawString("fa" + fireAspectLevel, x * 2, enchantmentY, color);
                enchantmentY += 8;
            }
            if (efficiencyLevel > 0)
            {
                this.minecraft.fontRendererObj.drawString("ef" + efficiencyLevel, x * 2, enchantmentY, color);
                enchantmentY += 8;
            }
        }
        if ((stack.getItem() instanceof ItemSword))
        {
            int sharpnessLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180314_l.effectId, stack);
            int knockbackLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180313_o.effectId, stack);
            int fireAspectLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
            if (sharpnessLevel > 0)
            {
                this.minecraft.fontRendererObj.drawString("sh" + sharpnessLevel, x * 2, enchantmentY, color);
                enchantmentY += 8;
            }
            if (knockbackLevel > 0)
            {
                this.minecraft.fontRendererObj.drawString("kn" + knockbackLevel, x * 2, enchantmentY, color);
                enchantmentY += 8;
            }
            if (fireAspectLevel > 0)
            {
                this.minecraft.fontRendererObj.drawString("fa" + fireAspectLevel, x * 2, enchantmentY, color);
                enchantmentY += 8;
            }
        }
        if ((stack.getItem() == Items.golden_apple) && (stack.hasEffect())) {
            this.minecraft.fontRendererObj.drawStringWithShadow("god", x * 2, enchantmentY, -3977919);
        }
    }

    private String getDisplayName(EntityPlayer player)
    {
        String name = player.getDisplayName().getFormattedText();

        String heartUnicode = " ❤";
        if (Skuxx.getInstance().getFriendManager().isFriend(player.getName())) {
            name = Skuxx.getInstance().getFriendManager().getFriend(player.getName()).getAlias();
        }
        if (name.contains(this.minecraft.getSession().getUsername())) {
            name = "You";
        }
        if (!health) {
            return name;
        }
        float health = player.getHealth();
        EnumChatFormatting color;
        if (health > 18.0F)
        {
            color = EnumChatFormatting.GREEN;
        }
        else
        {
            if (health > 16.0F)
            {
                color = EnumChatFormatting.DARK_GREEN;
            }
            else
            {
                if (health > 12.0F)
                {
                    color = EnumChatFormatting.YELLOW;
                }
                else
                {
                    if (health > 8.0F)
                    {
                        color = EnumChatFormatting.GOLD;
                    }
                    else
                    {
                        if (health > 5.0F) {
                            color = EnumChatFormatting.RED;
                        } else {
                            color = EnumChatFormatting.DARK_RED;
                        }
                    }
                }
            }
        }

        if (Math.floor(health) == health) {
            name = name + color + " " + (health > 0.0F ? Integer.valueOf((int)Math.floor(health)) : "dead");
        } else {
            name = name + color + " " + (health > 0.0F ? Integer.valueOf((int)health) : "dead");
        }

        return name + heartUnicode;
    }

    private int getDisplayColour(EntityPlayer player)
    {
        int colour = -5592406;
        if (Skuxx.getInstance().getFriendManager().isFriend(player.getName())) {
            return -11157267;
        }
        if (player.isInvisible()) {
            colour = -1113785;
        } else if (player.isSneaking()) {
            colour = -6481515;
        }
        return colour;
    }
    /*  @EventTarget
      private void onNametagRender(NametagRenderEvent event)
      {
          this.itemRender = new RenderItem(this.mc.renderEngine, this.mc.modelManager);
          Character colorFormatCharacter = new Character('§');
          EntityOtherPlayerMP entityIn = event.entity;
          float distance = Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entityIn);
          float scaleFactor = distance < 10.0F ? 0.9F : distance / 11.11F;
          int color = 16777215;
          float height = 0.0F;
          String str = event.string;
          if ((entityIn instanceof EntityPlayer))
          {
              EntityPlayer player = entityIn;
              if (player.getHealth() > 16.0F) {
                  str = str + " | " + colorFormatCharacter + "2" + MathUtils.round(player.getHealth(), 1);
              } else if (player.getHealth() > 8.0F) {
                  str = str + " | " + colorFormatCharacter + "6" + MathUtils.round(player.getHealth(), 1);
              } else {
                  str = str + " | " + colorFormatCharacter + "4" + MathUtils.round(player.getHealth(), 1);
              }
              if (!this.health) {
                  str = Skuxx.getInstance().getFriendManager().isFriend(player.getDisplayName().getUnformattedText()) ? entityIn.getDisplayName().getUnformattedText() : entityIn.getDisplayName().getFormattedText();
              }
              if (Skuxx.getInstance().getFriendManager().isFriend(player.getDisplayName().getUnformattedText()))
              {
                  color = -9183489;
                  str = colorFormatCharacter + "r" + Skuxx.getInstance().getFriendManager().replace(str);
              }
              if (player.isSneaking()) {
                  str = str + " " + colorFormatCharacter + "6[S]";
              }
              if (player.isInvisible()) {
                  str = str + " " + colorFormatCharacter + "6[I]";
              }
              if (this.ping) {
                  str = str + " " + colorFormatCharacter + "7[" +(int)entityIn.getDistanceToEntity(getPlayer()) + "]";
              }
              if (distance >= 10.0F) {
                  height = (float)(height + (distance / 40.0F - 0.25D));
              }
          }
          FontRenderer var12 = mc.fontRendererObj;
          float var13 = 1.6F;
          float var14 = 0.01666667F * var13;
          GL11.glPushMatrix();
          GL11.glEnable(32823);
          GL11.glPolygonOffset(1.0F, -1000000.0F);
          Minecraft.getMinecraft().entityRenderer.disableLightmap();
          GL11.glTranslatef((float)event.x + 0.0F, (float)event.y + entityIn.height + 0.5F + height, (float)event.z);
          GL11.glNormal3f(0.0F, 1.0F, 0.0F);
          GL11.glRotatef(-this.mc.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
          GL11.glRotatef(this.mc.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
          GL11.glScalef(-var14 * scaleFactor, -var14 * scaleFactor, var14 * scaleFactor);
          GL11.glDisable(2912);
          if (this.armor) {
              renderArmor(entityIn);
          }
          GL11.glDisable(2896);
          GL11.glDepthMask(false);
          GL11.glDisable(2929);
          GL11.glEnable(3042);
          OpenGlHelper.glBlendFunc(770, 771, 1, 0);
          WorldRenderer var15 = Tessellator.getInstance().getWorldRenderer();
          GL11.glDisable(3553);
          var15.startDrawingQuads();
          int var17 = var12.getStringWidth(str) / 2;
          var15.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);

          GL11.glLineWidth(0.3F);
          GL11.glColor3f(0.0F, 0.0F, 0.0F);

          GL11.glBegin(3);
          GL11.glVertex2d(-var17 - 2, -2.0D);
          GL11.glVertex2d(-var17 - 2, 10.0D);
          GL11.glEnd();

          GL11.glBegin(3);
          GL11.glVertex2d(-var17 - 2, 10.0D);
          GL11.glVertex2d(var17 + 2, 10.0D);
          GL11.glEnd();

          GL11.glBegin(3);
          GL11.glVertex2d(var17 + 2, 10.0D);
          GL11.glVertex2d(var17 + 2, -2.0D);
          GL11.glEnd();

          GL11.glBegin(3);
          GL11.glVertex2d(var17 + 2, -2.0D);
          GL11.glVertex2d(-var17 - 2, -2.0D);
          GL11.glEnd();

          var15.addVertex(-var17 - 2, -2.0D, 0.0D);
          var15.addVertex(-var17 - 2, 10.0D, 0.0D);
          var15.addVertex(var17 + 2, 10.0D, 0.0D);
          var15.addVertex(var17 + 2, -2.0D, 0.0D);
          Tessellator.getInstance().draw();
          GL11.glEnable(3553);
         // var12.drawStringWithShadow(str, -var17, 0.0F, color);
          GL11.glEnable(2929);
          GL11.glDepthMask(true);
          //var12.drawString(str, -var17, 0, color);
          var12.drawStringWithShadow(entityIn.getName(),-var17,0,color);
          var12.drawStringWithShadow((int)entityIn.getHealth()  + "",-var17 + var12.getStringWidth(entityIn.getName()) + 3,0,entityIn.getHealth()  < 10 ? 0xff0000 : 0x00ff00);
          var12.drawStringWithShadow("("+Skuxx.getInstance().getNetworkUtils().getPlayerPing(entityIn.getName()) + ")",-var17 + var12.getStringWidth(entityIn.getName())  + var12.getStringWidth((int)entityIn.getHealth() + "") + 5,0,0xd3d3d3);

          GL11.glEnable(2896);
          GL11.glDisable(3042);
          GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
          Minecraft.getMinecraft().entityRenderer.enableLightmap();
          GL11.glDisable(32823);
          GL11.glPolygonOffset(1.0F, 1000000.0F);
          GL11.glPopMatrix();

          event.setCancelled(true);
      }

      public  void rendertheitemsandshit(ItemStack stack, int x, int y)
      {
          GL11.glPushMatrix();
          GL11.glDepthMask(true);
          GlStateManager.clear(256);
          RenderHelper.enableStandardItemLighting();
          getMinecraft().getRenderItem().zLevel = -100.0F;
          GlStateManager.scale(1.0F, 1.0F, 0.01F);
          mc.getRenderItem().memes(stack, x, y + 8);
          mc.getRenderItem().func_175030_a(mc.fontRendererObj, stack, x, y + 8);
          mc.getRenderItem().zLevel = 0.0F;
          GlStateManager.scale(1.0F, 1.0F, 1.0F);
          RenderHelper.disableStandardItemLighting();
          GlStateManager.disableCull();
          GlStateManager.enableAlpha();
          GlStateManager.disableBlend();
          GlStateManager.disableLighting();
          GlStateManager.scale(0.5D, 0.5D, 0.5D);
          GlStateManager.disableDepth();
          renderEnchantText(stack, x, y);
          GlStateManager.enableDepth();
          GlStateManager.scale(2.0F, 2.0F, 2.0F);
          GL11.glPopMatrix();
      }

      public staticx void renderEnchantText(ItemStack stack, int x, int y)
      {
          int encY = y - 24;
          if ((stack.getItem() instanceof ItemArmor)) {
              Helper.getArrayFont().drawString(stack.getMaxDamage() - stack.getItemDamage() + "", x * 2 + 8, y + 15, -1);
          }
          NBTTagList enchants = stack.getEnchantmentTagList();
          if (enchants != null)
          {
              if (enchants.tagCount() >= 5)
              {
                  Helper.getArrayFont().drawStringWithShadow( "God", x * 2, encY + 15, -49088);
                  return;
              }
              for (int index = 0; index < enchants.tagCount(); index++)
              {
                  short id = enchants.getCompoundTagAt(index).getShort("id");
                  short level = enchants.getCompoundTagAt(index).getShort("lvl");
                  Enchantment enc = Enchantment.func_180306_c(id);
                  if (enc != null)
                  {
                      String encName = enc.getTranslatedName(level).substring(0, 1).toLowerCase();
                      encName = encName + level;
                      GL11.glPushMatrix();
                      GL11.glScalef(0.9F, 0.9F, 0.0F);
                      Helper.getArrayFont().drawString(encName, x * 2, encY + 4, -1);
                      GL11.glScalef(1.0F, 1.0F, 1.0F);
                      GL11.glPopMatrix();
                      encY += 8;
                  }
              }
          }
      }
      public float getNametagSize(EntityLivingBase player)
      {

          float dist = getPlayer().getDistanceToEntity(player) / 2.0F;
          float size = dist <= 2.0F ? 2.2F : dist;
          return size;
      }
      public void renderArmor(EntityPlayer player)
      {
          byte var16 = 0;
          if (player.isSneaking()) {
              var16 = 4;
          }
          FlexibleArray<Byte> armor = new FlexibleArray();
          ItemStack heldStack = player.inventory.getCurrentItem();
          if (heldStack != null) {
              armor.add(Byte.valueOf((byte)36));
          }
          for (byte b = 8; b >= 5; b = (byte)(b - 1))
          {
              ItemStack armorPiece = player.inventoryContainer.getSlot(b).getStack();
              if (armorPiece != null) {
                  armor.add(Byte.valueOf(b));
              }
          }
          int x = -(armor.size() * 8);
          for (Iterator it = armor.iterator(); it.hasNext();)
          {
              byte b = ((Byte)it.next()).byteValue();

              ItemStack stack = player.inventoryContainer.getSlot(b).getStack();
              String text = "";
              if (stack != null)
              {
                  int y = 0;
                  int sLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180314_l.effectId, stack);
                  int fLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
                  int kLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180313_o.effectId, stack);
                  if (sLevel > 0)
                  {
                      GL11.glDisable(2896);
                      drawSmallString("Sh" + sLevel, x, y);
                      y -= 9;
                  }
                  if (fLevel > 0)
                  {
                      GL11.glDisable(2896);
                      drawSmallString("Fir" + fLevel, x, y);
                      y -= 9;
                  }
                  if (kLevel > 0)
                  {
                      GL11.glDisable(2896);
                      drawSmallString("Kb" + kLevel, x, y);
                  }
                  else if ((stack.getItem() instanceof ItemArmor))
                  {
                      int pLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180310_c.effectId, stack);
                      int tLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack);
                      int uLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
                      if (pLevel > 0)
                      {
                          GL11.glDisable(2896);
                          drawSmallString("P" + pLevel, x, y);
                          y -= 9;
                      }
                      if (tLevel > 0)
                      {
                          GL11.glDisable(2896);
                          drawSmallString("Th" + tLevel, x, y);
                          y -= 9;
                      }
                      if (uLevel > 0)
                      {
                          GL11.glDisable(2896);
                          drawSmallString("Unb" + uLevel, x, y);
                      }
                  }
                  else if ((stack.getItem() instanceof ItemBow))
                  {
                      int powLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
                      int punLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
                      int fireLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack);
                      if (powLevel > 0)
                      {
                          GL11.glDisable(2896);
                          drawSmallString("Pow" + powLevel, x, y);
                          y -= 9;
                      }
                      if (punLevel > 0)
                      {
                          GL11.glDisable(2896);
                          drawSmallString("Pun" + punLevel, x, y);
                          y -= 9;
                      }
                      if (fireLevel > 0)
                      {
                          GL11.glDisable(2896);
                          drawSmallString("Fir" + fireLevel, x, y);
                      }
                  }
                  else if ((stack.getItem() instanceof ItemAppleGold))
                  {
                      if (stack.getItem().getRarity(stack) == EnumRarity.EPIC) {
                          text = text + "God";
                      }
                  }
                  this.itemRender.zLevel = -100.0F;
                  this.itemRender.renderItemIntoGUI(player.inventoryContainer.getSlot(b).getStack(), x, -18);
                  Minecraft.getMinecraft().getRenderItem().renderItemOverlays(Minecraft.getMinecraft().fontRendererObj, player.inventoryContainer.getSlot(b).getStack(), x, -18);
                  this.itemRender.zLevel = 0.0F;
                  x += 16;
              }
          }
      }

      private static void drawSmallString(String text, int x, int y)
      {
          x *= 2;
          //TODO?CHANTGE FONT
          GL11.glScalef(0.5F, 0.5F, 0.5F);
          Helper.getArrayFont().drawStringWithShadow(text, x, -36 - y, 64250);
          GL11.glScalef(2.0F, 2.0F, 2.0F);
      }
  */
    private double interpolate(double previous, double current, float delta)
    {
        return previous + (current - previous) * delta;
    }

}
