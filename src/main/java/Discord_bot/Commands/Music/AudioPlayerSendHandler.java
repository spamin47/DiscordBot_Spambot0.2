package Discord_bot.Commands.Music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

public class AudioPlayerSendHandler implements AudioSendHandler {
    private final AudioPlayer audioPlayer;
    private final ByteBuffer buffer;
    private final MutableAudioFrame frame;

    public AudioPlayerSendHandler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.buffer = ByteBuffer.allocate(1024); //amount of byte available to be sent to computer (too much byte might slow down computer!)
        this.frame = new MutableAudioFrame(); //allows writing to buffer
        this.frame.setBuffer(buffer);
    }


    @Override
    public boolean canProvide() {
        return this.audioPlayer.provide(this.frame); //if frame is provided, return true and call provide20MsAudio()
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        return this.buffer.flip();
    }

    @Override
    public boolean isOpus(){
        return true;
    }
}
