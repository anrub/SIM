package devhood.im.nsim.handler.ui;

import org.apache.log4j.Logger;

import devhood.im.nsim.model.Message;
import devhood.im.nsim.model.UserUpdateNotice;
import devhood.im.sim.messages.model.UserStatusMessage;
import devhood.im.sim.model.UserStatus;
import devhood.im.sim.service.interfaces.UserService;
import devhood.im.sim.ui.event.EventDispatcher;
import devhood.im.sim.ui.event.Events;

public class UserUpdateNoticeHandler implements IUiEventHandler {
	
	private Logger log = Logger.getLogger(UserUpdateNoticeHandler.class);

	private UserService userService;

	public UserUpdateNoticeHandler(UserService userService) {
		this.userService = userService;
	}

	@Override
	public void handle(Message m) {
		
		UserUpdateNotice n = (UserUpdateNotice) m;

		userService.getCurrentUser().setStatusType(
				UserStatus.valueOf(n.getUpdate()));

		devhood.im.sim.messages.model.Message msg = new UserStatusMessage(
				UserStatus.valueOf(n.getUpdate()));
		EventDispatcher.fireEvent(Events.SEND_MESSAGE, msg);
	}

}
