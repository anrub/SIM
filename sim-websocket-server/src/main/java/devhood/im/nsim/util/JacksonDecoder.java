package devhood.im.nsim.util;

import java.io.IOException;

import org.atmosphere.config.managed.Decoder;
import org.codehaus.jackson.map.ObjectMapper;

import devhood.im.nsim.model.Message;


public class JacksonDecoder implements Decoder<String, Message> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Message decode(String s) {
        try {
            return mapper.readValue(s, Message.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}