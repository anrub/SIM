package devhood.im.sim.service;

import devhood.im.sim.messages.BroadcastMessage;
import devhood.im.sim.messages.Message;
import devhood.im.sim.messages.RoomMessage;
import devhood.im.sim.messages.SingleMessage;
import devhood.im.sim.messages.UserStatusMessage;
import devhood.im.sim.model.UserStatus;

public class MessageFactory {
	public static Message createMessage() {
		return new Message();
	}

	public static RoomMessage createRoomMessage() {
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
		return new BroadcastMessage();
	}
}
