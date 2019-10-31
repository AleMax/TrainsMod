package alemax.trainsmod;

import cam72cam.mod.ModCore;
import cam72cam.mod.ModEvent;

@net.minecraftforge.fml.common.Mod(modid = TrainsMod.MODID, name = TrainsMod.NAME, version = TrainsMod.VERSION, dependencies = "required-before:modcore", acceptedMinecraftVersions = "[1.12,1.13)")
public class TrainsMod extends ModCore.Mod {
    public static final String MODID = "trainsmod";
    public static final String NAME = "TrainsMod";
    public static final String VERSION = "0.2.0";

    static {
        try {
            Class<ModCore.Mod> cls = (Class<ModCore.Mod>) Class.forName("alemax.trainsmod.TrainsMod");
            ModCore.register(() -> {
                try {
                    return cls.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException("Could not construct mod " + MODID, e);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Could not load mod " + MODID, e);
        }
    }

    @Override
    public String modID() {
        return null;
    }

    @Override
    public void commonEvent(ModEvent event) {

    }

    @Override
    public void clientEvent(ModEvent event) {

    }

    @Override
    public void serverEvent(ModEvent event) {

    }
}

