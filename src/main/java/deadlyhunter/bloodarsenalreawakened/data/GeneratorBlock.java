package deadlyhunter.bloodarsenalreawakened.data;

import deadlyhunter.bloodarsenalreawakened.common.BloodArsenalReawakened;
import net.minecraft.block.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.stream.Collectors;

import static deadlyhunter.bloodarsenalreawakened.common.block.ModBlocks.*;
import static deadlyhunter.bloodarsenalreawakened.data.GeneratorItem.takeAll;

/**
 * Code partially adapted from Botania
 * https://github.com/Vazkii/Botania
 */
public class GeneratorBlock extends BlockStateProvider
{
    public GeneratorBlock(DataGenerator gen, ExistingFileHelper exFileHelper)
    {
        super(gen, BloodArsenalReawakened.MOD_ID, exFileHelper);
    }

    @Nonnull
    @Override
    public String getName()
    {
        return "Blood Arsenal Reawakened Blockstates";
    }

    @Override
    protected void registerStatesAndModels()
    {
        Set<Block> remainingBlocks = Registry.BLOCK.stream().filter(b -> BloodArsenalReawakened.MOD_ID.equals(Registry.BLOCK.getKey(b).getNamespace()))
                .collect(Collectors.toSet());

        // Remove manually written models

        // Single blocks
        stairsBlock((StairsBlock) BLOOD_INFUSED_WOOD_STAIRS.get(), BloodArsenalReawakened.rl("block/blood_infused_planks"));
        fenceBlock((FenceBlock) BLOOD_INFUSED_WOOD_FENCE.get(), BloodArsenalReawakened.rl("block/blood_infused_planks"));
        fenceGateBlock((FenceGateBlock) BLOOD_INFUSED_WOOD_FENCE_GATE.get(), BloodArsenalReawakened.rl("block/blood_infused_planks"));
        remainingBlocks.remove(BLOOD_INFUSED_WOOD_STAIRS.get());
        remainingBlocks.remove(BLOOD_INFUSED_WOOD_FENCE.get());
        remainingBlocks.remove(BLOOD_INFUSED_WOOD_FENCE_GATE.get());

        // Process special block types
        takeAll(remainingBlocks, b -> b instanceof RotatedPillarBlock).forEach(b -> buildRotatedPillars(b, b.getRegistryName()));

        takeAll(remainingBlocks, b -> b instanceof PaneBlock).forEach(b ->
        {
            String name = Registry.BLOCK.getKey(b).getPath();
            ResourceLocation edge = BloodArsenalReawakened.rl("block/" + name);
            ResourceLocation pane = BloodArsenalReawakened.rl("block/" + name.substring(0, name.length() - 5));
            paneBlock((PaneBlock) b, pane, edge);
        });

        remainingBlocks.remove(BLOOD_INFUSED_WOOD_SLAB.get());

        remainingBlocks.forEach(this::simpleBlock);

        // Slabs come after the double-slabs gen
        slabBlock((SlabBlock) BLOOD_INFUSED_WOOD_SLAB.get(), BloodArsenalReawakened.rl("blood_infused_planks"), BloodArsenalReawakened.rl("block/blood_infused_planks"));
    }

    private void buildRotatedPillars(Block pillar, ResourceLocation rl)
    {
        ModelFile pillarModel = models().cubeColumn(rl.getPath(),
                BloodArsenalReawakened.rl("block/" + rl.getPath()),
                BloodArsenalReawakened.rl("block/" + rl.getPath() + "_top"));
        getVariantBuilder(pillar)
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.X).setModels(new ConfiguredModel(pillarModel, 90, 90, false))
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y).setModels(new ConfiguredModel(pillarModel))
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Z).setModels(new ConfiguredModel(pillarModel, 90, 0, false));
    }
}
