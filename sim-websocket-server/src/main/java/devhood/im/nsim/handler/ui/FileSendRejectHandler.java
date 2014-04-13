package devhood.im.nsim.handler.ui;

import org.atmosphere.cpr.Broadcaster;

import devhood.im.nsim.model.FileSendReject;
import devhood.im.nsim.model.Message;
import devhood.im.sim.messages.MessagingException;
import devhood.im.sim.messages.interfaces.FileMessageHandler;

public class FileSendRejectHandler implements IEventHandler {
	private Broadcaster broadcaster;

	private FileMessageHandler fileMessageHandler;

	public FileSendRejectHandler(Broadcaster broadcaster,
			FileMessageHandler fileMessageHandler) {
		this.broadcaster = broadcaster;
		this.fileMessageHandler = fileMessageHandler;
	}

	@Override
	public void handle(Message m) {
		FileSendReject msg = (FileSendReject) m;

		try {
			fileMessageHandler
					.rejectFileMessage(msg.getId(), msg.getReceiver());
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
