package devhood.im.sim.ui.event;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import devhood.im.sim.messages.MessageContext;
import devhood.im.sim.messages.model.FileSendAcceptMessage;
import devhood.im.sim.messages.model.FileSendRejectMessage;
import devhood.im.sim.messages.model.FileSendRequestMessage;
import devhood.im.sim.messages.model.Message;
import devhood.im.sim.messages.observer.MessageObserver;
import devhood.im.sim.model.User;
import devhood.im.sim.service.interfaces.UserChangeObserver;
import devhood.im.sim.service.interfaces.UserService;

/**
 * Observiert {@link MessageContext} sowie {@link UserService}. Sobald eine neue
 * Message eingeht/neue User/User offline gehen, erzeugt diese Klasse einen
 * Event fuer die UI.
 * 
 * @author flo
 * 
 */
@Named
public class EventProducer implements MessageObserver,
		UserChangeObserver {

	@Inject
	private MessageContext messageContext;

	@Inject
	private UserService userService;

	@PostConstruct
	public void init() {
		messageContext.registerMessageObserver(this);
		userService.registerUserChangeObserver(this);
	}

	@Override
	public void onMessage(Message m) {
		System.out.println(m);
		EventDispatcher.fireEvent(Events.MESSAGE_RECEIVED, m);
	}

	@Override
	public void onFileSendRequestMessage(FileSendRequestMessage m) {
		EventDispatcher.fireEvent(Events.MESSAGE_FILE_REQUEST_RECEIVED, m);
	}

	@Override
	public void onFileSendAcceptMessage(FileSendAcceptMessage m) {
		EventDispatcher.fireEvent(Events.MESSAGE_FILE_ACCEPT_RECEIVED, m);
	}

	@Override
	public void onFileSendRejectMessage(FileSendRejectMessage m) {
		EventDispatcher.fireEvent(Events.MESSAGE_FILE_REJECT_RECEIVED, m);
	}

	@Override
	public void onUserAdded(List<User> user) {
		EventDispatcher.fireEvent(Events.USER_ONLINE_NOTICE, user);
	}

	@Override
	public void onUserRemoved(List<User> user) {
		EventDispatcher.fireEvent(Events.USER_OFFLINE_NOTICE, user);
	}

}
