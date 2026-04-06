package com.candle.delta_delight.util;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

public interface VoxelShapeHelper {
    static VoxelShape rotateShape(Direction facing,
                                  double minX, double minY, double minZ,
                                  double maxX, double maxY, double maxZ) {
        double x1 = minX, z1 = minZ, x2 = maxX, z2 = maxZ;

        switch (facing) {
            case SOUTH:
                x1 = 16 - maxX;
                z1 = 16 - maxZ;
                x2 = 16 - minX;
                z2 = 16 - minZ;
                break;
            case WEST:
                x1 = minZ;
                z1 = 16 - maxX;
                x2 = maxZ;
                z2 = 16 - minX;
                break;
            case EAST:
                x1 = 16 - maxZ;
                z1 = minX;
                x2 = 16 - minZ;
                z2 = maxX;
                break;
            case NORTH:
            default:
                break;
        }
        return Block.box(x1, minY, z1, x2, maxY, z2);
    }
}
