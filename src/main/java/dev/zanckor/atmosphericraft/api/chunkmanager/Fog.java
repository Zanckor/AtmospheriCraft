package dev.zanckor.atmosphericraft.api.chunkmanager;

/**
 * Record class for fog management
 * @param density - Quantity of density, if higher, player will see less
 * @param distance - Distance of fog, 0.5 per block
 */
public record Fog(float density, float distance) {
}
