package alemax.trainsmod.networking;

import alemax.trainsmod.TrainsMod;
import net.minecraft.util.Identifier;

public abstract class TMPacket {

    public final Identifier identifier;

    public TMPacket(String identifier) {
        this.identifier = new Identifier(TrainsMod.modid, identifier);
    }

    public abstract void register();

}
