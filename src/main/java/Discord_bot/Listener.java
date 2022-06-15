package Discord_bot;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Listener extends ListenerAdapter {
    @Override
    public void onReady(ReadyEvent event){
        System.out.printf("%#s is ready\n", event.getJDA().getSelfUser());
    }

}
