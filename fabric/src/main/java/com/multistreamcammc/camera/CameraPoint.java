package com.multistreamcammc.camera;
import net.minecraft.nbt.NbtCompound;import net.minecraft.registry.*;import net.minecraft.util.Identifier;import net.minecraft.util.math.BlockPos;import net.minecraft.world.World;
public record CameraPoint(String name,RegistryKey<World> dimension,BlockPos pos,float yaw,float pitch){
 public NbtCompound toNbt(){NbtCompound n=new NbtCompound();n.putString("name",name);n.putString("dimension",dimension.getValue().toString());n.putInt("x",pos.getX());n.putInt("y",pos.getY());n.putInt("z",pos.getZ());n.putFloat("yaw",yaw);n.putFloat("pitch",pitch);return n;}
 public static CameraPoint fromNbt(NbtCompound n){Identifier id=Identifier.tryParse(n.getString("dimension"));return new CameraPoint(n.getString("name"),RegistryKey.of(RegistryKeys.WORLD,id==null?World.OVERWORLD.getValue():id),new BlockPos(n.getInt("x"),n.getInt("y"),n.getInt("z")),n.getFloat("yaw"),n.getFloat("pitch"));}
}
