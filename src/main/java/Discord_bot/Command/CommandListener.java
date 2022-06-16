package Discord_bot.Command;

import net.dv8tion.jda.api.hooks.ListenerAdapter;

import net.dv8tion.jda.api.events.message.guild.*;

public class CommandListener extends ListenerAdapter {
    public String prefix;
    public CommandManager cmdManager = new CommandManager();
    public static GuildMessageReceivedEvent event;

    public CommandListener(String prefix){
        this.prefix = prefix;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        this.event = event;
        logMessage(event);
        try{
            cmdManager.handle(event, prefix);
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    //log out message of member
    public static void logMessage(GuildMessageReceivedEvent event){

        String[] args = event.getMessage().getContentRaw().split(" ");

        System.out.println("User Effective name: " + event.getMember().getEffectiveName() +
                "(" +event.getMember().getId() +")");
        //System.out.println("\nRaw Content: \n" + event.getMessage());
        System.out.println("Message:");
        //print out string array
        int size = args.length;
        for(int i = 0;i<size;i++){
            System.out.print(args[i] + " ");
        }
        System.out.println("");
    }
}
