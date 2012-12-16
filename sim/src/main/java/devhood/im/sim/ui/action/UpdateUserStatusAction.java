package devhood.im.sim.ui.action;

import javax.inject.Inject;
import javax.inject.Named;

import devhood.im.sim.config.SimConfiguration;
import devhood.im.sim.messages.MessageContext;
import devhood.im.sim.messages.MessageFactory;
import devhood.im.sim.messages.MessagingException;
import devhood.im.sim.messages.model.Message;
import devhood.im.sim.model.User;
import devhood.im.sim.model.UserStatus;
import devhood.im.sim.service.interfaces.UserService;

@Named
public class UpdateUserStatusAction implements Action {

	private String statusText;
	@Inject
	private SimConfiguration simConfiguration;

	@Inject
	private UserService userService;

	@Inject
	private MessageContext simService;

	@Override
	public void execute() {
		UserStatus status = UserStatus.get(statusText);

		userService.getCurrentUser().setStatusType(status);
		Message statusMessage = MessageFactory.createUserStatusMessage(status);
		statusMessage.setSender(userService.getCurrentUser().getName());

		Iterable<User> users = userService.getUsers();
		for (User user : users) {
			statusMessage.getReceiver().add(user.getName());
		}

		try {
			simService.sendMessage(statusMessage);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

	public SimConfiguration getSimConfiguration() {
		return simConfiguration;
	}

	public void setSimConfiguration(SimConfiguration simConfiguration) {
		this.simConfiguration = simConfiguration;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public MessageContext getSimService() {
		return simService;
	}

	public void setSimService(MessageContext simService) {
		this.simService = simService;
	}

}
