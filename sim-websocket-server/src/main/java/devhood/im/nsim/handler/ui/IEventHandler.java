package devhood.im.nsim.handler.ui;

import devhood.im.nsim.model.Message;

public interface IEventHandler {
	void handle(Message m);
}
