package deadlyhunter.bloodarsenalreawakened.common.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import java.util.UUID;

public class ResetHeartsCommand {

    public static LiteralArgumentBuilder<CommandSource> register() {
        return Commands.literal("resethearts")
            .requires(source -> source.hasPermissionLevel(2))
            .then(Commands.argument("player", EntityArgument.player())
                .executes(context -> {
                    ServerPlayerEntity player = EntityArgument.getPlayer(context, "player");
                    resetPlayerHearts(player);
                    context.getSource().sendFeedback(new StringTextComponent(TextFormatting.GREEN + "Herz-Zähler für Spieler " + player.getName().getString() + " wurden zurückgesetzt."), true);
                    //player.sendMessage(new StringTextComponent(TextFormatting.YELLOW + "Deine Herz-Zähler wurden vom Server zurückgesetzt!"), UUID.fromString("00000000-0000-0000-0000-000000000000"));
                    return Command.SINGLE_SUCCESS;
                }))
            .executes(context -> {
                try {
                    ServerPlayerEntity player = context.getSource().asPlayer();
                    resetPlayerHearts(player);
                    context.getSource().sendFeedback(new StringTextComponent(TextFormatting.GREEN + "Deine Herz-Zähler wurden zurückgesetzt."), true);
                   //player.sendMessage(new StringTextComponent(TextFormatting.YELLOW + "Deine Herz-Zähler wurden vom Server zurückgesetzt!"), UUID.fromString("00000000-0000-0000-0000-000000000000"));

                } catch (CommandSyntaxException e) {
                    context.getSource().sendErrorMessage(new StringTextComponent("Dieser Befehl kann nur von einem Spieler ausgeführt werden!"));
                    return 0;
                }
                return Command.SINGLE_SUCCESS;
            });
    }

    private static void resetPlayerHearts(ServerPlayerEntity player) {
        player.getPersistentData().putInt("heart1_count", 0);
        player.getPersistentData().putInt("heart2_count", 0);
        player.getPersistentData().putInt("heart3_count", 0);
    }
}
