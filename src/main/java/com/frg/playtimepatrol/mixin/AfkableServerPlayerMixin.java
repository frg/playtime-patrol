package com.frg.playtimepatrol.mixin;

import com.frg.playtimepatrol.IAfkablePlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class AfkableServerPlayerMixin extends Entity implements IAfkablePlayer {
    @Shadow
    @Final
    public MinecraftServer server;
    public ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
    @Unique
    private boolean isAfk;

    public AfkableServerPlayerMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    public boolean isAfk() {
        return this.isAfk;
    }

    private void setAfk(boolean isAfk) {
        this.isAfk = isAfk;
        // TODO: Stop counting player active time
    }

    @Inject(method = "updateLastActionTime", at = @At("TAIL"))
    private void onActionTimeUpdate(CallbackInfo ci) {
        if (!isAfk) return;
        setAfk(false);
    }

    public void setPosition(double x, double y, double z) {
        // TODO: Make AFK time count towards active time configurable
        if (this.getX() != x || this.getY() != y || this.getZ() != z) {
            player.updateLastActionTime();
        }

        super.setPosition(x, y, z);
    }
}