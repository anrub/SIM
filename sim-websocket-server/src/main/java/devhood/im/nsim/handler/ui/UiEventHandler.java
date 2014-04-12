package devhood.im.nsim.handler.ui;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.atmosphere.cpr.Broadcaster;
import org.springframework.context.ApplicationContext;

import devhood.im.nsim.model.GetUserlist;
import devhood.im.nsim.model.Message;
import devhood.im.nsim.model.SendMessage;
import devhood.im.nsim.model.UserUpdateNotice;
import devhood.im.sim.service.interfaces.UserService;

/**
 * Behandlet Messages, die von der UI kommen.
 * 
 * @author flo
 * 
 */
public class UiEventHandler implements IEventHandler {

	private final Logger logger = Logger.getLogger(getClass());

	private Map<Class, IEventHandler> map = new HashMap<Class, IEventHandler>();

	public UiEventHandler(Broadcaster broadcaster,
			ApplicationContext applicationContext) {
		UserService userService = applicationContext.getBean(UserService.class);
		map.put(GetUserlist.class, new GetUserlistHandler(broadcaster,
				userService));
		map.put(SendMessage.class, new SendMessageHandler());
		map.put(UserUpdateNotice.class,
				new UserUpdateNoticeHandler(userService));
	}

	public void handle(Message m) {
		logger.info(String.format("Client Message empfangen: %s", m));

		IEventHandler h = map.get(m.getClass());
		if (h != null) {
			h.handle(m);
		}
	}

}
