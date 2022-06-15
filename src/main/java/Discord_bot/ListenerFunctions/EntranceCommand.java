package Discord_bot.ListenerFunctions;


import Discord_bot.Command.CommandListener;
import Discord_bot.Command.CommandManager;
import Discord_bot.Command.IListener;
import Discord_bot.Commands.Music.JoinCommand;
import Discord_bot.Commands.Music.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.managers.AudioManager;


public class EntranceCommand implements IListener {

    @Override
    public String getName() {
        return "entrance";
    }

    @Override
    public void handle(GuildVoiceJoinEvent event) {

        System.out.println("Handling " + getName() + " command...");
        final Member self = event.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();
        final AudioManager audioManager = event.getGuild().getAudioManager();

        if(!selfVoiceState.inVoiceChannel()){ //if bot is not in VC then join
            final VoiceChannel memberChannel = event.getChannelJoined(); //get voice channel
            audioManager.openAudioConnection(memberChannel); //join channel
        }

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    PlayerManager.getInstance(CommandListener.event).loadAndPlay2(event.getChannelJoined(),"https://youtu.be/ryDk5r-nNjY");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        t1.start();
            synchronized (audioManager){
                try{
                    audioManager.wait(5000);
                    audioManager.closeAudioConnection();
                }catch(Exception e){
                    e.printStackTrace();
                }
    }


    }
}

