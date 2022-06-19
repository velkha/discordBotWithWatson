package sparkles.controllers;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);
    public void processMessage(MessageReceivedEvent event){

        boolean isBot = event.getAuthor().isBot();
        if(isBot){
            LOGGER.info("Bot realizando peticion: %s", event.getAuthor().getName());
            this.ifBotProcedures(event);
        }
        else{
            LOGGER.info("Usuario realizando peticion: %s", event.getAuthor().getName());
            this.ifHumanProcedures(event);
        }
    }
    public void ifBotProcedures(MessageReceivedEvent event){

    }
    public void ifHumanProcedures(MessageReceivedEvent event){

        CommandController commandController;


        /*if(this.isMessageForWatson(event)){

        }*/
        if(event.getMessage().getContentDisplay().charAt(0)=='&'){
            commandController = new CommandController();
            commandController.commandProcedure(event);
        }
        else{
            this.proceedWithStandardMessage(event);
        }
    }


    private void proceedWithStandardMessage(MessageReceivedEvent event) {
        User user = event.getAuthor();
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();
        String msg = message.getContentDisplay();
        channel.sendMessage("No me moleste porfavor, estoy pensando en alpacas magicas multicolores").queue();
    }

    private boolean isMessageForWatson(MessageReceivedEvent event) {
        return false;
    }

}
