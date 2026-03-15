package com.multistreamcammc.client;

import net.minecraft.util.math.BlockPos;

public record ClientCameraEntry(String name, String dimension, BlockPos pos, float yaw, float pitch) {
    public String label() {
        return name + "  [" + dimension + "]  " + pos.toShortString();
    }
}
