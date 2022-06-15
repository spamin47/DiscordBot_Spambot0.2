package Discord_bot.Commands.Music;

import Discord_bot.Command.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

@SuppressWarnings("ConstantConditions")
public class JoinCommand implements Command {
    @Override
    public String getName() {
        return "join";
    }

    @Override
    public void handle(GuildMessageReceivedEvent event, String[] commandArgs) {
        System.out.println("Handling " + getName() + " command...");

        final TextChannel channel = event.getChannel();
        final Member self = event.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if(selfVoiceState.inVoiceChannel()){ //check if bot is currently in a Voice Channel
            channel.sendMessage("I'm already in a voice channel!").queue();
            return;
        }

        final Member member = event.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if(!memberVoiceState.inVoiceChannel() ){ //check if member that sent command is in Voice Channel
            channel.sendMessage("You need to be in voice channel for me to join!").queue();
            return;
        }

        if(!self.hasPermission(Permission.VOICE_CONNECT)){
            channel.sendMessage("I do not have vc connect permission!").queue();
            return;
        }

        final AudioManager audioManager = event.getGuild().getAudioManager();
        final VoiceChannel memberChannel = memberVoiceState.getChannel();

        audioManager.openAudioConnection(memberChannel); //join channel
    }
}
