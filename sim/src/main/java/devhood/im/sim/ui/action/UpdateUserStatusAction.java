package devhood.im.sim.ui.action;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import devhood.im.sim.config.SimConfiguration;
import devhood.im.sim.messages.Message;
import devhood.im.sim.model.User;
import devhood.im.sim.model.UserStatus;
import devhood.im.sim.service.MessageFactory;
import devhood.im.sim.service.SimService;
import devhood.im.sim.service.interfaces.UserService;

@Named
public class UpdateUserStatusAction implements Action {

	private String statusText;
	@Inject
	private SimConfiguration simConfiguration;

	@Inject
	private UserService userService;

	@Inject
	private SimService simService;

	@Override
	public void execute() {
		UserStatus status = UserStatus.get(statusText);

		simConfiguration.getCurrentUser().setStatusType(status);
		Message statusMessage = MessageFactory.createUserStatusMessage(status);
		statusMessage.setSender(simConfiguration.getCurrentUser().getName());

		List<User> users = userService.getUsers();
		for (User user : users) {
			statusMessage.getReceiver().add(user.getName());
		}

		simService.sendMessage(statusMessage);
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

	public SimService getSimService() {
		return simService;
	}

	public void setSimService(SimService simService) {
		this.simService = simService;
	}

}
