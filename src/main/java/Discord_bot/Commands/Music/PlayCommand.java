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

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.function.Function;

@SuppressWarnings("ConstantConditions")
public class PlayCommand implements Command {



    @Override
    public String getName() {
        return "p";
    }

    @Override
    public void handle(GuildMessageReceivedEvent event, String[] commandArgs) {

        System.out.println("Handling " + getName() + " command...");
        final TextChannel textChannel = event.getChannel();
        final Member self = event.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        //if bot is not in VC then join VC
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

        String link = commandArgs[1];
        try{
            //System.out.println("link: " + link);
            if(!isUrl(link)){
                System.out.println("Not a url link.");
                link = "ytsearch:";
                //concatenate array of strings into one link
                for(int i = 1; i<commandArgs.length;i++){
                    link+=commandArgs[i];
                }
            }
            System.out.println("SERVER: " + event.getGuild().getName() +". Initiate PlayCommand");
            PlayerManager.getInstance(event).LoadAndPlay(textChannel,link);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    //check if the string is an URL
    private boolean isUrl(String link) {
        try{
            URL url = new URL(link);
            url.toURI();
            System.out.println("succeeded in making url");
            return true;
        }catch(URISyntaxException e){
            return false;
        }catch (MalformedURLException e){
            return false;
        }
    }
}
