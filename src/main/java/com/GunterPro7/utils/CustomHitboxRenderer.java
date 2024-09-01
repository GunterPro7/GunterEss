package com.GunterPro7.utils;

import com.GunterPro7.listener.Listener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

import static com.GunterPro7.Main.mc;

public class CustomHitboxRenderer implements Listener {
    private static final CustomHitboxRenderer instance = new CustomHitboxRenderer();
    private CustomHitboxRenderer() {}

    public static CustomHitboxRenderer getInstance() {
        return instance;
    }

    public void renderHitboxAroundPlayer(EntityPlayer player, float partialTicks) {
        double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

        double dx = x - mc.getRenderManager().viewerPosX;
        double dy = y - mc.getRenderManager().viewerPosY;
        double dz = z - mc.getRenderManager().viewerPosZ;

        AxisAlignedBB boundingBox = new AxisAlignedBB(dx - 0.5, dy, dz - 0.5, dx + 0.5, dy + 2.0, dz + 0.5);

        renderHitbox(boundingBox, new float[]{1.0F, 0.0F, 0.0F, 1.0F});
    }

    public void renderHitboxAroundItem(EntityItem item, float partialTicks) {
        renderHitbox(item.getEntityBoundingBox(), new float[]{0.0F, 1.0F, 0.0F, 1.0F});
    }

    private void renderHitbox(AxisAlignedBB boundingBox, float[] color) {
        Tessellator tessellator = Tessellator.getInstance();
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableLighting();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableDepth();
        GlStateManager.color(color[0], color[1], color[2], color[3]);

        tessellator.getWorldRenderer().begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);
        drawBoundingBox(tessellator, boundingBox);
        tessellator.draw();

        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        GlStateManager.enableLighting();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.disableBlend();
    }

    public void drawBoundingBox(Tessellator tessellator, AxisAlignedBB boundingBox) {
        tessellator.getWorldRenderer().pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        tessellator.getWorldRenderer().pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        tessellator.getWorldRenderer().pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        tessellator.getWorldRenderer().pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        tessellator.getWorldRenderer().pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();

        tessellator.getWorldRenderer().pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        tessellator.getWorldRenderer().pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        tessellator.getWorldRenderer().pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        tessellator.getWorldRenderer().pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        tessellator.getWorldRenderer().pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();

        tessellator.getWorldRenderer().pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        tessellator.getWorldRenderer().pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        tessellator.getWorldRenderer().pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        tessellator.getWorldRenderer().pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        tessellator.getWorldRenderer().pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        tessellator.getWorldRenderer().pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        tessellator.getWorldRenderer().pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        tessellator.getWorldRenderer().pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
    }
}
