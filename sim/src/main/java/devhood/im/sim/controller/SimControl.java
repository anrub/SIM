package devhood.im.sim.controller;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import devhood.im.sim.config.SimConfiguration;
import devhood.im.sim.event.EventDispatcher;
import devhood.im.sim.event.Events;
import devhood.im.sim.model.FileSendAcceptMessage;
import devhood.im.sim.model.FileSendRejectMessage;
import devhood.im.sim.model.FileSendRequestMessage;
import devhood.im.sim.model.Message;
import devhood.im.sim.model.MessageType;
import devhood.im.sim.model.User;
import devhood.im.sim.service.PeerToPeerMessageReceiver;
import devhood.im.sim.service.interfaces.MessageCallback;
import devhood.im.sim.service.interfaces.MessageProcessor;
import devhood.im.sim.service.interfaces.MessageSender;
import devhood.im.sim.service.interfaces.UserService;

/**
 * Controller Klasse von Sim. Das ist die zentrale Kontrollinstanz der
 * Anwendung. Facade nach aussen.
 * 
 * @author flo
 * 
 */
@Named("simControl")
public class SimControl {
	/**
	 * Der aktuelle User, der die Anwendung verwendet.
	 */
	private User currentUser;

	/**
	 * Konfiguration von SIM.
	 */
	@Inject
	private SimConfiguration simConfiguration;

	/**
	 * Empfaenger von Messages.
	 */
	@Inject
	private PeerToPeerMessageReceiver messageReceiver;

	/**
	 * Sender von Messages.
	 */
	@Inject
	private MessageSender messageSender;

	// private List<Conversation> conversations
	// TODO Messages auch hier halten
	private List<Message> messages;
	// TODO messageProcessors, sind objs. die auf die, die message prozessieren
	private List<MessageProcessor> messageProcessors;

	/**
	 * Service rund um User Informationen.
	 */
	@Inject
	private UserService userService;

	/**
	 * Initialisiert die Bean.
	 */
	public void init() {
		// TODO Sender kriegt Callback zum empfangen?! klingt komisch.
		messageSender.setMessageCallback(new MessageCallback() {

			@Override
			public void messageReceivedCallback(Message m) {
				System.out.println(m);
				EventDispatcher.fireEvent(Events.MESSAGE_RECEIVED, m);
			}

			@Override
			public void messageFileRequestReceivedCallback(
					FileSendRequestMessage m) {
				EventDispatcher.fireEvent(Events.MESSAGE_FILE_REQUEST_RECEIVED,
						m);
			}

			@Override
			public void messageFileRequestAcceptCallback(FileSendAcceptMessage m) {
				EventDispatcher.fireEvent(Events.MESSAGE_FILE_ACCEPT_RECEIVED,
						m);
			}

			@Override
			public void messageFileRequestRejectCallback(FileSendRejectMessage m) {
				EventDispatcher.fireEvent(Events.MESSAGE_FILE_REJECT_RECEIVED,
						m);
			}

		});

		userService.refresh(simConfiguration.getCurrentUser());
	}

	/**
	 * Versendet eine Message.
	 * 
	 * @param m
	 *            Message zum versandt.
	 */
	public void sendMessage(Message m) {
		List<String> receiver = m.getReceiver();
		if (MessageType.SINGLE.equals(m.getMessageType())) {
			if (receiver.size() == 1) {
				String singleReceiver = receiver.get(0);
				User user = userService.getUser(singleReceiver);
				messageSender.sendMessage(user, m);
			}
		} else if (MessageType.ALL.equals(m.getMessageType())
				|| MessageType.USER_STATUS.equals(m.getMessageType())) {
			messageSender.sendMessageToAllUsers(m);
		}

	}

	public List<User> getCurrentUsers() {
		return userService.getCurrentUsers();
	}

	public User getCurrentUser() {
		return currentUser;
	}

}
