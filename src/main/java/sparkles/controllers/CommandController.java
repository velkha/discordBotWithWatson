package sparkles.controllers;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandController {
    private static final String ERR_COMM_NOT_FOUND="Lo siento, no reconozco el comando -> ";

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandController.class);
    public void commandProcedure(MessageReceivedEvent event) {
        LOGGER.info("Comando detectado: %s", event.getMessage().getContentDisplay());
        String command = event.getMessage().getContentDisplay().substring(1);
        switch (command.toLowerCase()){
            case "help":
            case "ayuda":
                this.sendCommandList(event.getChannel());
                break;
            default:
                if(!this.onlyTextCommand(event))
                    event.getChannel().sendMessage(ERR_COMM_NOT_FOUND+command).queue();
        }
    }

    private boolean onlyTextCommand(MessageReceivedEvent event) {

        return false;
    }

    private void sendCommandList(MessageChannel channel) {

    }
}
