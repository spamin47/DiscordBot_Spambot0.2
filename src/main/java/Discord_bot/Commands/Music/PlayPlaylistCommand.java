package Discord_bot.Commands.Music;

import Discord_bot.Command.Command;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class PlayPlaylistCommand implements Command{

        @Override
        public String getName() {
            return "pl";
        }


        @Override
        public void handle(GuildMessageReceivedEvent event, String[] commandArgs) {

            System.out.println("Handling " + getName() + " command...");
            final TextChannel textChannel = event.getChannel();
            final Member self = event.getGuild().getSelfMember();
            final GuildVoiceState selfVoiceState = self.getVoiceState();

            //if bot is not in VC then join VC
            if(!selfVoiceState.inVoiceChannel()){
                JoinCommand JC = new JoinCommand();
                JC.handle(event,commandArgs);
            }
            final Member member = event.getMember();
            final GuildVoiceState memberVoiceState = member.getVoiceState();

            if(!memberVoiceState.inVoiceChannel()){
                textChannel.sendMessage("Need to be in VC!").queue();
                return;
            }
                try{
                PlayerManager.getInstance(event).LoadAndPlayPlaylist(textChannel,commandArgs[1]);
            }catch(Exception e){
                e.printStackTrace();
            }

        }

    }
