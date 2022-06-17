package Discord_bot.Commands.Music;

import com.sedmelluq.discord.lavaplayer.filter.equalizer.Equalizer;
import com.sedmelluq.discord.lavaplayer.filter.equalizer.EqualizerFactory;
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
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager {
    private static PlayerManager INSTANCE;

    private final Map<Long, GuildMusicManager> musicManagers;
    private AudioPlayerManager audioPlayerManager = null;
    private final GuildMessageReceivedEvent event;

    public PlayerManager(GuildMessageReceivedEvent event) throws Exception{
        this.musicManagers = new HashMap<>();
        this.event = event;
        try{
            this.audioPlayerManager = new DefaultAudioPlayerManager();
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

    //Commands
    //Load and play track
    public void LoadAndPlay(TextChannel channel, String trackUrl){
        GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());

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
                //get list of tracks
                final List<AudioTrack> tracks = playlist.getTracks();
                AudioTrack track = tracks.get(0);
                channel.sendMessage("Adding track to queue: ")
                        .append(track.getInfo().title)
                        .append("' by '")
                        .append(track.getInfo().author)
                        .queue();
                musicManager.scheduler.queue(track);

            }
            @Override
            public void noMatches() {
            }
            @Override
            public void loadFailed(FriendlyException exception) {

            }
        });
    }
    public void LoadAndPlayPlaylist(TextChannel channel, String trackUrl){
        GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());

        this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                //get list of tracks
                final List<AudioTrack> tracks = playlist.getTracks();
                channel.sendMessage(String.valueOf(tracks.size()))
                        .append(" tracks from playlist ")
                        .append(playlist.getName())
                        .queue();
                //queue in all the tracks from playlist
                for(AudioTrack track:tracks){
                    musicManager.scheduler.queue(track);
                }
            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException exception) {

            }
        });
    }

    //Force play a track
    public void loadAndPlay2(VoiceChannel channel, String trackUrl){
        GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());

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

    //Show queue
    public void showQueue(TextChannel channel){
        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());
        musicManager.scheduler.showQueue();
    }

    //equalizer settings
    public void setEqualizer(float []eq,TextChannel channel){
        System.out.println("Calling setEqualizer in PlayerManager");
        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());
        musicManager.scheduler.setEqualizer(eq);
    }
    public void resetEqualizer(TextChannel channel){
        System.out.println("Calling setEqualizer in PlayerManager");
        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());
        musicManager.scheduler.resetEqualizer();
    }
    public void showEqualizer(TextChannel channel){
        System.out.println("Calling showEqualizer in PlayerManager");
        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());
        musicManager.scheduler.displayEqualizer();
    }

    public String getGuildName(){
        return event.getGuild().getName();
    }

    public static PlayerManager getInstance(GuildMessageReceivedEvent event) throws Exception{
        if(INSTANCE == null){
            System.out.println("PlayerManager is null. Creating new PlayerManager...");
            INSTANCE = new PlayerManager(event);
        }
        else if(!INSTANCE.getGuildName().equals(event.getGuild().getName())){ //if Bot is in a different server, then create new PlayerManager
            System.out.println("In different server. Creating new PlayerManager...");
            INSTANCE = new PlayerManager(event);
        }
        return INSTANCE;
    }


}
