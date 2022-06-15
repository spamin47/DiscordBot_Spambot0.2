package Discord_bot.Commands.Music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {
    private static PlayerManager INSTANCE;

    private final Map<Long, GuildMusicManager> musicManagers;
    private AudioPlayerManager audioPlayerManager = null;

    public PlayerManager() throws Exception{
        this.musicManagers = new HashMap<>();

        try{
            System.out.println("Creating audioPlayerManager...");
            this.audioPlayerManager = new DefaultAudioPlayerManager();
            System.out.println("Finished creation...");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Cannot declare audioPlayerManager");
        }

        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild){
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {

           final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);

           guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
            return guildMusicManager;
        });
    }

    public void loadAndPlay(TextChannel channel, String trackUrl){
        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());
        this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                musicManager.scheduler.queue(track);

                channel.sendMessage("Adding track to queue: ")
                        .append(track.getInfo().title)
                        .append("' by '")
                        .append(track.getInfo().author)
                        .queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {

            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException exception) {

            }
        });
    }

    public static PlayerManager getInstance() throws Exception{

        if(INSTANCE == null){
            INSTANCE = new PlayerManager();
        }

        return INSTANCE;
    }
}
