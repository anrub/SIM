package devhood.im.sim.messages;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import devhood.im.sim.config.SimConfiguration;
import devhood.im.sim.messages.interfaces.TextMessageSender;
import devhood.im.sim.messages.model.BroadcastMessage;
import devhood.im.sim.messages.model.FileSendAcceptMessage;
import devhood.im.sim.messages.model.FileSendRejectMessage;
import devhood.im.sim.messages.model.FileSendRequestMessage;
import devhood.im.sim.messages.model.Message;
import devhood.im.sim.messages.model.RoomMessage;
import devhood.im.sim.messages.model.SingleMessage;
import devhood.im.sim.messages.observer.MessageObserver;
import devhood.im.sim.model.User;
import devhood.im.sim.service.interfaces.UserService;

/**
 * MessageContext, zentraler Punkt der Messages.
 *
 * @author flo
 *
 */
@Named
public class MessageContext {
	/**
	 * Konfiguration von SIM.
	 */
	@Inject
	private SimConfiguration simConfiguration;

	/**
	 * Sender von Messages.
	 */
	@Inject
	private TextMessageSender textMmessageSender;

	/**
	 * Service rund um User Informationen.
	 */
	@Inject
	private UserService userService;

	private List<MessageObserver> messageObservers = new ArrayList<MessageObserver>();

	public void registerMessageObserver(MessageObserver messageObserver) {
		messageObservers.add(messageObserver);
	}

	/**
	 * Initialisiert die Bean.
	 */
	@PostConstruct
	public void init() {
		userService.refresh(userService.getCurrentUser());

		textMmessageSender.setMessageCallback(new DelegatingMessageObserver());
	}

	private void notifyOnFileSendAcceptMessage(FileSendAcceptMessage m) {
		for (MessageObserver o : messageObservers) {
			o.onFileSendAcceptMessage(m);
		}
	}

	private void notifyOnFileSendRejectMessage(FileSendRejectMessage m) {
		for (MessageObserver o : messageObservers) {
			o.onFileSendRejectMessage(m);
		}
	}

	private void notifyOnFileSendRequestMessage(FileSendRequestMessage m) {
		for (MessageObserver o : messageObservers) {
			o.onFileSendRequestMessage(m);
		}
	}

	private void notifyOnMessage(Message m) {
		for (MessageObserver o : messageObservers) {
			o.onMessage(m);
		}
	}

	/**
	 * Versendet eine Message.
	 *
	 * @param m
	 *            Message zum versandt.
	 */
	public void sendMessage(Message m) throws MessagingException {
		List<String> receiver = m.getReceiver();
		if (m instanceof RoomMessage) {
			textMmessageSender.sendMessageToRoom((RoomMessage) m);
		} else if (m instanceof SingleMessage) {
			String singleReceiver = receiver.get(0);
			User user = userService.getUser(singleReceiver);
			textMmessageSender.sendMessage(user, m);
		} else if (m instanceof BroadcastMessage) {
			textMmessageSender.sendMessageToAllUsers(m);
		}

	}

	class DelegatingMessageObserver implements MessageObserver {
		@Override
		public void onMessage(Message m) {
			notifyOnMessage(m);
		}

		@Override
		public void onFileSendRequestMessage(FileSendRequestMessage m) {
			notifyOnFileSendRequestMessage(m);
		}

		@Override
		public void onFileSendRejectMessage(FileSendRejectMessage m) {
			notifyOnFileSendRejectMessage(m);
		}

		@Override
		public void onFileSendAcceptMessage(FileSendAcceptMessage m) {
			notifyOnFileSendAcceptMessage(m);
		}
	}

}
