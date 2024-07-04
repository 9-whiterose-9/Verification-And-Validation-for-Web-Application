package webapp.webpresentation;

import java.util.LinkedList;
import java.util.List;

public class Helper {

	private List<String> messages;

	public Helper() {
		messages = new LinkedList<String>();
	}

	public List<String> getMessages () {
		return messages;
	}
	
	public void addMessage(String message) {
		messages.add(message);
	}
	
	public boolean isHasMessages() {
		return messages.size() != 0;
	}

}
