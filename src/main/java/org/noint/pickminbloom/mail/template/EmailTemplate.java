package org.noint.pickminbloom.mail.template;

import jakarta.activation.DataSource;
import jakarta.mail.util.ByteArrayDataSource;

public abstract class EmailTemplate<T> {
    public abstract String build(T event);

    public DataSource bindImg(byte[] img) {
        return new ByteArrayDataSource(img, "image/png");
    }
}
