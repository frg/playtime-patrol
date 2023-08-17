package com.frg.playtimepatrol;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class PlaytimePatrol implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("com.frg.playtimepatrol");

	private static final HashMap<UUID, PlayerData> playerDataMap = new HashMap<>();
	private static int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("onInitialize");

		// Listen for block break events as a simple way to detect activity
		PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
			getPlayerData(player).incrementActiveTime();
		});

		// Listen for server ticks to periodically check player active time
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			int today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
			if (today != currentDay) {
				currentDay = today;
				playerDataMap.clear(); // Reset data for a new day
			}

			for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
				PlayerData data = getPlayerData(player);
				if (data.hasExceededLimit()) {
					player.networkHandler.disconnect(Text.of("You have exceeded your allowed active time for today!"));
				}
			}
		});
	}

	private static PlayerData getPlayerData(PlayerEntity player) {
		return playerDataMap.computeIfAbsent(player.getUuid(), k -> new PlayerData());
	}
}