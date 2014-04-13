package devhood.im.nsim.handler.sim;

import java.io.IOException;

import org.atmosphere.cpr.Broadcaster;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class SimpleBroadcaster {
	private Broadcaster broadcaster;

	public SimpleBroadcaster(Broadcaster broadcaster) {
		this.broadcaster = broadcaster;
	}

	public void broadcast(Object toSend) {
		ObjectMapper om = new ObjectMapper();

		try {
			broadcaster.broadcast(om.writeValueAsString(toSend));
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}