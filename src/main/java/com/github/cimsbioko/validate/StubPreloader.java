package com.github.cimsbioko.validate;

import org.javarosa.core.model.data.IAnswerData;
import org.javarosa.core.model.instance.TreeElement;
import org.javarosa.core.model.utils.IPreloadHandler;

public class StubPreloader implements IPreloadHandler {

    public boolean handlePostProcess(TreeElement node, String params) {
        return false;
    }

    public IAnswerData handlePreload(String params) {
        return null;
    }

    public String preloadHandled() {
        return "property";
    }

}