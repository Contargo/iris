package net.contargo.iris;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class MessageUnitTest {

    private static final String MESSAGE = "message";

    @Test
    public void createSuccessMessage() {

        Message success = Message.success(MESSAGE);
        assertThat(success.getMessage(), is(MESSAGE));
        assertThat(success.getType(), is(Message.MessageType.SUCCESS));
    }


    @Test
    public void createErrorMessage() {

        Message error = Message.error(MESSAGE);
        assertThat(error.getMessage(), is(MESSAGE));
        assertThat(error.getType(), is(Message.MessageType.ERROR));
    }


    @Test
    public void createWarningMessage() {

        Message warning = Message.warning(MESSAGE);
        assertThat(warning.getMessage(), is(MESSAGE));
        assertThat(warning.getType(), is(Message.MessageType.WARNING));
    }


    @Test
    public void toStringTest() {

        String message = Message.success(MESSAGE).toString();
        assertThat(message, endsWith("[type=SUCCESS,message=message]"));
    }
}
