package jagm.classicpipes.blockentity;

import jagm.classicpipes.PipeConfig;

import jagm.classicpipes.ClassicPipes;
import jagm.classicpipes.block.CopperPipeBlock;
import jagm.classicpipes.services.Services;
import jagm.classicpipes.util.FacingOrNone;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Predicate;

public class CopperPipeEntity extends RoundRobinPipeEntity {

    private long cooldown;

    public CopperPipeEntity(BlockPos pos, BlockState state) {
        this(ClassicPipes.COPPER_PIPE_ENTITY, pos, state);
    }

    public CopperPipeEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);
        this.cooldown = this.extractionIntervalTicks();
    }

    @Override
    public void tickServer(ServerLevel level, BlockPos pos, BlockState state) {
        super.tickServer(level, pos, state);
        if (state.getValue(CopperPipeBlock.ENABLED) && state.getValue(CopperPipeBlock.FACING) != FacingOrNone.NONE) {
            if (this.cooldown-- <= 0) {
                Direction direction = state.getValue(CopperPipeBlock.FACING).getDirection();
                if (Services.LOADER_SERVICE.handleItemExtraction(this, state, level, pos.relative(direction), direction.getOpposite(), this.extractAmount(), this.filterPredicate())) {
                    level.sendBlockUpdated(pos, state, state, 2);
                    this.setChanged();
                }
                this.cooldown = this.extractionIntervalTicks();
            }
        }
    }

    protected int extractAmount() {
        return PipeConfig.copperExtractAmount();
    }

    protected long extractionIntervalTicks() {
        return PipeConfig.copperExtractInterval();
    }

    @Override
    public short getTargetSpeed() { return PipeConfig.copperSpeed(); }

    @Override
    public short getAcceleration() { return PipeConfig.copperAcceleration(); }


    protected Predicate<ItemStack> filterPredicate() {
        return stack -> true;
    }

    @Override
    protected void loadAdditional(CompoundTag valueInput, HolderLookup.Provider registries) {
        super.loadAdditional(valueInput, registries);
        this.cooldown = valueInput.getLong("cooldown");
    }

    @Override
    protected void saveAdditional(CompoundTag valueOutput, HolderLookup.Provider registries) {
        super.saveAdditional(valueOutput, registries);
        valueOutput.putLong("cooldown", this.cooldown);
    }

}
