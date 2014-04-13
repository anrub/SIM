package devhood.im.nsim.handler.ui;

import org.atmosphere.cpr.Broadcaster;

import devhood.im.nsim.handler.sim.SimpleBroadcaster;
import devhood.im.nsim.model.FileSendAccept;
import devhood.im.nsim.model.Message;
import devhood.im.sim.messages.MessagingException;
import devhood.im.sim.messages.interfaces.FileMessageHandler;

public class FileSendAcceptHandler implements IEventHandler {
	private Broadcaster broadcaster;

	private FileMessageHandler fileMessageHandler;

	public FileSendAcceptHandler(Broadcaster broadcaster,
			FileMessageHandler fileMessageHandler) {
		this.broadcaster = broadcaster;
		this.fileMessageHandler = fileMessageHandler;
	}

	@Override
	public void handle(Message m) {
		FileSendAccept msg = (FileSendAccept) m;
		try {
			String path = msg.getPath();
			if (path == null) {
				path = System.getProperty("user.home") + "/Downloads/";
			}
			fileMessageHandler.acceptFileMessage(m.getReceiver(), msg.getId(),
					path);
			Message showMsg = new Message();
			showMsg.setSender("SIM");
			showMsg.setText("Starte Filetranser..nach " + path);
			new SimpleBroadcaster(broadcaster).broadcast(showMsg);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
