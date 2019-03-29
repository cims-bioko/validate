package com.github.cimsbioko.validate;

import org.javarosa.core.model.FormDef;
import org.javarosa.core.model.FormIndex;
import org.javarosa.core.model.GroupDef;
import org.javarosa.core.model.SelectChoice;
import org.javarosa.core.model.instance.InstanceInitializationFactory;
import org.javarosa.core.services.PropertyManager;
import org.javarosa.core.services.PrototypeManager;
import org.javarosa.form.api.FormEntryCaption;
import org.javarosa.form.api.FormEntryController;
import org.javarosa.form.api.FormEntryModel;
import org.javarosa.form.api.FormEntryPrompt;
import org.javarosa.model.xform.XFormsModule;
import org.javarosa.xform.parse.XFormParseException;
import org.javarosa.xform.util.XFormUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.commons.io.IOUtils.toByteArray;
import static org.javarosa.core.model.Constants.CONTROL_SELECT_MULTI;
import static org.javarosa.core.model.Constants.CONTROL_SELECT_ONE;
import static org.javarosa.form.api.FormEntryController.*;

public class XFormValidator {

    private final StatusHandler statusHandler;

    public XFormValidator(StatusHandler handler) {
        statusHandler = handler == null ? new LoggingStatusHandler() : handler;
    }

    public void validateStream(InputStream xmlStream) {
        try {
            if (xmlStream == null) {
                statusHandler.error("failed to read form: no data");
            } else {
                validateBytes(toByteArray(xmlStream));
            }
        } catch (IOException e) {
            statusHandler.error("failed to read form: input error", e);
        }
    }

    public void validateBytes(byte[] xmlBytes) {
        if (xmlBytes == null) {
            statusHandler.error("failed to read form: no data");
        } else {
            validateXml(xmlBytes);
            if (!statusHandler.hasFailed()) {
                validateXForm(xmlBytes);
            }
        }
    }

    public void validateXml(byte[] formBytes) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(null); // squelch errors sent to std err/out
            builder.parse(new ByteArrayInputStream(formBytes));
        } catch (SAXException e) {
            statusHandler.error("failed to read form: invalid xml", e);
        } catch (ParserConfigurationException e) {
            statusHandler.error("failed to read form: problem with xml parser configuration", e);
        } catch (IOException e) {
            statusHandler.error("failed to read form: xml input error", e);
        }
    }

    public void validateXForm(byte[] xmlBytes) {

        PrototypeManager.registerPrototypes(Constants.JR_CLASS_NAMES);

        new XFormsModule().registerModule();

        PropertyManager.setPropertyManager(new StubPropertyManager());

        try {
            FormDef formDef = XFormUtils.getFormFromInputStream(new ByteArrayInputStream(xmlBytes));
            if (formDef == null) {
                statusHandler.error("failed to read form: parser returned null form");
            } else {

                // stub javarosa functions to prevent failures while traversing
                formDef.getPreloader().addPreloadHandler(new StubPreloader());
                formDef.getEvaluationContext().addFunctionHandler(new StubFunctionHandler());
                formDef.initialize(true, new InstanceInitializationFactory());

                statusHandler.info("well-formed xform, parsed successfully");

                FormEntryModel entryModel = new FormEntryModel(formDef);
                FormIndex formIndex = FormIndex.createBeginningOfFormIndex();
                Set<String> visitedRepeats = new HashSet<>();

                // traverse the form structure
                int event;
                do {
                    formIndex = entryModel.incrementIndex(formIndex);
                    event = entryModel.getEvent(formIndex);
                    switch (event) {
                        case EVENT_PROMPT_NEW_REPEAT:
                            String repeatPath = cleanPath(formIndex);
                            if (!visitedRepeats.contains(repeatPath)) {
                                visitedRepeats.add(repeatPath);
                                entryModel.getForm().createNewRepeat(formIndex);
                                formIndex = entryModel.getFormIndex();
                            }
                            break;
                        case EVENT_GROUP:
                            GroupDef groupDef = (GroupDef) entryModel.getForm().getChild(formIndex);
                            if (groupDef.getChildren() == null || groupDef.getChildren().size() == 0) {
                                String groupPath = cleanPath(formIndex);
                                statusHandler.error(String.format("group %s is empty", groupPath));
                            }
                            break;
                        case EVENT_QUESTION:
                            FormEntryPrompt questionPrompt = entryModel.getQuestionPrompt(formIndex);
                            int controlType = questionPrompt.getControlType();
                            boolean questionHasChoices = controlType == CONTROL_SELECT_MULTI || controlType == CONTROL_SELECT_ONE;
                            if (questionHasChoices) {
                                String questionPath = cleanPath(formIndex);
                                List<SelectChoice> items = questionPrompt.getSelectChoices();
                                for (int i = 0; i < items.size(); ++i) {
                                    SelectChoice choice = items.get(i);
                                    String label = questionPrompt.getSelectChoiceText(choice);
                                    String image = questionPrompt.getSpecialFormSelectChoiceText(choice, FormEntryCaption.TEXT_FORM_IMAGE);
                                    if ((label == null || label.trim().length() == 0)
                                            && (image == null || image.trim().length() == 0)) {
                                        statusHandler.warn(
                                                String.format("neither label nor image for choice %d of question %s",
                                                        i + 1, questionPath));
                                    }
                                    if (choice.getValue() == null || choice.getValue().trim().length() == 0) {
                                        statusHandler.error("no value for choice " + (i + 1) + " of question " + questionPath);
                                    }
                                }
                            }
                            break;
                    }
                } while (event != FormEntryController.EVENT_END_OF_FORM);

                if (statusHandler.hasFailed()) {
                    statusHandler.error("xform is invalid");
                } else {
                    statusHandler.info("xform is valid");
                }
            }
        } catch (XFormParseException e) {
            statusHandler.error("xform is invalid: parse failed", e);
        } catch (Exception e) {
            statusHandler.error("xform appears invalid: general failure during validation", e);
        }
    }

    private String cleanPath(FormIndex index) {
        return index.getReference().toString().replaceAll("\\[\\d+\\]", "");
    }
}
