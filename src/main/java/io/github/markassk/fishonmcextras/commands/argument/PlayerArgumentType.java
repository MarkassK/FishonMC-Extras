package io.github.markassk.fishonmcextras.commands.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.command.CommandSource;
import net.minecraft.command.EntitySelectorReader;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class PlayerArgumentType implements ArgumentType<String> {

    private PlayerArgumentType() {
    }

    public static PlayerArgumentType getPlayerArgumentType() {
        return new PlayerArgumentType();
    }

    @Override
    public String parse(final StringReader reader) throws CommandSyntaxException {
        return reader.readString();
    }

    public static PlayerListEntry getPlayer(final CommandContext<?> context, final String name) {
        if(MinecraftClient.getInstance().getNetworkHandler() != null) {
            return MinecraftClient.getInstance().getNetworkHandler().getPlayerListEntry(context.getArgument(name, String.class));
        }
        return null;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (context.getSource() instanceof CommandSource commandSource) {
            StringReader stringReader = new StringReader(builder.getInput());
            stringReader.setCursor(builder.getStart());
            EntitySelectorReader entitySelectorReader = new EntitySelectorReader(stringReader, EntitySelectorReader.shouldAllowAtSelectors(commandSource));

            try {
                entitySelectorReader.read();
            } catch (CommandSyntaxException ignored) {
            }

            return entitySelectorReader.listSuggestions(builder, builderx -> CommandSource.suggestMatching(commandSource.getPlayerNames().stream().filter(s -> {
                if (MinecraftClient.getInstance().player != null) {
                    return !Objects.equals(s, MinecraftClient.getInstance().player.getGameProfile().getName());
                }
                return true;
            }), builderx));
        } else {
            return Suggestions.empty();
        }
    }
}
