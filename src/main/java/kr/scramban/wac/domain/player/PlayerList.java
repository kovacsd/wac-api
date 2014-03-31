package kr.scramban.wac.domain.player;

import java.util.HashMap;
import java.util.Map;

public class PlayerList {

    private final Map<String, Player> players = new HashMap<String, Player>();
    private String myName;

    {
        players.put(Player.NEUTRAL_NAME, Player.NATURAL);
    }

    public void addEnemy(final String name) {
        addPlayer(name, false);
    }

    public void addMe(final String name) {
        myName = name;
        addPlayer(name, true);
    }

    private void addPlayer(final String name, final boolean isMe) {
        players.put(name, new Player(name, isMe));
    }

    public Player getPlayer(final String name) {
        return players.get(name);
    }

    public String getMyName() {
        return myName;
    }
}
