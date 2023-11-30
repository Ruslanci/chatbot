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

    private GameSession getSession(Long userId, Long chatId) {
        return userSessions.computeIfAbsent(userId, key -> new GameSession(this, chatId));
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

                    if (session == null || !session.isRunning()) {
                        sendText("You are not in a session, type /start to play", ctx.chatId());
                    } else {
                        session.putMessage(ctx.update().getMessage().getText());
                    }
                })
                .build();
    }

    

    public Ability startGame() {
        return Ability.builder()
                .name("start")
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx -> {
                    Long userId = ctx.user().getId();
                    Long chatId = ctx.chatId();

                    GameSession existingSession = userSessions.remove(userId);
                    if (existingSession != null) {
                        existingSession.stop();
                    }

                    GameSession session = getSession(userId, chatId);

                    if (session.isRunning()) {
                        sendText("You are already in a session.", chatId);
                    } else {
                        session.start();
                    }
                })
                .build();
    }

    public Ability endGame() {
        return Ability.builder()
                .name("end")
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx -> {
                    Long userId = ctx.user().getId();
                    Long chatId = ctx.chatId();
                    
                    GameSession session = userSessions.remove(userId);
                    if (session != null) {
                        session.stop();
                        sendText("Game session ended. Type /start to play again.", chatId);
                    } else {
                        sendText("You are not in a session.", chatId);
                    }
                })
                .build();
    }
}