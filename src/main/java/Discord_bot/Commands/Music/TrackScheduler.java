package Discord_bot.Commands.Music;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter{

    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue; //queue for tracks


    public TrackScheduler(AudioPlayer player){
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
        this.player.startTrack(this.queue.poll(),false);
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

    public void test(){

    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if(endReason.mayStartNext){
            nextTrack();
        }
    }

}