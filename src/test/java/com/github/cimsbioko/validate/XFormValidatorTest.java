package com.github.cimsbioko.validate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.*;

public class XFormValidatorTest {

    private XFormValidator service;
    private ResultBuilder handler;

    @Before
    public void setup() {
        handler = new ResultBuilder();
        service = new XFormValidator(handler);
    }

    @After
    public void tearDown() {
        service = null;
        handler = null;
    }

    @Test
    public void nullHandler() {
        new XFormValidator(null).validateStream(null);
    }

    @Test
    public void nullStream() {
        service.validateStream(null);
        Result result = handler.buildResult();
        assertNotNull(result);
        assertTrue(result.hasFailed());
        assertTrue(result.getMessages().stream().anyMatch(m -> m.toString().contains("failed to read form: no data")));
    }

    @Test
    public void nullBytes() {
        service.validateBytes(null);
        Result result = handler.buildResult();
        assertNotNull(result);
        assertTrue(result.hasFailed());
        assertTrue(result.getMessages().stream().anyMatch(m -> m.toString().contains("failed to read form: no data")));
    }

    @Test
    public void emptyStream() {
        service.validateStream(new ByteArrayInputStream("".getBytes()));
        Result result = handler.buildResult();
        assertNotNull(result);
        assertTrue(result.hasFailed());
        assertFalse(result.getMessages().isEmpty());
        assertTrue(result.getMessages().stream().anyMatch(m -> m.toString().contains("failed to read form: invalid xml")));
    }

    @Test
    public void invalidXml() {
        service.validateStream(loadTestForm("simplest-broken-xml"));
        Result result = handler.buildResult();
        assertNotNull(result);
        assertTrue(result.hasFailed());
        assertFalse(result.getMessages().isEmpty());
        assertTrue(result.getMessages().stream().anyMatch(m -> m.toString().contains("failed to read form: invalid xml")));
    }

    @Test
    public void simplestValid() {
        service.validateStream(loadTestForm("simplest"));
        Result result = handler.buildResult();
        assertFalse(result.hasFailed());
        assertTrue(result.getMessages().stream().anyMatch(m -> m.toString().contains("xform is valid")));
    }

    @Test
    public void simplestInvalid() {
        service.validateStream(loadTestForm("simplest-broken"));
        Result result = handler.buildResult();
        assertTrue(result.hasFailed());
        assertTrue(result.getMessages().stream().anyMatch(m -> m.toString().contains("xform is invalid")));
        assertTrue(result.getMessages().stream().anyMatch(m -> m.toString().contains("problem with readonly condition")));
    }

    @Test
    public void emptyGroup() {
        service.validateStream(loadTestForm("empty-group"));
        Result result = handler.buildResult();
        assertTrue(result.hasFailed());
        assertTrue(result.getMessages().stream().anyMatch(m -> m.toString().contains("xform is invalid")));
        assertTrue(result.getMessages().stream().anyMatch(m -> m.toString().contains("group /data/things is empty")));
    }

    @Test
    public void unlabeledChoice() {
        service.validateStream(loadTestForm("unlabeled-choice"));
        Result result = handler.buildResult();
        assertFalse(result.hasFailed());
        assertTrue(result.getMessages().stream().anyMatch(m -> m.toString().contains("xform is valid")));
        assertTrue(result.getMessages().stream().anyMatch(m -> m.toString().contains("neither label nor image for choice 1 of question /data/select")));
    }

    @Test
    public void choiceWithoutValue() {
        service.validateStream(loadTestForm("choice-without-value"));
        Result result = handler.buildResult();
        assertTrue(result.hasFailed());
        assertTrue(result.getMessages().stream().anyMatch(m -> m.toString().contains("xform is invalid")));
        assertTrue(result.getMessages().stream().anyMatch(m -> m.toString().contains("no value for choice 1 of question /data/select")));
    }

    @Test
    public void selectWithoutChoices() {
        service.validateStream(loadTestForm("select-without-choices"));
        Result result = handler.buildResult();
        assertTrue(result.hasFailed());
        assertTrue(result.getMessages().stream().anyMatch(m -> m.toString().contains("xform is invalid")));
        assertTrue(result.getMessages().stream().anyMatch(m -> m.toString().contains("Select question has no choices")));
    }

    private InputStream loadTestForm(String name) {
        return XFormValidatorTest.class.getResourceAsStream(String.format("/%s.xml", name));
    }
}
