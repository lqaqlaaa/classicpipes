package jagm.classicpipes.util;

import jagm.classicpipes.PipeConfig;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class ItemInPipe {

    public static final short PIPE_LENGTH = 2048;
    public static final short HALFWAY = PIPE_LENGTH / 2;
    public static final short DEFAULT_SPEED = 64;
    public static final short DEFAULT_ACCELERATION = 1;
    public static final short SPEED_LIMIT = (short) PipeConfig.speedLimit();
    public static final short DESPAWN_AGE = 24000;

    public static final Codec<ItemInPipe> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
                MiscUtil.UNLIMITED_STACK_CODEC.fieldOf("item").orElse(ItemStack.EMPTY).forGetter(ItemInPipe::getStack),
                Codec.SHORT.fieldOf("speed").orElse(PipeConfig.initialItemPacketSpeed()).forGetter(ItemInPipe::getSpeed),
                Codec.INT.fieldOf("progress").orElse(0).forGetter(ItemInPipe::getProgress),
                Codec.BYTE.fieldOf("from_direction").orElse((byte) 0).forGetter(item -> (byte) item.getFromDirection().get3DDataValue()),
                Codec.BYTE.fieldOf("target_direction").orElse((byte) 0).forGetter(item -> (byte) item.getTargetDirection().get3DDataValue()),
                Codec.BOOL.fieldOf("ejecting").orElse(true).forGetter(ItemInPipe::isEjecting),
                Codec.LONG.fieldOf("age").orElse(0L).forGetter(ItemInPipe::getAge)
        ).apply(instance, ItemInPipe::new)
    );

    private ItemStack stack;
    private short speed;
    private int progress;
    private Direction fromDirection;
    private Direction targetDirection;
    private boolean ejecting;
    private long age;

    public ItemInPipe(ItemStack stack, short speed, int progress, Direction fromDirection, Direction targetDirection, boolean ejecting, long age) {
        this.stack = stack;
        this.speed = (short) Math.min(speed, SPEED_LIMIT);
        this.progress = progress;
        this.fromDirection = fromDirection;
        this.targetDirection = targetDirection;
        this.ejecting = ejecting;
        this.age = age;
    }

    public ItemInPipe(ItemStack stack, Direction fromDirection, Direction toDirection) {
        this(stack, fromDirection, toDirection, true);
    }

    public ItemInPipe(ItemStack stack, Direction fromDirection, Direction toDirection, boolean ejecting) {
        this(stack, PipeConfig.initialItemPacketSpeed(), 0, fromDirection, toDirection, ejecting, 0L);
    }

    public ItemInPipe(ItemStack stack, short speed, int progress, byte fromDirection, byte targetDirection, boolean ejecting, long age) {
        this(stack, speed, progress, Direction.from3DDataValue(fromDirection), Direction.from3DDataValue(targetDirection), ejecting, age);
    }

    public ItemInPipe copyWithCount(int count) {
        return new ItemInPipe(this.getStack().copyWithCount(count), this.speed, this.progress, this.fromDirection, this.targetDirection, this.ejecting, this.age);
    }

    public void move(short targetSpeed, short acceleration) {
        if (this.speed < targetSpeed) {
            this.speed = (short) Math.min(this.speed + acceleration, Math.min(targetSpeed, SPEED_LIMIT));
        } else if (this.speed > targetSpeed) {
            this.speed = (short) Math.max(this.speed - acceleration, Math.max(targetSpeed, 1));
        }
        this.progress += this.speed;
        this.age++;
    }

    public void drop(ServerLevel level, BlockPos pos) {
        if (!this.stack.isEmpty()) {
            Vec3 offset = this.getRenderPosition(0.0F);
            ItemEntity droppedItem = new ItemEntity(level, pos.getX() + offset.x, pos.getY() + offset.y, pos.getZ() + offset.z, this.stack);
            droppedItem.setDefaultPickUpDelay();
            level.addFreshEntity(droppedItem);
        }
    }

    public Vec3 getRenderPosition(float partialTicks){
        float p = (float) this.progress / PIPE_LENGTH + partialTicks * (float) this.speed / PIPE_LENGTH;
        float q = 1.0F - p;
        boolean h = p < 0.5F;
        Direction d = h ? this.fromDirection : this.targetDirection;
        return new Vec3(
                d == Direction.WEST ? (h ? p : q) : (d == Direction.EAST ? (h ? q : p) : 0.5F),
                d == Direction.DOWN ? (h ? p : q) : (d == Direction.UP ? (h ? q : p) : 0.5F),
                d == Direction.NORTH ? (h ? p : q) : (d == Direction.SOUTH ? (h ? q : p) : 0.5F)
        );
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
    }

    public int getProgress() {
        return this.progress;
    }

    public void resetProgress(Direction direction) {
        this.progress -= PIPE_LENGTH;
        this.fromDirection = direction;
        this.targetDirection = direction;
    }

    public boolean isEjecting() {
        return this.ejecting;
    }

    public void setEjecting(boolean ejecting) {
        this.ejecting = ejecting;
    }

    public Direction getFromDirection() {
        return fromDirection;
    }

    public Direction getTargetDirection() {
        return targetDirection;
    }

    public void setTargetDirection(Direction direction) {
        this.targetDirection = direction;
    }

    public short getSpeed() {
        return this.speed;
    }

    public long getAge() {
        return this.age;
    }

}
