package com.multistreamcammc.camera;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public record CameraPoint(String name, RegistryKey<World> dimension, BlockPos pos, float yaw, float pitch) {
    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.putString("name", name);
        nbt.putString("dimension", dimension.getValue().toString());
        nbt.putInt("x", pos.getX());
        nbt.putInt("y", pos.getY());
        nbt.putInt("z", pos.getZ());
        nbt.putFloat("yaw", yaw);
        nbt.putFloat("pitch", pitch);
        return nbt;
    }

    public static CameraPoint fromNbt(NbtCompound nbt) {
        String name = nbt.getString("name");
        Identifier dimensionId = Identifier.tryParse(nbt.getString("dimension"));
        RegistryKey<World> dimension = RegistryKey.of(RegistryKeys.WORLD, dimensionId == null ? World.OVERWORLD.getValue() : dimensionId);
        BlockPos pos = new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"));
        float yaw = nbt.getFloat("yaw");
        float pitch = nbt.getFloat("pitch");
        return new CameraPoint(name, dimension, pos, yaw, pitch);
    }
}
