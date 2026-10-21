package net.jzells.voyager_golems.common.blocks;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.jzells.voyager_golems.common.blocks.blockentity.GolemDenBlockEntity;
import net.jzells.voyager_golems.common.blocks.blockentity.VoyagerBlockEntities;
import net.jzells.voyager_golems.common.data.GolemDenDefinition;
import net.jzells.voyager_golems.common.data.GolemDenDefinitionRegistry;
import net.jzells.voyager_golems.util.VoyagerGuiUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GolemDenBlock extends BaseEntityBlock{

    public static final MapCodec<GolemDenBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    BlockBehaviour.propertiesCodec(),
                    Codec.STRING.fieldOf("golemDen")
                            .flatXmap(s -> DataResult.success(GolemDenDefinitionRegistry.get(s)),
                                    p->DataResult.success(p.group().getId()))
                            .forGetter(GolemDenBlock::getDefinition)
            ).apply(instance,GolemDenBlock::new));

    protected GolemDenBlock(Properties pProperties, GolemDenDefinition definition) {
        super(pProperties);
        this.definition = definition;
    }

    public MapCodec<GolemDenBlock> codec() {
        return CODEC;
    }

    private final GolemDenDefinition definition;

    public GolemDenDefinition getDefinition() {
        return definition;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new GolemDenBlockEntity(pPos, pState);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(pBlockEntityType == VoyagerBlockEntities.GOLEM_DEN_BLOCK_ENTITY.get())
        {
            return ((pLevel1, pPos, pState1, pBlockEntity) ->
                    GolemDenBlockEntity.tick(pLevel, pPos, pState, (GolemDenBlockEntity) pBlockEntity)); // this is where the magic happens
        }
        return null;
    }

    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);

        pTooltip.add(Component.literal(
                "§7Group: " + this.definition.group().getColorName()
        ));
        pTooltip.add(Component.literal(
                "§7Tier: §f" + this.definition.tier()
        ));
        pTooltip.add(Component.literal(
                "§7=========="
        ));
        pTooltip.add(Component.literal(
                "§l§7Den Bonuses:"
        ));
        pTooltip.add(Component.literal(
                "§eSpeed Multiplier: " + VoyagerGuiUtil.colorPercentage(
                        this.definition.speed_boost()
                )
        ));
        pTooltip.add(Component.literal(
                "§6Output Multiplier: " + VoyagerGuiUtil.colorPercentage(
                        this.definition.output_boost()
                )
        ));
        pTooltip.add(Component.literal(
                "§aXP Boost: " + VoyagerGuiUtil.colorPercentage(
                        this.definition.xp_boost()
                )
        ));
    }

    // right click opens ui

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            if (level.getBlockEntity(pos) instanceof GolemDenBlockEntity golemDenBlockEntity) {
                player.openMenu(new SimpleMenuProvider(golemDenBlockEntity,golemDenBlockEntity.getDisplayName()), pos);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.CONSUME;
    }

//    @Override
//    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
//        this.useWithoutItem(state,level,pos,player,hitResult);
//        ItemInteractionResult.
//        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
//    }
    //    @Override
//    public InteractionResult use(
//            BlockState pState,
//            Level pLevel,
//            BlockPos pPos,
//            Player pPlayer,
//            InteractionHand pHand,
//            BlockHitResult pHit
//    ) {
//        if (pLevel.isClientSide) {
//            return InteractionResult.sidedSuccess(true);
//        }
//
//        if (pPlayer instanceof ServerPlayer serverPlayer
//                && pLevel.getBlockEntity(pPos) instanceof GolemDenBlockEntity blockEntity) {
//            NetworkHooks.openScreen(serverPlayer, blockEntity, pPos);
//        }
//
//        return InteractionResult.sidedSuccess(false);
//    }


//    private boolean passClientChecks(
//            BlockState pState,
//            Level pLevel,
//            BlockPos pPos,
//            Player pPlayer
//    ) {
//        if (pLevel.isClientSide) {
//            return false;
//        }
//
//        return pLevel.getBlockEntity(pPos) instanceof GolemDenBlockEntity;
//    }

}
