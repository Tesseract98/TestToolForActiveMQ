package main.templater;

import main.exceptions.MyExceptions;
import org.mvel2.templates.CompiledTemplate;

public class Generator {

    private final CompiledTemplate templateOfMessage;
    private final Mvel mvel;

    public Generator(CompiledTemplate templateOfMessage, Mvel mvel) {
        this.templateOfMessage = templateOfMessage;
        this.mvel = mvel;
    }

    public String createMessage() throws MyExceptions {
        return mvel.getContent(templateOfMessage);
    }

}
