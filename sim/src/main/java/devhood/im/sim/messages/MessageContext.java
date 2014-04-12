package devhood.im.sim.messages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import devhood.im.sim.ui.event.EventDispatcher;
import devhood.im.sim.ui.event.EventObserver;
import devhood.im.sim.ui.event.Events;

/**
 * MessageContext, zentraler Punkt der Messages.
 * 
 * @author flo
 * 
 */
@Named
public class MessageContext implements EventObserver {
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

	/**
	 * History der Messages.
	 */
	private Map<String, List<Message>> history = new HashMap<String, List<Message>>();

	private List<MessageObserver> messageObservers = new ArrayList<MessageObserver>();

	public void registerMessageObserver(MessageObserver messageObserver) {
		messageObservers.add(messageObserver);

		EventDispatcher.add(this);
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
		addMessageHistory(m);
		if (m instanceof RoomMessage) {
			textMmessageSender.sendMessageToRoom((RoomMessage) m);
		} else if (m instanceof SingleMessage) {
			m.setSender(simConfiguration.getUsername());
			String singleReceiver = receiver.get(0);
			User user = userService.getUser(singleReceiver);
			textMmessageSender.sendMessage(user, m);
		} else if (m instanceof BroadcastMessage) {
			m.setSender(simConfiguration.getUsername());
			textMmessageSender.sendMessageToAllUsers(m);
		}

	}

	class DelegatingMessageObserver implements MessageObserver {
		@Override
		public void onMessage(Message m) {
			notifyOnMessage(m);
			addMessageHistory(m);
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

	@Override
	public void eventReceived(Events event, Object o) {
		if (Events.SEND_MESSAGE.equals(event)) {
			Message m = (Message) o;
			try {
				sendMessage(m);
			} catch (MessagingException e) {
				// TODO FF
				throw new RuntimeException(e);
			}
		}
	}

	public List<Message> getHistory(String id) {
		List<Message> list = history.get(id);
		if (list == null) {
			list = new ArrayList<Message>();
			history.put(id, list);
		}

		return list;
	}

	public void addMessageHistory(Message m) {
		if (m instanceof SingleMessage) {
			getHistory(m.getReceiver().get(0)).add(m);
		} else if (m instanceof BroadcastMessage) {
			getHistory(simConfiguration.getStreamTabName()).add(m);
		} else if (m instanceof RoomMessage) {
			RoomMessage rm = (RoomMessage) m;
			getHistory(rm.getRoomName()).add(m);
		}
	}

}
