package Discord_bot.Command;

import Discord_bot.Commands.DisconnectCommand;
import Discord_bot.Commands.GreetingCommand;
import Discord_bot.Commands.Music.*;
import Discord_bot.Commands.Music.Equalizer.SetEqualizerCommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

public class CommandManager {

    private GuildMessageReceivedEvent event;
    private String prefix;
    private ArrayList<Command> commands = new ArrayList<>(); //stores commands

    public CommandManager(){

        commands.add(new GreetingCommand());
        commands.add(new PlayCommand());
        commands.add(new JoinCommand());
        commands.add(new PauseCommand());
        commands.add(new SkipCommand());
        commands.add(new UnpauseCommand());
        commands.add(new StopCommand());
        commands.add(new DisconnectCommand());
        commands.add(new ClearCommand());
        commands.add(new PlayPlaylistCommand());
        commands.add(new ShowQueueCommand());
        commands.add(new SetEqualizerCommand());
    }

    public void handle(GuildMessageReceivedEvent event, String prefix){
        String[] args = event.getMessage().getContentRaw().split(" ");

        //check message sent and handle the command mentioned in message
        for(Command cmd: commands){//iterate through available commands
            if(args[0].equalsIgnoreCase(prefix + cmd.getName())){ //checks prefix and command name
                try{
                    cmd.handle(event, args); //handle the command
                }catch(Exception e){
                    e.printStackTrace();
                    System.out.println("something is wrong with CommandManager");
                }

            }
        }
    }
}
