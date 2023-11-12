package game.telegram;

import game.core.MessageTrader;
import game.core.Session;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Exchanger;

import static org.telegram.abilitybots.api.objects.Flag.TEXT;
import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

public class ChatBot extends AbilityBot {
    private static String tokenPath = "PasswordGameBotToken";
    private static String namePath = "PasswordGameBotName";
    private final Map<Long, Session> sessionMap;

    public ChatBot() {
        super(System.getenv(tokenPath), System.getenv(namePath));
        sessionMap = new HashMap<Long, Session>();
    }

    @Override
    public long creatorId() {
        return 0;
    }

    public void sendText(String text, Long chatId) {
        SendMessage msg = new SendMessage();
        msg.setText(text);
        msg.setChatId(chatId);
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public Ability readText() {
        return Ability.builder()
                .name(DEFAULT)
                .flag(TEXT)
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx -> {
                    if (!sessionMap.containsKey(ctx.chatId()))
                        sendText("You are not in session, type /start to play", ctx.chatId());
                    else
                        sessionMap.get(ctx.chatId()).putMessage(ctx.update().getMessage().getText());
                })
                .build();
    }
    public Ability startGame() {
        return Ability.builder()
                .name("start")
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx -> {
                    Session currentSession = new Session(this, ctx.chatId());
                    sessionMap.put(ctx.chatId(), currentSession);
                    new Thread(currentSession).start();
                })
                .build();
    }
}