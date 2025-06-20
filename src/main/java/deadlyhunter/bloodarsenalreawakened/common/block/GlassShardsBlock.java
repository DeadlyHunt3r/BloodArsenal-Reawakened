package deadlyhunter.bloodarsenalreawakened.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import wayoftime.bloodmagic.util.helper.PlayerSacrificeHelper;

public class GlassShardsBlock extends Block {
    public GlassShardsBlock() {
        super(Properties.create(Material.IRON)
            .hardnessAndResistance(0.5F)
            .sound(SoundType.GLASS)
            .harvestTool(ToolType.PICKAXE)
            .notSolid()
            .doesNotBlockMovement()
            .setOpaque((state, world, pos) -> false)
            .setBlocksVision((state, world, pos) -> false)
        );
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, net.minecraft.entity.Entity entity) {
        if (!world.isRemote && entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;

            livingEntity.attackEntityFrom(DamageSource.CACTUS, 2.0F);

            if (livingEntity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) livingEntity;
                PlayerSacrificeHelper.findAndFillAltar(world, player, 50, true);
            }
        }

        super.onEntityCollision(state, world, pos, entity);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D); 
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D); 
    }
}
