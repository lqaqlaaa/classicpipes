package jagm.classicpipes.util;

import net.minecraft.core.Direction;

public class ScheduledRoute {

    

    private final Direction direction;
    private long age;

    public ScheduledRoute(Direction direction) {
        this.direction = direction;
        this.age = 0;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean timedOut() {
        return age >= jagm.classicpipes.PipeConfig.scheduledRouteTimeoutTicks();
    }

    public void tick() {
        this.age++;
    }

}
