package devhood.im.nsim.util;

import java.io.IOException;

import org.atmosphere.cpr.Broadcaster;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Utility Broadcaster.
 * 
 * @author flo
 * 
 */
public class SimpleBroadcaster {
	private Broadcaster broadcaster;

	public SimpleBroadcaster(Broadcaster broadcaster) {
		this.broadcaster = broadcaster;
	}

	/**
	 * Sendet toSend als JSON String.
	 * 
	 * @param toSend
	 *            Obj.
	 */
	public void broadcast(Object toSend) {
		ObjectMapper om = new ObjectMapper();

		try {
			String value = om.writeValueAsString(toSend);
			broadcaster.broadcast(value);
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
