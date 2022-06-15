package Discord_bot;

import Discord_bot.Command.CommandListener;
import Discord_bot.Command.Commands;
import Discord_bot.ListenerFunctions.JoinListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class BotStartup {
    public static void main(String[] args) throws LoginException {
        //get and set token for bot
        MyFileReader readToken = new MyFileReader("src/main/java/Keys/Keys.txt");
        JDABuilder jda = JDABuilder.createDefault(readToken.getText());
        readToken.closeFile();

        jda.setActivity(Activity.watching("Your mother"));
        jda.setStatus(OnlineStatus.ONLINE); //can set it to DO NOTDISTURB, IDLE, OFFLINE, INVISIBLE. Setting to invisible or offline is a bad idea

        //adding event listeners
        jda.addEventListeners(new Listener());
        jda.addEventListeners(new Commands("!"));
        jda.addEventListeners(new CommandListener("!"));
        jda.addEventListeners(new JoinListener("!"));


        jda.setChunkingFilter(ChunkingFilter.ALL); //allow you to see all the members in the discord server
        jda.setMemberCachePolicy(MemberCachePolicy.ALL);
        jda.enableIntents(GatewayIntent.GUILD_MEMBERS);//gives permissions to view members
        jda.enableIntents(GatewayIntent.GUILD_VOICE_STATES);
        jda.enableCache(CacheFlag.VOICE_STATE);
        jda.disableCache(CacheFlag.CLIENT_STATUS);
        jda.build();
    }
}