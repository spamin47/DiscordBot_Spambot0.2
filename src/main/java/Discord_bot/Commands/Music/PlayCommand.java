package Discord_bot.Commands.Music;

import Discord_bot.Command.Command;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeSearchMusicProvider;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeSearchMusicResultLoader;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

@SuppressWarnings("ConstantConditions")
public class PlayCommand implements Command {



    @Override
    public String getName() {
        return "play";
    }


    @Override
    public void handle(GuildMessageReceivedEvent event, String[] commandArgs) {

        System.out.println("Handling " + getName() + " command...");
        final TextChannel textChannel = event.getChannel();
        final Member self = event.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if(!selfVoiceState.inVoiceChannel()){
            JoinCommand JC = new JoinCommand();
            JC.handle(event,commandArgs);
        }
        final Member member = event.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if(!memberVoiceState.inVoiceChannel()){
            textChannel.sendMessage("Need to be in VC!").queue();
            return;
        }
        try{
            System.out.println("link: " + commandArgs[1]);

            PlayerManager.getInstance().loadAndPlay(textChannel,commandArgs[1]);
            //PlayerManager.getInstance().
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
