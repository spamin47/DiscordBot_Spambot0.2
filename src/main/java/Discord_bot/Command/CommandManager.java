package Discord_bot.Command;

import Discord_bot.Commands.GreetingCommand;
import Discord_bot.Commands.Music.JoinCommand;
import Discord_bot.Commands.Music.PlayCommand;
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
    }

    public void handle(GuildMessageReceivedEvent event, String prefix){
        String[] args = event.getMessage().getContentRaw().split(" ");

        //check message sent and handle the command mentioned in message
        for(Command cmd: commands){
            if(args[0].equalsIgnoreCase(prefix + cmd.getName())){
                try{
                    cmd.handle(event, args);
                }catch(Exception e){
                    e.printStackTrace();
                    System.out.println("something is wrong with CommandManager");
                }

            }
        }
    }
}
