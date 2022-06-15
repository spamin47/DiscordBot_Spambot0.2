package Discord_bot.ListenerFunctions;

import Discord_bot.Command.CommandListener;
import Discord_bot.Command.CommandManager;
import Discord_bot.Commands.Music.PlayCommand;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class JoinListener extends ListenerAdapter {

    public String prefix;
    public CommandManager cmdManager = new CommandManager();

    public JoinListener(String prefix){
        this.prefix = prefix;
    }

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent event){
        logMember(event);

    }
    //Log members who joined
    public static void logMember(GuildVoiceJoinEvent event){
        System.out.println(event.toString());
        System.out.println("User joined: " + event.getMember());
        System.out.println("User Effective name: " + event.getMember().getEffectiveName());
        System.out.println("User id: " + event.getMember().getId());

        if(event.getMember().getId().toString().equals("147920540168880128")){
            System.out.println("the lord has join");

            EntranceCommand entranceCommand = new EntranceCommand();
            entranceCommand.handle(event);
        }

    }
}

