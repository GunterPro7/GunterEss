package com.GunterPro7uDerKatzenLord.mixin;

import com.GunterPro7uDerKatzenLord.LagHandler;
import com.GunterPro7uDerKatzenLord.event.ClientPacketEvent;
import com.GunterPro7uDerKatzenLord.listener.AdvancedChat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.*;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = NetHandlerPlayClient.class, priority = 2147483647)
public abstract class MixinNetHandlerPlayClient {

    private void handlePacket(Packet<?> packet, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new ClientPacketEvent(packet, ci));
    }

    @Inject(method = "handleSpawnObject", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleSpawnObjectInject(S0EPacketSpawnObject packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleSpawnExperienceOrb", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleSpawnExperienceOrbInject(S11PacketSpawnExperienceOrb packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleSpawnGlobalEntity", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleSpawnGlobalEntityInject(S2CPacketSpawnGlobalEntity packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleSpawnMob", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleSpawnMobInject(S0FPacketSpawnMob packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleScoreboardObjective", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleScoreboardObjectiveInject(S3BPacketScoreboardObjective packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleSpawnPainting", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleSpawnPaintingInject(S10PacketSpawnPainting packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleSpawnPlayer", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleSpawnPlayerInject(S0CPacketSpawnPlayer packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleAnimation", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleAnimationInject(S0BPacketAnimation packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleStatistics", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleStatisticsInject(S37PacketStatistics packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleBlockBreakAnim", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleBlockBreakAnimInject(S25PacketBlockBreakAnim packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleSignEditorOpen", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleSignEditorOpenInject(S36PacketSignEditorOpen packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleUpdateTileEntity", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleUpdateTileEntityInject(S35PacketUpdateTileEntity packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleBlockAction", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleBlockActionInject(S24PacketBlockAction packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleBlockChange", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleBlockChangeInject(S23PacketBlockChange packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleChat", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleChatInject(S02PacketChat packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleTabComplete", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleTabCompleteInject(S3APacketTabComplete packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleMultiBlockChange", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleMultiBlockChangeInject(S22PacketMultiBlockChange packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleMaps", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleMapsInject(S34PacketMaps packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleConfirmTransaction", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleConfirmTransactionInject(S32PacketConfirmTransaction packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleCloseWindow", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleCloseWindowInject(S2EPacketCloseWindow packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleWindowItems", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleWindowItemsInject(S30PacketWindowItems packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleOpenWindow", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleOpenWindowInject(S2DPacketOpenWindow packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleWindowProperty", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleWindowPropertyInject(S31PacketWindowProperty packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleSetSlot", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleSetSlotInject(S2FPacketSetSlot packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleCustomPayload", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleCustomPayloadInject(S3FPacketCustomPayload packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleDisconnect", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleDisconnectInject(S40PacketDisconnect packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleUseBed", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleUseBedInject(S0APacketUseBed packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleEntityStatus", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleEntityStatusInject(S19PacketEntityStatus packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleEntityAttach", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleEntityAttachInject(S1BPacketEntityAttach packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleExplosion", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleExplosionInject(S27PacketExplosion packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleChangeGameState", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleChangeGameStateInject(S2BPacketChangeGameState packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleKeepAlive", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleKeepAliveInject(S00PacketKeepAlive packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleChunkData", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleChunkDataInject(S21PacketChunkData packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleMapChunkBulk", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleMapChunkBulkInject(S26PacketMapChunkBulk packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleEffect", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleEffectInject(S28PacketEffect packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleJoinGame", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleJoinGameInject(S01PacketJoinGame packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleEntityMovement", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleEntityMovementInject(S14PacketEntity packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handlePlayerPosLook", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handlePlayerPosLookInject(S08PacketPlayerPosLook packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleParticles", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleParticlesInject(S2APacketParticles packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handlePlayerAbilities", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handlePlayerAbilitiesInject(S39PacketPlayerAbilities packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handlePlayerListItem", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handlePlayerListItemInject(S38PacketPlayerListItem packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleDestroyEntities", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleDestroyEntitiesInject(S13PacketDestroyEntities packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleRemoveEntityEffect", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleRemoveEntityEffectInject(S1EPacketRemoveEntityEffect packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleRespawn", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleRespawnInject(S07PacketRespawn packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleEntityHeadLook", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleEntityHeadLookInject(S19PacketEntityHeadLook packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleHeldItemChange", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleHeldItemChangeInject(S09PacketHeldItemChange packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleDisplayScoreboard", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleDisplayScoreboardInject(S3DPacketDisplayScoreboard packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleEntityMetadata", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleEntityMetadataInject(S1CPacketEntityMetadata packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleEntityVelocity", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleEntityVelocityInject(S12PacketEntityVelocity packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleEntityEquipment", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleEntityEquipmentInject(S04PacketEntityEquipment packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleSetExperience", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleSetExperienceInject(S1FPacketSetExperience packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleUpdateHealth", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleUpdateHealthInject(S06PacketUpdateHealth packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleTeams", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleTeamsInject(S3EPacketTeams packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleUpdateScore", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleUpdateScoreInject(S3CPacketUpdateScore packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleSpawnPosition", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleSpawnPositionInject(S05PacketSpawnPosition packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleTimeUpdate", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleTimeUpdateInject(S03PacketTimeUpdate packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleUpdateSign", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleUpdateSignInject(S33PacketUpdateSign packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleSoundEffect", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleSoundEffectInject(S29PacketSoundEffect packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleCollectItem", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleCollectItemInject(S0DPacketCollectItem packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleEntityTeleport", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleEntityTeleportInject(S18PacketEntityTeleport packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleEntityProperties", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleEntityPropertiesInject(S20PacketEntityProperties packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleEntityEffect", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleEntityEffectInject(S1DPacketEntityEffect packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleCombatEvent", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleCombatEventInject(S42PacketCombatEvent packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleServerDifficulty", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleServerDifficultyInject(S41PacketServerDifficulty packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleCamera", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleCameraInject(S43PacketCamera packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleWorldBorder", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleWorldBorderInject(S44PacketWorldBorder packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleTitle", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleTitleInject(S45PacketTitle packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleSetCompressionLevel", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleSetCompressionLevelInject(S46PacketSetCompressionLevel packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handlePlayerListHeaderFooter", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handlePlayerListHeaderFooterInject(S47PacketPlayerListHeaderFooter packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleResourcePack", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleResourcePackInject(S48PacketResourcePackSend packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }


    @Inject(method = "handleEntityNBT", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void handleEntityNBTInject(S49PacketUpdateEntityNBT packetIn, CallbackInfo ci) {
        this.handlePacket(packetIn, ci);
    }



}
