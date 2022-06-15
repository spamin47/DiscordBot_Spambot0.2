package Discord_bot.Commands.Music;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter{

    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue; //queue for tracks
    private final GuildMessageReceivedEvent event;

    public TrackScheduler(AudioPlayer player, GuildMessageReceivedEvent event){
        this.event = event;
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
    }

    //queue track
    public void queue(AudioTrack track){
        if(!this.player.startTrack(track,true)){ //player start track if nothing is playing at the moment.
            this.queue.offer(track); //adds track to queue if a track is playing
        }
    }

    //goto next track
    public void nextTrack(){
        AudioTrack track = this.queue.poll();
        AudioTrackInfo trackInfo = track.getInfo();
        this.player.startTrack(track,false);
        event.getChannel().sendMessage("Now Playing " + trackInfo.title + " by " + trackInfo.author).queue();
    }

    //pause track
    public void pauseTrack(){
        this.player.setPaused(true);
    }

    //unpause track
    public void unpauseTrack(){
        this.player.setPaused(false);
    }

    //stop track
    public void stopTrack(){
        this.player.stopTrack();
    }

    //force play track
    public void playTrack(AudioTrack track){
        this.player.startTrack(track,false);
    }

    //clear queue
    public void clearQueue(){
        queue.clear();
    }

    public void test(){

    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if(endReason.mayStartNext){
            System.out.println("Next Track: " + track.getInfo().title);
            nextTrack();
        }
        if(queue.isEmpty()){
            System.out.println("Disconnecting...");
            AudioManager audioManager = event.getGuild().getAudioManager();
            audioManager.closeAudioConnection();
        }

    }

}