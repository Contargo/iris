package net.contargo.iris;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * Bean representing a Message.
 *
 * @author  Marc Kannegiesser - kannegiesser@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 * @author  Arnold Franke - franke@synyx.de
 */
public final class Message {

    public static enum MessageType {

        SUCCESS,
        ERROR,
        WARNING
    }

    private final String message;
    private final MessageType type;

    public Message(String message, MessageType type) {

        this.message = message;
        this.type = type;
    }

    public MessageType getType() {

        return type;
    }


    public String getMessage() {

        return message;
    }


    public static Message error(String message) {

        return new Message(message, MessageType.ERROR);
    }


    public static Message success(String message) {

        return new Message(message, MessageType.SUCCESS);
    }


    public static Message warning(String message) {

        return new Message(message, MessageType.WARNING);
    }


    @Override
    public String toString() {

        return new ToStringBuilder(this).append("type", type).append("message", message).toString();
    }
}
