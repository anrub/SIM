package devhood.im.sim.messages.observer;

import devhood.im.sim.messages.model.FileSendAcceptMessage;
import devhood.im.sim.messages.model.FileSendRejectMessage;
import devhood.im.sim.messages.model.FileSendRequestMessage;
import devhood.im.sim.messages.model.Message;

public class MessageObserverAdapter implements MessageObserver {

	@Override
	public void onMessage(Message m) {

	}

	@Override
	public void onFileSendRequestMessage(FileSendRequestMessage m) {

	}

	@Override
	public void onFileSendAcceptMessage(FileSendAcceptMessage m) {

	}

	@Override
	public void onFileSendRejectMessage(FileSendRejectMessage m) {
	}

}
