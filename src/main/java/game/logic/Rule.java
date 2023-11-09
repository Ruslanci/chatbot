package game.logic;
public interface Rule {
    public Boolean match(String password);
    public String getDescription();
}