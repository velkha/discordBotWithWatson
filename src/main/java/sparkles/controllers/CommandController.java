package sparkles.controllers;

import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sparkles.utilities.FileUtilities;
import sparkles.utilities.Utilities;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
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

        switch (command.split(" ", 2)[0].toLowerCase()){
            case "help":
            case "ayuda":
                this.sendCommandList(event);
                break;
            case "misdatos":
                this.showDatos(event);
                break;
            case "builds":
                this.getBuilds(event);
                break;
            case "test":
                this.testearCosas(event);
                break;
            default:
                if(!this.onlyTextCommand(command))
                    this.sendMessage(event,ERR_COMM_NOT_FOUND+command);
                else{
                    this.sendMessage(event, commandsMap.get(command));
                }
        }
    }

    private void testearCosas(MessageReceivedEvent event) {
        int x;
        x=3;
        testcosas2(x);
        event.getChannel().sendMessage("test: "+x).queue();
    }

    private void testcosas2(int x) {
        x=5;
    }

    private void showDatos(MessageReceivedEvent event) {
        User user = event.getAuthor();
        StringBuilder response =  new StringBuilder();
        response.append("Hola "+user.getName()+" aqui tienes tus datos por si quieres hacer algo con ellos\n");
        response.append("\nid: "+user.getAvatarId());
        response.append("\ntag: "+user.getAsTag());
        response.append("\nAvatarid: "+user.getAvatarId());
        response.append("\nAvatarurl: "+user.getAvatarUrl());
        response.append("\nDiscriminator: "+user.getDiscriminator());
        user.openPrivateChannel().queue((channel)->{
            channel.sendMessage(response.toString()).queue();
        });
    }

    private void addOtherCommands(StringBuilder stringBuilder) {
        stringBuilder.append(this.addCommandString("help"));
        stringBuilder.append(this.addCommandString("ayuda"));
        stringBuilder.append(this.addCommandString("misDatos"));
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
    private void getBuilds(MessageReceivedEvent event){
        User user = event.getAuthor();
        BuildSearcher bs = new BuildSearcher();
        String aux= event.getMessage().getContentDisplay().substring(1);
        if(aux.split(" ").length>1){
            String clase = (aux.split(" "))[1].toLowerCase();
            List<String> builds = bs.buscarBuilds(event, clase);
            user.openPrivateChannel().queue((channel)->{
                for(String str: builds){
                    channel.sendMessage(str).queue();
                }
            });
        }
        else{
            String clases=null;
            clases=bs.getProfessions(event);
            if(clases.length()<=1) clases="No tengo disponbles builds en este momento";
            event.getChannel().sendMessage(clases).queue();
        }

    };

}
