package sparkles.events.listeners;

import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import sparkles.controllers.ChannelController;
import sparkles.controllers.MessageController;

public class ChannelListener extends ListenerAdapter {
    private static ChannelController cc;

    public ChannelListener(ChannelController cc) {
        this.cc = cc;
    }

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        super.onGuildVoiceJoin(event);
        System.out.println("User join");
        System.out.println(event.getChannelJoined().getName());
        cc.channelActionPicker(event);
    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        super.onGuildVoiceLeave(event);
        System.out.println("User leave");
        System.out.println(event.getChannelLeft().getName());
        cc.channelActionPicker(event);
    }

    @Override
    public void onGuildVoiceMove(@NotNull GuildVoiceMoveEvent event) {
        super.onGuildVoiceMove(event);
        System.out.println("User movido");
        System.out.println(event.getChannelJoined().getName());
        System.out.println(event.getChannelLeft().getName());
        cc.channelActionPicker(event);
    }
}
