package Discord_bot.Commands.Music;

import Discord_bot.Command.Command;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class SkipCommand implements Command {
    @Override
    public String getName() {
        return "skip";
    }


    @Override
    public void handle(GuildMessageReceivedEvent event, String[] commandArgs) {

        System.out.println("Handling " + getName() + " command...");
        final TextChannel textChannel = event.getChannel();
        final Member self = event.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if(!selfVoiceState.inVoiceChannel()){
            textChannel.sendMessage("Need to be in VC!").queue();
            return;
        }
        final Member member = event.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if(!memberVoiceState.inVoiceChannel()){
            textChannel.sendMessage("Need to be in VC!").queue();
            return;
        }
        try{
            PlayerManager.getInstance(event).skipMusic(textChannel);

        }catch(Exception e){
            e.printStackTrace();
        }

    }
}