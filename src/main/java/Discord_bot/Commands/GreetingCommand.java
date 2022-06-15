package Discord_bot.Commands;

import Discord_bot.Command.Command;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class GreetingCommand implements Command {
    @Override
    public String getName() {
        return "hi";
    }

    @Override
    public void handle(GuildMessageReceivedEvent event, String[] commandArgs) {
        System.out.println("handling Greeting Command...");
        event.getChannel().sendMessage("Hi! " + event.getMember().getAsMention()).queue();
    }
}
