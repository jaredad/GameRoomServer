package centralCode;

public class MessageHandler {

	Profile profileHandler;
	public MessageHandler() {
		profileHandler = new Profile();
	}

	public void handleMessage(String message) {
		int spaceIndex = message.indexOf(" ");
		int ord = Integer.parseInt(message.substring(0, spaceIndex));
		message = message.substring(spaceIndex + 1);
		if (ord == MessageType.PROFILE.ordinal()) {
			handleProfile(message);
		}
	}

	public void handleProfile(String profile) {
		profileHandler.deserialize(profile);
	}

}
