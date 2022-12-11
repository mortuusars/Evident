package io.github.mortuusars.evident.foundation.helper;

import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import org.lwjgl.system.CallbackI;

public class Pos {
    public static Vector3f center(BlockPos pos) {
        return new Vector3f(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f);
    }

    public static Vector3f center(BlockPos pos, float offsetX, float offsetY, float offsetZ) {
        return new Vector3f(pos.getX() + 0.5f + offsetX, pos.getY() + 0.5f + offsetY, pos.getZ() + 0.5f + offsetZ);
    }
}
