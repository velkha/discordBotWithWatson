package sparkles.controllers;

import com.ibm.watson.text_to_speech.v1.model.Voice;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChannelController {
    private String sectionName = "canales test bot";
    private String channelName = "ava";


    public void channelActionPicker(@NotNull GuildVoiceJoinEvent event){

        if(checkInfiniteChannel(event.getChannelJoined())){
            this.infinityJoined(event.getChannelJoined());
        }
    }
    public void channelActionPicker(GuildVoiceLeaveEvent event){
        if(checkInfiniteChannel(event.getChannelLeft())){
            this.infinityLeft(event.getChannelLeft());
        }
    }
    public void channelActionPicker(GuildVoiceMoveEvent event){
        if(checkInfiniteChannel(event.getChannelJoined())){
            this.infinityJoined(event.getChannelJoined());
        }
        if(checkInfiniteChannel(event.getChannelLeft())){
            this.infinityLeft(event.getChannelLeft());
        }
    }


    public boolean checkInfiniteChannel(GuildChannel chn){
        if(chn.getType() == ChannelType.VOICE){
            VoiceChannel vc = (VoiceChannel) chn;
            return vc.getParentCategory().getName().equalsIgnoreCase(this.sectionName);
        }

        return false;
    }

    public void infinityJoined(GuildChannel chn){
        VoiceChannel vc = (VoiceChannel) chn;
        Category category = vc.getParentCategory();
        if(!isOnlyOneInfinityChannel(chn))
        chn.getGuild().createVoiceChannel(this.channelName,category).complete();


    }
    public void infinityLeft(GuildChannel chn){
        VoiceChannel vc = (VoiceChannel) chn;
        int test = vc.getMembers().size();
        if(vc.getMembers().size()==0&&isOnlyOneInfinityChannel(chn)){

        }
    }

    private boolean isOnlyOneInfinityChannel(GuildChannel chn) {
        VoiceChannel vc = (VoiceChannel) chn;
        Category category = vc.getParentCategory();
        List<GuildChannel> gcs=category.getChannels();
        VoiceChannel aux;
        boolean flag = false;
        for (GuildChannel gc: gcs) {
            if(gc.getName().equalsIgnoreCase(this.channelName)&&gc.getType()==ChannelType.VOICE){
                System.out.println("canal con el nombre encontrado");
                aux=(VoiceChannel) gc;
                if(aux.getMembers().size()<1){
                    System.out.println("canal con el nombre vacio encontrado");
                    if(flag){
                        System.out.println("y eliminado");
                        gc.delete().complete();
                    }
                    flag=true;
                }
            }
        }
        return flag;
    }


}
