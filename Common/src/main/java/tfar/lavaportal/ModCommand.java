package tfar.lavaportal;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class ModCommand {

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(Commands.literal(LavaPortal.MOD_ID)
                .then(Commands.argument("destination", BlockPosArgument.blockPos()).executes(ModCommand::setDestination))
                .then(Commands.argument("dimension",StringArgumentType.string()).executes(ModCommand::setDestinationAndDimension))
        );
    }

    private static int setDestination(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Level level = context.getSource().getLevel();
        BlockPos pos = BlockPosArgument.getLoadedBlockPos(context,"destination");

        return 1;
    }

    private static int setDestinationAndDimension(CommandContext<CommandSourceStack>context) throws CommandSyntaxException {

        return 1;
    }


}
