package kr.scramban.wac.domain.player;

public enum PlayerType {

    ME(true),
    ENEMY(false),
    NATURAL(false);

    private final boolean me;

    private PlayerType(final boolean me) {
        this.me = me;
    }

    public boolean isMe() {
        return me;
    }
}
