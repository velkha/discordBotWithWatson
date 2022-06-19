package sparkles.controllers;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sparkles.utilities.FileUtilities;
import sparkles.utilities.Utilities;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

public class CommandController {
    private static final String ERR_COMM_NOT_FOUND="Lo siento, no reconozco el comando -> ";
    private static Map<String, String> commandsMap;
    public CommandController(){
        if(commandsMap==null){
            this.initializeMap();
        }
    }
    private void initializeMap() {
        FileUtilities futil = new FileUtilities();
        Utilities util = new Utilities();
        try {
            commandsMap = (Map<String, String>) util.jsonToMapKV(futil.getJsonFromFile("commands.json"));
        } catch (IOException | URISyntaxException e) {
            LOGGER.error(e.getMessage());
        }
    }
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandController.class);
    public void commandProcedure(MessageReceivedEvent event) {
        LOGGER.info("Comando detectado: "+ event.getMessage().getContentDisplay());
        String command = event.getMessage().getContentDisplay().substring(1);
        switch (command.toLowerCase()){
            case "help":
            case "ayuda":
                this.sendCommandList(event);
                break;
            default:
                if(!this.onlyTextCommand(command))
                    this.sendMessage(event,ERR_COMM_NOT_FOUND+command);
                else{
                    this.sendMessage(event, commandsMap.get(command));
                }
        }
    }
    private void addOtherCommands(StringBuilder stringBuilder) {
        stringBuilder.append(this.addCommandString("help"));
        stringBuilder.append(this.addCommandString("ayuda"));
    }
    private String addCommandString(String str){
        return "&"+str+"\n";
    }
    private boolean onlyTextCommand(String command) {
        return commandsMap!=null&&commandsMap.containsKey(command);
    }
    private  void sendMessage(MessageReceivedEvent event, String mensaje) {
        event.getChannel().sendMessage(mensaje).queue();
    }
    private void sendCommandList(MessageReceivedEvent event) {
        this.sendMessage(event, this.getCommandList());
    }
    public String getCommandList(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Los commandos disponibles son los siguientes: \n");
        for (Map.Entry<String, String> entry : commandsMap.entrySet()) {
            stringBuilder.append("&");
            stringBuilder.append(entry.getKey());
            stringBuilder.append("\n");
        }
        this.addOtherCommands(stringBuilder);
        return stringBuilder.toString();
    }


}
