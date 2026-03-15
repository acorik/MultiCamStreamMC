package com.multistreamcammc.client;

import com.multistreamcammc.MultiStreamCamMCMod;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

import java.util.List;

public class CameraQuickMenuScreen extends Screen {
    private final List<ClientCameraEntry> cameras;

    protected CameraQuickMenuScreen(List<ClientCameraEntry> cameras) {
        super(Text.literal("MultiStreamCamMC"));
        this.cameras = cameras;
    }

    @Override
    protected void init() {
        int y = 24;
        for (ClientCameraEntry camera : cameras) {
            this.addDrawableChild(ButtonWidget.builder(Text.literal(camera.label()), b -> goToCamera(camera.name()))
                    .dimensions(this.width / 2 - 140, y, 280, 20)
                    .build());
            y += 24;
            if (y > this.height - 40) {
                break;
            }
        }

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Cerrar"), b -> close())
                .dimensions(this.width / 2 - 60, this.height - 28, 120, 20)
                .build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 8, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }

    private void goToCamera(String name) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(name);
        ClientPlayNetworking.send(MultiStreamCamMCMod.CAMERA_GOTO_PACKET, buf);
        MinecraftClient.getInstance().setScreen(null);
    }
}
