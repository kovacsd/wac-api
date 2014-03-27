package kr.scramban.wac.domain.player;

public class Player {
	
	public static final String NEUTRAL_NAME = "neutral";
	public static final Player NATURAL = new Player(NEUTRAL_NAME, false);

	private final String name;
	private final boolean isMe;

	Player(String name, boolean isMe) {
		this.name = name;
		this.isMe = isMe;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isMe() {
		return isMe;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
