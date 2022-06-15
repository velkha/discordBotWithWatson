package sparkles.events.listeners;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import sparkles.controllers.MessageController;

import javax.security.auth.login.LoginException;

public class MessageListener extends ListenerAdapter {
    /*public static void main(String[] args)
            throws LoginException {
        JDA jda = JDABuilder.createDefault(args[0]).build();
        jda.addEventListener(new MessageListener());
    }*/
    private static MessageController messageController;

    public MessageListener(MessageController messageController) {
        this.messageController = messageController;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.TEXT)) {
            /*System.out.printf("[%s][%s] %#s: %s%n", event.getGuild().getName(),
                    event.getChannel().getName(), event.getAuthor(), event.getMessage().getContentDisplay());*/
            messageController.processMessage(event);

            System.out.printf("Mensaje enviado por %s en el canal %s \n", event.getAuthor().getName(), event.getChannel().getName());
            System.out.printf("Texto del mensaje: %s \n", event.getMessage().getContentDisplay());
        } else {
            System.out.printf("[PM] %#s: %s%n \n", event.getAuthor(), event.getMessage().getContentDisplay());
        }
    }
    private void guildChannelProcedures(){

    }
    private void privateChannelProcedures(){

    }

}