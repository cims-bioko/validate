package com.github.cimsbioko.validate;

import java.util.List;

public interface Result {

    boolean hasFailed();

    List<Message> getMessages();
}
