package systems.conduit.fundamentals.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import systems.conduit.main.api.mixins.Player;
import systems.conduit.main.api.mixins.ServerPlayer;
import systems.conduit.main.core.commands.BaseCommand;

/**
 * @author Innectic
 * @since 12/13/2020
 */
public class FlyCommand extends BaseCommand {
    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return Commands.literal("fly").executes(source -> {
            if (source.getSource().getEntity() == null || !(source.getSource().getEntity() instanceof Player)) {
                source.getSource().sendFailure(new TextComponent("Fundamentals> ").withStyle(s -> s.withBold(true)
                        .withColor(ChatFormatting.YELLOW)).append(new TextComponent("You aren't a player!")
                        .withStyle(s -> s.withColor(ChatFormatting.RED).withBold(true))));
                return 0;
            }

            // Command sender is a player
            ServerPlayer target = (ServerPlayer) source.getSource().getEntity();

            boolean currentState = target.getAbilities().mayFly();

            target.getAbilities().setMayFly(!currentState);
            target.getAbilities().setFlying(!currentState);
            target.sendUpdatedAbilities();

            TextComponent base = (TextComponent) new TextComponent("Fundamentals> ").withStyle(s -> s.withBold(true).withColor(ChatFormatting.YELLOW));

            if (currentState) base.append(new TextComponent("You can no longer fly!").withStyle(s -> s.withColor(ChatFormatting.RED).withBold(true)));
            else base.append(new TextComponent("You can now fly!").withStyle(s -> s.withColor(ChatFormatting.GREEN).withBold(true)));

            source.getSource().sendSuccess(base, false);

            return 1;
        });
    }
}
