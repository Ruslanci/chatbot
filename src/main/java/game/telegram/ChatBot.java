package game.telegram;

import game.core.GameSession;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.telegram.abilitybots.api.objects.Flag.TEXT;
import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

import java.util.HashMap;
import java.util.Map;

public class ChatBot extends AbilityBot {
    private static final String tokenPath = "PasswordGameBotToken";
    private static final String namePath = "PasswordGameBotName";
    private final Map<Long, GameSession> userSessions;

    public ChatBot() {
        super(System.getenv(tokenPath), System.getenv(namePath));
        userSessions = new HashMap<>();
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
                    Long userId = ctx.user().getId();
                    GameSession session = userSessions.get(userId);

                    if (session == null) {
                        sendText("You are not in a session, type /start to play", ctx.chatId());
                    } else {
                        session.putMessage(ctx.update().getMessage().getText());
                    }
                })
                .build();
    }

    

    public Ability startGameCommand() {
        return Ability.builder()
                .name("start")
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx -> {
                    Long userId = ctx.user().getId();
                    Long chatId = ctx.chatId();

                    GameSession existingSession = userSessions.get(userId);

                    

                    if (existingSession != null) {
                        sendText("You are already in a session.", chatId);
                    } else {
                        GameSession session = new GameSession(this, chatId, userId);
                        userSessions.put(userId, session);
                        session.start();
                    }
                })
                .build();
    }

    public Ability endGameCommand() {
        return Ability.builder()
                .name("end")
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx -> {
                    Long userId = ctx.user().getId();
                    Long chatId = ctx.chatId();
                    
                    endGame(userId, chatId);
                })
                .build();
    }
    public void endGame(Long userId, Long chatId) {
        GameSession session = userSessions.remove(userId);
        if (session != null) {
            sendText("Game session ended. Type /start to play again.", chatId);
        } else {
            sendText("You are not in a session.", chatId);
        }
    }
}