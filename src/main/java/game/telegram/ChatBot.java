package game.telegram;

import game.core.GameSession;
import game.core.SessionThread;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.telegram.abilitybots.api.objects.Flag.TEXT;
import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

public class ChatBot extends AbilityBot {
    private static final String tokenPath = "PasswordGameBotToken";
    private static final String namePath = "PasswordGameBotName";
    private SessionThread sessionThread;

    public ChatBot() {
        super(System.getenv(tokenPath), System.getenv(namePath));
        sessionThread = null;
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
                    if (sessionThread == null || !sessionThread.isAlive())
                        sendText("You are not in session, type /start to play", ctx.chatId());
                    else
                        sessionThread.putMessage(ctx.update().getMessage().getText());
                })
                .build();
    }
    public Ability startGame() {
        return Ability.builder()
                .name("start")
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx -> {
                    sessionThread = new SessionThread(new GameSession(this, ctx.chatId()));
                    sessionThread.start();
                })
                .build();
    }

    public Ability endGame() {
        return Ability.builder()
                .name("end")
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx -> {
                    sessionThread.interrupt();
                    sendText("Your are not in session anymore, type /start to play again", ctx.chatId());
                })
                .build();
    }
}