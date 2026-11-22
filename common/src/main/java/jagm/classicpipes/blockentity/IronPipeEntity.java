package jagm.classicpipes.blockentity;

import jagm.classicpipes.PipeConfig;

import jagm.classicpipes.ClassicPipes;
import jagm.classicpipes.block.IronPipeBlock;
import jagm.classicpipes.util.ItemInPipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class IronPipeEntity extends ItemPipeEntity {

    public IronPipeEntity(BlockPos pos, BlockState state) {
        super(ClassicPipes.IRON_PIPE_ENTITY, pos, state);
    }

    @Override
    public void routeItem(BlockState state, ItemInPipe item) {
        item.setEjecting(false);
        item.setTargetDirection(state.getValue(IronPipeBlock.FACING));
    }

    @Override
    protected boolean canJoinNetwork() {
        return false;
    }

    @Override
    public short getTargetSpeed() {
        return PipeConfig.ironSpeed();
    }

    @Override
    public short getAcceleration() {
        return PipeConfig.ironAcceleration();
    }

}
