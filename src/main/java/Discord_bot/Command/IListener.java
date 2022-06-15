package Discord_bot.Command;

import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;

public interface IListener {
    String getName();
    void handle(GuildVoiceJoinEvent event);

}
