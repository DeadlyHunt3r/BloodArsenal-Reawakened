package deadlyhunter.bloodarsenalreawakened.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class LevelCleaverCommand {
    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
        dispatcher.register(
            Commands.literal("cleaverlevel")
                .then(Commands.argument("level", IntegerArgumentType.integer(0, 100))
                    .executes(ctx -> {
                        int level = IntegerArgumentType.getInteger(ctx, "level");
                        CommandSource source = ctx.getSource();
                        ServerPlayerEntity player = source.asPlayer();
                        ItemStack stack = player.getHeldItemMainhand();
                        if (stack.getItem() instanceof deadlyhunter.bloodarsenalreawakened.common.item.tool.BloodstormCleaverItem) {
                            CompoundNBT tag = stack.getOrCreateTag();
                            tag.putInt("Level", level);
                            tag.putInt("KillCount", 0);
                            stack.setTag(tag);
                            player.sendMessage(new StringTextComponent("Bloodstorm Cleaver level set to " + level), player.getUniqueID());
                            return 1;
                        } else {
                            source.sendErrorMessage(new StringTextComponent("You must hold the Bloodstorm Cleaver."));
                            return 0;
                        }
                    })
                )
        );
    }
}
