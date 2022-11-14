package sparkles.controllers;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sparkles.ddbb.GestorDDBB;
import sparkles.events.listeners.ChannelListener;
import sparkles.events.listeners.MessageListener;
import sparkles.events.listeners.ReadyListener;
import sparkles.utilities.Utilities;

import javax.naming.NamingException;
import javax.security.auth.login.LoginException;
import java.sql.SQLException;

public class DiscordBotController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscordBotController.class);
    public static void main(String[] args) {
        //WatsonAssistant wa = new WatsonAssistant();
        botInit();
        //botTest();
    }

    private static void botTest() {

        GestorDDBB gestorDDBB = new GestorDDBB();
        try {
            gestorDDBB.crearConexion();
            gestorDDBB.cerrarConexion();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }


    public static void botInit(){

        try {
            DiscordBotController.startBot();

        } catch (LoginException e) {
            throw new RuntimeException(e);
        }
    }
    public static void startBot() throws LoginException {
        LOGGER.info("Comenzando creacion del bot");
        Utilities utils = new Utilities();
        JDA bot = JDABuilder.createDefault(utils.getResource("sparkles.token", "sparklesConfig.properties")).setActivity(Activity.playing(utils.getResource("sparkles.action", "SparklesConfig.properties"))).build();
        MessageController mc = new MessageController();
        ReadyController rc = new ReadyController();
        ChannelController cc = new ChannelController();
        bot.addEventListener(new MessageListener(mc));
        bot.addEventListener(new ReadyListener(rc));
        bot.addEventListener(new ChannelListener(cc));
        LOGGER.info("Bot iniciado\n"+"Directorio: "+System.getProperty("user.dir")+"\nHome: "+System.getProperty("user.home"));
    }
}
