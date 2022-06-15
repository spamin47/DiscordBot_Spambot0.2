package Discord_bot.Command;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public interface Command {

    String getName();
    void handle(GuildMessageReceivedEvent event, String[] commandArgs);

}
