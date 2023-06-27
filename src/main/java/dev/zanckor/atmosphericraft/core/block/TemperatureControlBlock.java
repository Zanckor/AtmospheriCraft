package dev.zanckor.atmosphericraft.core.block;

import dev.zanckor.atmosphericraft.api.chunkmanager.FakeChunk;
import dev.zanckor.atmosphericraft.api.database.LocateHash;
import dev.zanckor.atmosphericraft.api.database.TemperatureControlEnum;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

import static dev.zanckor.atmosphericraft.api.database.TemperatureControlEnum.NONE;

public class TemperatureControlBlock extends Block {
    TemperatureControlEnum controlTemperatureEnum;
    public static final BooleanProperty WORKING_STATUS = BooleanProperty.create("working_status");

    public TemperatureControlBlock(Properties properties, TemperatureControlEnum controlTemperatureEnum) {
        super(properties);

        this.controlTemperatureEnum = controlTemperatureEnum;
        this.registerDefaultState(this.defaultBlockState().setValue(WORKING_STATUS, Boolean.valueOf(false)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        super.createBlockStateDefinition(stateBuilder);
        stateBuilder.add(WORKING_STATUS);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        final ChunkPos CHUNK_POS = new ChunkPos(blockPos);
        FakeChunk fakeChunk = LocateHash.getFakeChunk(CHUNK_POS);
        blockState.setValue(WORKING_STATUS, !blockState.getValue(WORKING_STATUS));

        //If working status is on, change chunk temperature based on controlTemperatureEnum
        fakeChunk.changeTemperatureControlEnum(blockState.getValue(WORKING_STATUS) ? controlTemperatureEnum : NONE);
        LocateHash.putChunkHashMap(CHUNK_POS, fakeChunk);


        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }
}
