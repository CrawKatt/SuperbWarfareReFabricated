package com.atsuishio.superbwarfare.capability;

import com.atsuishio.superbwarfare.Mod;
import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class LaserCapability {

    public static ResourceLocation ID = Mod.loc("laser_capability");

    public interface ILaserCapability extends Component {

        void init(LaserHandler handler);

        void start();

        void tick();

        void stop();

        void end();
    }

    public static class LaserCapabilityImpl implements ILaserCapability {

        public LaserHandler laserHandler;

        @Override
        public void init(LaserHandler handler) {
            this.laserHandler = handler;
        }

        @Override
        public void start() {
            this.laserHandler.start();
        }

        @Override
        public void tick() {
        }

        @Override
        public void stop() {
            if (this.laserHandler != null) {
                this.laserHandler.stop();
            }
        }

        @Override
        public void end() {
            if (this.laserHandler != null) {
                this.laserHandler.end();
            }
        }

        @Override
        public void writeToNbt(CompoundTag tag) {
            if (this.laserHandler != null) {
                tag.put("Laser", this.laserHandler.writeNBT());
            }
        }

        @Override
        public void readFromNbt(CompoundTag nbt) {
            if (nbt.contains("Laser") && this.laserHandler != null) {
                this.laserHandler.readNBT(nbt.getCompound("Laser"));
            }
        }
    }
}