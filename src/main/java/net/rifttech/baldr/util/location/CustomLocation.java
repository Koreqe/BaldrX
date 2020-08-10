package net.rifttech.baldr.util.location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.util.Vector;

@Getter @Setter
@AllArgsConstructor
public class CustomLocation {
    private final long timestamp = System.currentTimeMillis();

    private double x, y, z;

    private float yaw, pitch;

    public CustomLocation clone() {
        return new CustomLocation(x, y, z, yaw, pitch);
    }

    public Vector toVector() {
        return new Vector(this.x, this.y, this.z);
    }
}
