package Discord_bot.Commands.Music;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import java.awt.*;
import java.time.Duration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter{

    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue; //queue for tracks
    private final GuildMessageReceivedEvent event;
    private final EmbedBuilder embed;
    private MessageEmbed builtEmbed;

    public TrackScheduler(AudioPlayer player, GuildMessageReceivedEvent event){
        this.event = event;
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
        this.embed = new EmbedBuilder();
        embed.setColor(new Color(0x327da8));
        embed.setThumbnail("https://i.pinimg.com/originals/a0/f7/5e/a0f75e40bc09ad8850447362d2e1756d.jpg");
        builtEmbed = embed.build();

    }

    //queue track
    public void queue(AudioTrack track){
        if(!this.player.startTrack(track,true)){ //player start track if nothing is playing at the moment.
            this.queue.offer(track); //adds track to queue if a track is playing
            printQueue();
        }else{
            //create embed
            setUpdateEmbed_CurrentlyPlaying(track);
            printQueue();
        }
    }

    //goto next track
    public void nextTrack(){
        AudioTrack track = this.queue.poll();
        //If there is no next track, then stop player
        if(track==null){
            stopTrack();
            return;
        }

        this.player.startTrack(track,false);

        //create embed
        setUpdateEmbed_CurrentlyPlaying(track);
        printQueue();

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
        printQueue();
    }

    //show queue
    public void showQueue(){
        event.getChannel().sendMessage(setUpdateEmbed_Queue()).queue();
    }

    public void test(){
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if(endReason.mayStartNext){
            System.out.println("Next Track: " + track.getInfo().title);
            nextTrack();
        }
        if(queue.isEmpty() && this.player.getPlayingTrack()==null){ //if track queue is empty, then disconnect the bot from Voice Channel
            System.out.println("Disconnecting...");
            AudioManager audioManager = event.getGuild().getAudioManager();
            audioManager.closeAudioConnection();
        }
    }

    public void printQueue(){
        System.out.println("Queue: ");
        for(AudioTrack track: queue){
            System.out.println(track.getInfo().title);
        }
    }

    //set up embedded for queue
    public MessageEmbed setUpdateEmbed_Queue(){
        EmbedBuilder queueEmbed = new EmbedBuilder();
        String queueSongs = "```";
        queueEmbed.setColor(new Color(0x327da8));
        queueEmbed.setTitle("Queue");
        //get the top 10 songs in queue
        int i = 0;
        for(AudioTrack track: queue){
            if(i==10){
                break;
            }
            AudioTrackInfo trackInfo = track.getInfo();
            queueSongs+=convertMilliSecToTime(trackInfo.length) + " " + trackInfo.title+"\n";
            i++;
        }
        queueSongs+="```"; //put description in discord block format
        queueEmbed.setDescription(queueSongs);
        return queueEmbed.build();
    }

    //set up embedded for currently playing song
    public void setUpdateEmbed_CurrentlyPlaying(AudioTrack track){
        AudioTrackInfo trackInfo = track.getInfo();
        embed.setTitle("**"+"Currently Playing:"+"**");
        embed.setDescription("```" +convertMilliSecToTime(trackInfo.length)+
                " " +trackInfo.title+"```\n"+
                "_" +trackInfo.author+"_\n"+
                "<" +trackInfo.uri+">");
        event.getChannel().sendMessage(embed.build()).queue();
    }

    //get time(String) from milliSeconds
    public String convertMilliSecToTime(Long milliSeconds){
        return Duration.ofMillis(milliSeconds).toString()
                .replace('M',':')
                .replaceAll("PT","")
                .replace('S',' ');
    }

}