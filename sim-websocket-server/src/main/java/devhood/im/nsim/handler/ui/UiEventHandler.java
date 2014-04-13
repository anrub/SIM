package devhood.im.nsim.handler.ui;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.atmosphere.cpr.Broadcaster;
import org.springframework.context.ApplicationContext;

import devhood.im.nsim.model.FileSendAccept;
import devhood.im.nsim.model.FileSendReject;
import devhood.im.nsim.model.GetContent;
import devhood.im.nsim.model.GetUserlist;
import devhood.im.nsim.model.Message;
import devhood.im.nsim.model.SendMessage;
import devhood.im.nsim.model.UserUpdateNotice;
import devhood.im.sim.messages.MessageContext;
import devhood.im.sim.messages.interfaces.FileMessageHandler;
import devhood.im.sim.service.interfaces.UserService;

/**
 * Behandlet Messages, die von der UI kommen.
 * 
 * @author flo
 * 
 */
public class UiEventHandler implements IUiEventHandler {

	private final Logger logger = Logger.getLogger(getClass());

	private Map<Class, IUiEventHandler> map = new HashMap<Class, IUiEventHandler>();

	private Broadcaster broadcaster;

	public UiEventHandler(Broadcaster broadcaster,
			ApplicationContext applicationContext) {
		UserService userService = applicationContext.getBean(UserService.class);
		this.broadcaster = broadcaster;

		map.put(GetUserlist.class, new GetUserlistHandler(broadcaster,
				userService));
		map.put(SendMessage.class, new SendMessageHandler());
		map.put(UserUpdateNotice.class,
				new UserUpdateNoticeHandler(userService));
		map.put(GetContent.class, new GetContentHandler(broadcaster,
				applicationContext.getBean(MessageContext.class)));
		map.put(FileSendAccept.class, new FileSendAcceptHandler(broadcaster,
				applicationContext.getBean(FileMessageHandler.class)));
		map.put(FileSendReject.class, new FileSendRejectHandler(broadcaster,
				applicationContext.getBean(FileMessageHandler.class)));
	}

	public void handle(Message m) {
		logger.info(String.format("Client Message empfangen: %s", m));

		IUiEventHandler h = map.get(m.getClass());
		if (h != null) {
			h.handle(m);
		} else {
			new UmbrellaUiHandler(broadcaster).handle(m);
		}
	}

}
