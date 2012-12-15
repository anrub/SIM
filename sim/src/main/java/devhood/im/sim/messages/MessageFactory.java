package devhood.im.sim.messages;

import devhood.im.sim.messages.model.BroadcastMessage;
import devhood.im.sim.messages.model.Message;
import devhood.im.sim.messages.model.RoomMessage;
import devhood.im.sim.messages.model.SingleMessage;
import devhood.im.sim.messages.model.UserStatusMessage;
import devhood.im.sim.model.UserStatus;

public class MessageFactory {
	public static Message createRoomMessage() {
		return new RoomMessage();
	}

	public static Message createMessage(boolean room, String name) {
		if (room) {
			RoomMessage msg = new RoomMessage();
			msg.setRoomName(name);

			return msg;
		}
		return new SingleMessage();
	}

	public static Message createUserStatusMessage(UserStatus status) {
		return new UserStatusMessage(status);
	}

	public static Message createBroadcastMessage() {
		Message msg = new BroadcastMessage();
		msg.setReliable(false);
		return msg;
	}
}
