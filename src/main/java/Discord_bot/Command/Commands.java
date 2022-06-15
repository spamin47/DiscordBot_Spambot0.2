package Discord_bot.Command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;


public class Commands extends ListenerAdapter{
    public String prefix;

    public Commands(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) //Guild - discord server
    {
        String[] args = event.getMessage().getContentRaw().split(" ");

        System.out.println("\nRaw Content: \n" + event.getMessage());
        System.out.println("Command input: \n" + args[0]);
        //basic commmand
        if(args[0].equalsIgnoreCase(prefix + "hii")) {
            System.out.println(args[0]);//
            event.getMessage().reply("shut up retard!").queue();
            event.getMessage().reply("keep malding").queue();
            event.getMessage().reply("ratioed").queue();
            event.getMessage().reply("your gay").queue();
            event.getMessage().reply("your mom fat").queue();
            event.getChannel().sendMessage("smol pp").queue();
        }
        if(args[0].equalsIgnoreCase(prefix + "Ken")) {
            event.getChannel().sendMessage("nword").queue();
        }

        //embed command
        if(args[0].equalsIgnoreCase(prefix+"testEmbed")) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("Embed Title", "");
            embed.setDescription("this is a description of the embed"); //cannot post links
            embed.addField("Embed field 1","description of field 1",false);
            embed.addField("Embed field 2","description of field 2",false);
            embed.setColor(Color.GREEN);
            embed.setFooter("Bot created by spamak47");
            event.getChannel().sendMessage(embed.build()).queue();
            embed.clear(); //if doesn't get cleared then embed can get glitched out and does its own thing
        }

        //giving roles
        if(args[0].equalsIgnoreCase(prefix+"giveRole")) {
            if(event.getMessage().getMentionedRoles().toArray().length == 1) {
                if(event.getMessage().getMentionedUsers().toArray().length == 1) {
                    Member member = event.getGuild().getMember(event.getMessage().getMentionedUsers().get(0));//crtl shift o
                    Role roleToGive = event.getMessage().getMentionedRoles().get(0);
                    event.getGuild().addRoleToMember(member, roleToGive).queue();
                    event.getMessage().reply("Gave the role " + roleToGive.getAsMention()+ " to " + member.getAsMention()).queue();
                }
                else {
                    event.getMessage().reply("Please mention only 1 user to give").queue();
                }
            }
            else {
                event.getMessage().reply("Please mention only 1 role to give").queue();
            }
        }

        if(args[0].equalsIgnoreCase(prefix+"joinVC")) {
            if(!event.getGuild().getSelfMember().hasPermission(event.getChannel(),Permission.VOICE_CONNECT)) {
                event.getChannel().sendMessage("i do not have permissions.").queue();
            }
        }
    }
}
