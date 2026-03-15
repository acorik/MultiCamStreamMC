package com.multistreamcammc.camera;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class CameraState extends PersistentState {
    public static final String KEY = "multistreamcammc_cameras";
    private final Map<String, CameraPoint> cameras = new LinkedHashMap<>();

    public static CameraState get(MinecraftServer server) {
        PersistentStateManager manager = server.getWorld(World.OVERWORLD).getPersistentStateManager();
        return manager.getOrCreate(CameraState::fromNbt, CameraState::new, KEY);
    }

    public Collection<CameraPoint> getAll() {
        return cameras.values();
    }

    public Optional<CameraPoint> get(String name) {
        return Optional.ofNullable(cameras.get(name.toLowerCase()));
    }

    public boolean put(CameraPoint cameraPoint) {
        cameras.put(cameraPoint.name().toLowerCase(), cameraPoint);
        markDirty();
        return true;
    }

    public boolean remove(String name) {
        CameraPoint removed = cameras.remove(name.toLowerCase());
        if (removed != null) {
            markDirty();
            return true;
        }
        return false;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtList cameraList = new NbtList();
        cameras.values().forEach(camera -> cameraList.add(camera.toNbt()));
        nbt.put("cameras", cameraList);
        return nbt;
    }

    public static CameraState fromNbt(NbtCompound nbt) {
        CameraState state = new CameraState();
        NbtList cameraList = nbt.getList("cameras", 10);
        for (int i = 0; i < cameraList.size(); i++) {
            CameraPoint point = CameraPoint.fromNbt(cameraList.getCompound(i));
            state.cameras.put(point.name().toLowerCase(), point);
        }
        return state;
    }
}
