package org.noint.pickminbloom.exception.mail;

import org.noint.pickminbloom.exception.PickminBloomException;

public class MessagingBuildException extends PickminBloomException {
    public MessagingBuildException(String felid) {
        super(500, felid, "Message Build Failed");
    }
}
