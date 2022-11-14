package sparkles.controllers;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static sparkles.utilities.StringUtils.extractUrls;

public class BuildSearcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);
    private static final String NOT_FOUND="Builds para la clase indicada no encontradas";
    public List<String>  buscarBuilds(MessageReceivedEvent event, String clase){
        List<String> rtnStr;
        GuildChannel classCategory = this.checkIfBuildSectionExist(clase, event);
        if(classCategory!=null){
            rtnStr=this.getBuilds(event, clase, classCategory);
        }
        else{
            rtnStr=null;
        }
        return rtnStr;
    }

    private List<String> getBuilds(MessageReceivedEvent event, String clase, GuildChannel classCategory) {
        List<String> buildsStr=new ArrayList<>();
        Category category = (Category) classCategory;
        List<GuildChannel> channels = category.getChannels();
        buildsStr.add("He encontrado las siguientes builds con referencia a la categoria \""+clase+"\"\n");
        for(GuildChannel channel: channels){
            if(channel.getType()==ChannelType.TEXT){
                buildsStr.add("Builds en el canal de **"+channel.getName()+"**");
                LOGGER.info(channel.getName());
                buildsStr.addAll(this.getBuildsFrom(channel));
            }
        }
        for (String url : buildsStr)
        {
            LOGGER.info(url);
        }
        return buildsStr;
    }

    private List<String> getBuildsFrom(GuildChannel channel) {
        List<String> urls = new ArrayList<String>();
        StringBuilder strBuilds = new StringBuilder();
        MessageHistory history = MessageHistory.getHistoryFromBeginning((MessageChannel) channel).complete();
        List<Message> mess = history.getRetrievedHistory();
        for(Message msg: mess){
            strBuilds.append(msg.getContentRaw()+"\n");
            for(Message.Attachment atchh: msg.getAttachments()){
                strBuilds.append(atchh.getUrl()+"\n");
            }
        }
        urls = extractUrls(strBuilds.toString());
        return urls;
    }

    public GuildChannel checkIfBuildSectionExist(String channelName, MessageReceivedEvent event){
        Guild guild = event.getGuild();

        GuildChannel channelFound = null;
        LOGGER.debug("buscando canales");
        List<GuildChannel> channels = guild.getChannels();
        for(GuildChannel channel : channels) {
            if(channel.getName().equalsIgnoreCase(channelName)&&channel.getType()== ChannelType.CATEGORY){
                channelFound=channel;
                break;
            }
        }
        return channelFound;
    }

    public String getProfessions(MessageReceivedEvent event) {
        Guild guild = event.getGuild();
        boolean flag=false;
        StringBuilder str= new StringBuilder();
        LOGGER.debug("buscando canales");
        List<GuildChannel> channels = guild.getChannels();
        for(GuildChannel channel : channels) {
            if(channel.getType()== ChannelType.CATEGORY){
                LOGGER.info(channel.getName());
                if(flag){
                    str.append(channel.getName()+"\n");
                }
                if(channel.getName().toLowerCase().contains("start builds")){
                    flag=true;
                }
                if(channel.getName().toLowerCase().contains("end builds")){
                    flag=false;
                }
            }
        }
        return str.toString();
    }
}
