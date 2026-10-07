package com.multistreamcammc.camera;
import net.minecraft.nbt.*;import net.minecraft.server.MinecraftServer;import net.minecraft.world.*;import java.util.*;
public class CameraState extends PersistentState{
 public static final String KEY="multistreamcammc_cameras";private final Map<String,CameraPoint> cameras=new LinkedHashMap<>();
 public static CameraState get(MinecraftServer s){return s.getWorld(World.OVERWORLD).getPersistentStateManager().getOrCreate(CameraState::fromNbt,CameraState::new,KEY);}
 public Collection<CameraPoint> getAll(){return cameras.values();}public Optional<CameraPoint> get(String n){return Optional.ofNullable(cameras.get(n.toLowerCase(Locale.ROOT)));}
 public void put(CameraPoint p){cameras.put(p.name().toLowerCase(Locale.ROOT),p);markDirty();}public boolean remove(String n){if(cameras.remove(n.toLowerCase(Locale.ROOT))!=null){markDirty();return true;}return false;}
 public NbtCompound writeNbt(NbtCompound n){NbtList l=new NbtList();cameras.values().forEach(c->l.add(c.toNbt()));n.put("cameras",l);return n;}
 public static CameraState fromNbt(NbtCompound n){CameraState s=new CameraState();NbtList l=n.getList("cameras",10);for(int i=0;i<l.size();i++){CameraPoint p=CameraPoint.fromNbt(l.getCompound(i));s.cameras.put(p.name().toLowerCase(Locale.ROOT),p);}return s;}
}
