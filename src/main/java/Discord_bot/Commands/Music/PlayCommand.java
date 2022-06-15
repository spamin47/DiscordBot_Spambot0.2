package Discord_bot.Commands.Music;

import Discord_bot.Command.Command;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
@SuppressWarnings("ConstantConditions")
public class PlayCommand implements Command {
    @Override
    public String getName() {
        return "play";
    }


    @Override
    public void handle(GuildMessageReceivedEvent event, String[] commandArgs) {
        final TextChannel textChannel = event.getChannel();
        final Member self = event.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if(!selfVoiceState.inVoiceChannel()){
            textChannel.sendMessage("I'm not in VC!").queue();
            return;
        }
        final Member member = event.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if(!memberVoiceState.inVoiceChannel()){
            textChannel.sendMessage("Need to be in VC!").queue();
            return;
        }

        try{
            PlayerManager.getInstance().loadAndPlay(textChannel,"https://youtu.be/bdpv-wuY9qo");
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
