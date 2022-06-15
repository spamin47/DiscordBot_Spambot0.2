package Discord_bot.Command;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.*;

public class CommandListener extends ListenerAdapter {
    public String prefix;
    public CommandManager cmdManager = new CommandManager();

    public CommandListener(String prefix){
        this.prefix = prefix;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        try{
            cmdManager.handle(event, prefix);
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
