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
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {
    private static PlayerManager INSTANCE;

    private final Map<Long, GuildMusicManager> musicManagers;
    private AudioPlayerManager audioPlayerManager = null;
    private GuildMessageReceivedEvent event;

    public PlayerManager(GuildMessageReceivedEvent event) throws Exception{
        this.musicManagers = new HashMap<>();
        this.event = event;
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

           final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager,event);

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

    public void loadAndPlay2(VoiceChannel channel, String trackUrl){
        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());

        this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                musicManager.scheduler.playTrack(track);
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


    //pause track
    public void pauseMusic(TextChannel channel){
        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());
        musicManager.scheduler.pauseTrack();
    }

    //unpause track
    public void unpauseMusic(TextChannel channel){
        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());
        musicManager.scheduler.unpauseTrack();
    }

    //skip track
    public void skipMusic(TextChannel channel){
        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());
        musicManager.scheduler.nextTrack();
    }

    //stop track
    public void stopMusic(TextChannel channel){
        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());
        musicManager.scheduler.stopTrack();
    }

    //Clear Queue
    public void clearQueue(TextChannel channel) {
        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());
        musicManager.scheduler.clearQueue();
    }

    public static PlayerManager getInstance(GuildMessageReceivedEvent event) throws Exception{

        if(INSTANCE == null){
            INSTANCE = new PlayerManager(event);
        }

        return INSTANCE;
    }


}
