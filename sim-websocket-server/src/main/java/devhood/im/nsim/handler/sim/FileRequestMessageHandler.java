package devhood.im.nsim.handler.sim;

import org.atmosphere.cpr.Broadcaster;

import devhood.im.nsim.model.FileSendRequest;
import devhood.im.nsim.util.SimpleBroadcaster;
import devhood.im.sim.event.Events;
import devhood.im.sim.messages.model.FileSendRequestMessage;

/**
 * Verarbeitet {@link FileSendRequestMessage} aus dem core, transformiert Sie in
 * UI Message und pusht weiter an UI.
 * 
 * @author flo
 * 
 */
public class FileRequestMessageHandler implements ISimEventHandler {

	private Broadcaster broadcaster;

	public FileRequestMessageHandler(Broadcaster broadcaster) {
		this.broadcaster = broadcaster;
	}

	@Override
	public void handle(Events e, Object o) {
		FileSendRequestMessage msg = (FileSendRequestMessage) o;

		FileSendRequest req = new FileSendRequest();
		req.setFilename(msg.getFilename());
		req.setFiletype(msg.getFileType());
		req.setId(msg.getId());
		req.setReceiver(msg.getReceiver().get(0));
		req.setSender(msg.getSender());
		req.setSize(msg.getSize());
		req.setText(msg.getText());

		new SimpleBroadcaster(broadcaster).broadcast(req);
	}

}
