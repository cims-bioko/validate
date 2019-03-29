package com.github.cimsbioko.validate;

import org.javarosa.core.model.condition.EvaluationContext;
import org.javarosa.core.model.condition.IFunctionHandler;

import java.util.ArrayList;
import java.util.List;

public class StubFunctionHandler implements IFunctionHandler {

    public String getName() {
        return "pulldata";
    }

    public List<Class[]> getPrototypes() {
        return new ArrayList<>();
    }

    public boolean rawArgs() {
        return true;
    }

    public boolean realTime() {
        return false;
    }

    public Object eval(Object[] args, EvaluationContext ec) {
        return args[0];
    }

}
