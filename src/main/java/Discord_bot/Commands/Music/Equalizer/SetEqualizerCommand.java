package Discord_bot.Commands.Music.Equalizer;

import Discord_bot.Command.Command;
import Discord_bot.Commands.Music.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class SetEqualizerCommand implements Command {
    public static final float[] BASS_BOOST =
            {0.2f, 0.15f, 0.1f, 0.05f, 0.0f, -0.05f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f};
    @Override
    public String getName() {
        return "eq";
    }


    @Override
    public void handle(GuildMessageReceivedEvent event, String[] commandArgs) {

        System.out.println("Handling " + getName() + " command...");
        final TextChannel textChannel = event.getChannel();
        final Member self = event.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if(!selfVoiceState.inVoiceChannel()){
            textChannel.sendMessage("Need to be in VC!").queue();
            return;
        }
        final Member member = event.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if(!memberVoiceState.inVoiceChannel()){
            textChannel.sendMessage("Need to be in VC!").queue();
            return;
        }

        //display equalizer in textchannel
        if(commandArgs[1].equalsIgnoreCase("show")){
            try{
                PlayerManager.getInstance(event).showEqualizer(textChannel);
            }catch (Exception e){
                e.printStackTrace();
            }
            return;
        }
        //set equalizer to bass boosted setting
        if(commandArgs[1].equalsIgnoreCase("bass")){
            try{
                PlayerManager.getInstance(event).setEqualizer(BASS_BOOST,textChannel);
            }catch (Exception e){
                e.printStackTrace();
            }
            return;
        }
        //reset equalizer gain values
        if(commandArgs[1].equalsIgnoreCase("reset")){
            try{
                PlayerManager.getInstance(event).resetEqualizer(textChannel);
            }catch (Exception e){
                e.printStackTrace();
            }
            return;
        }
        float[] eq = new float[15];
        int i = 1;
        System.out.println("Converting argument to equalizer settings...");
        while(i!=16 && i<commandArgs.length){
            eq[i-1] = Float.parseFloat(commandArgs[i]);
            System.out.print(eq[i-1] + " ");
            i++;
        }
        System.out.println("");

        try{
            PlayerManager.getInstance(event).setEqualizer(eq,textChannel);
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}