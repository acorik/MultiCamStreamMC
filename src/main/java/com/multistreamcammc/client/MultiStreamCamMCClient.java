package com.multistreamcammc.client;

import com.multistreamcammc.MultiStreamCamMCMod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class MultiStreamCamMCClient implements ClientModInitializer {
    private static final List<ClientCameraEntry> CACHE = new ArrayList<>();
    private static KeyBinding menuKey;

    @Override
    public void onInitializeClient() {
        menuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.multistreamcammc.menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                "category.multistreamcammc"
        ));

        ClientPlayNetworking.registerGlobalReceiver(MultiStreamCamMCMod.CAMERA_SYNC_PACKET, (client, handler, buf, responseSender) -> {
            List<ClientCameraEntry> entries = new ArrayList<>();
            int size = buf.readVarInt();
            for (int i = 0; i < size; i++) {
                entries.add(new ClientCameraEntry(
                        buf.readString(),
                        buf.readString(),
                        buf.readBlockPos(),
                        buf.readFloat(),
                        buf.readFloat()
                ));
            }
            client.execute(() -> {
                CACHE.clear();
                CACHE.addAll(entries);
                client.setScreen(new CameraQuickMenuScreen(CACHE));
            });
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (menuKey.wasPressed()) {
                requestCameras(client);
            }
        });
    }

    private static void requestCameras(MinecraftClient client) {
        ClientPlayNetworking.send(MultiStreamCamMCMod.CAMERA_REQUEST_PACKET, PacketByteBufs.empty());
        if (client.player != null && ObsDetector.isObsWebsocketReachable()) {
            client.player.sendMessage(Text.literal("OBS detectado en puerto 4455 (obs-websocket)."), true);
        }
    }
}
