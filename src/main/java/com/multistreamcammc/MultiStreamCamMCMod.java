package com.multistreamcammc;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.multistreamcammc.camera.CameraPoint;
import com.multistreamcammc.camera.CameraState;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MultiStreamCamMCMod implements ModInitializer {
    public static final String MOD_ID = "multistreamcammc";
    public static final Identifier CAMERA_SYNC_PACKET = new Identifier(MOD_ID, "camera_sync");
    public static final Identifier CAMERA_REQUEST_PACKET = new Identifier(MOD_ID, "camera_request");
    public static final Identifier CAMERA_GOTO_PACKET = new Identifier(MOD_ID, "camera_goto");

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
                CommandManager.literal("multistreamcam")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(CommandManager.literal("crear")
                                .then(CommandManager.argument("nombre", StringArgumentType.word())
                                        .executes(ctx -> {
                                            ServerCommandSource source = ctx.getSource();
                                            ServerPlayerEntity player = source.getPlayer();
                                            String name = StringArgumentType.getString(ctx, "nombre");
                                            BlockPos pos = player.getBlockPos();
                                            CameraPoint point = new CameraPoint(name, player.getWorld().getRegistryKey(), pos, player.getYaw(), player.getPitch());
                                            CameraState.get(source.getServer()).put(point);
                                            source.sendFeedback(() -> Text.literal("Cámara creada: " + name), true);
                                            return 1;
                                        })))
                        .then(CommandManager.literal("crear_en")
                                .then(CommandManager.argument("nombre", StringArgumentType.word())
                                        .then(CommandManager.argument("dimension", DimensionArgumentType.dimension())
                                                .executes(ctx -> {
                                                    ServerCommandSource source = ctx.getSource();
                                                    ServerPlayerEntity player = source.getPlayer();
                                                    String name = StringArgumentType.getString(ctx, "nombre");
                                                    ServerWorld world = DimensionArgumentType.getDimensionArgument(ctx, "dimension");
                                                    BlockPos pos = player.getBlockPos();
                                                    CameraPoint point = new CameraPoint(name, world.getRegistryKey(), pos, player.getYaw(), player.getPitch());
                                                    CameraState.get(source.getServer()).put(point);
                                                    source.sendFeedback(() -> Text.literal("Cámara creada en dimensión: " + name), true);
                                                    return 1;
                                                }))))
                        .then(CommandManager.literal("ir")
                                .then(CommandManager.argument("nombre", StringArgumentType.word())
                                        .executes(ctx -> gotoCamera(ctx.getSource(), StringArgumentType.getString(ctx, "nombre")))))
                        .then(CommandManager.literal("eliminar")
                                .then(CommandManager.argument("nombre", StringArgumentType.word())
                                        .executes(ctx -> {
                                            String name = StringArgumentType.getString(ctx, "nombre");
                                            boolean removed = CameraState.get(ctx.getSource().getServer()).remove(name);
                                            if (removed) {
                                                ctx.getSource().sendFeedback(() -> Text.literal("Cámara eliminada: " + name), true);
                                                return 1;
                                            }
                                            ctx.getSource().sendError(Text.literal("No existe la cámara: " + name));
                                            return 0;
                                        })))
                        .then(CommandManager.literal("listar")
                                .executes(ctx -> {
                                    CameraState state = CameraState.get(ctx.getSource().getServer());
                                    if (state.getAll().isEmpty()) {
                                        ctx.getSource().sendFeedback(() -> Text.literal("No hay cámaras creadas."), false);
                                        return 1;
                                    }
                                    state.getAll().forEach(cam -> ctx.getSource().sendFeedback(
                                            () -> Text.literal("- " + cam.name() + " @ " + cam.dimension().getValue() + " " + cam.pos().toShortString()), false));
                                    return state.getAll().size();
                                }))
        ));

        ServerPlayNetworking.registerGlobalReceiver(CAMERA_REQUEST_PACKET, (server, player, handler, buf, responseSender) ->
                server.execute(() -> sendCameras(player)));

        ServerPlayNetworking.registerGlobalReceiver(CAMERA_GOTO_PACKET, (server, player, handler, buf, responseSender) -> {
            String name = buf.readString();
            server.execute(() -> gotoCamera(player.getCommandSource(), name));
        });
    }

    private static int gotoCamera(ServerCommandSource source, String name) {
        CameraState state = CameraState.get(source.getServer());
        return state.get(name).map(cam -> {
            ServerPlayerEntity player;
            try {
                player = source.getPlayer();
            } catch (Exception ex) {
                source.sendError(Text.literal("Este comando requiere jugador."));
                return 0;
            }
            ServerWorld world = source.getServer().getWorld(cam.dimension());
            if (world == null) {
                source.sendError(Text.literal("La dimensión de la cámara no está disponible."));
                return 0;
            }
            player.teleport(world, cam.pos().getX() + 0.5, cam.pos().getY(), cam.pos().getZ() + 0.5, cam.yaw(), cam.pitch());
            source.sendFeedback(() -> Text.literal("Movido a cámara: " + cam.name()), false);
            return 1;
        }).orElseGet(() -> {
            source.sendError(Text.literal("No existe la cámara: " + name));
            return 0;
        });
    }

    private static void sendCameras(ServerPlayerEntity player) {
        PacketByteBuf out = PacketByteBufs.create();
        var cameras = CameraState.get(player.getServer()).getAll();
        out.writeVarInt(cameras.size());
        cameras.forEach(cam -> {
            out.writeString(cam.name());
            out.writeString(cam.dimension().getValue().toString());
            out.writeBlockPos(cam.pos());
            out.writeFloat(cam.yaw());
            out.writeFloat(cam.pitch());
        });
        ServerPlayNetworking.send(player, CAMERA_SYNC_PACKET, out);
    }
}
