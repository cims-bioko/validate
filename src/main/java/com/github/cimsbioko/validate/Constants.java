package com.github.cimsbioko.validate;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Constants {

    public final static Class[] CORE_SERVICE_CLASSES = {
            org.javarosa.core.services.locale.ResourceFileDataSource.class,
            org.javarosa.core.services.locale.TableLocaleSource.class
    };

    public final static Class[] CORE_MODEL_CLASSES = {
            org.javarosa.core.model.FormDef.class,
            org.javarosa.core.model.SubmissionProfile.class,
            org.javarosa.core.model.QuestionDef.class,
            org.javarosa.core.model.GroupDef.class,
            org.javarosa.core.model.instance.FormInstance.class,
            org.javarosa.core.model.data.BooleanData.class,
            org.javarosa.core.model.data.DateData.class,
            org.javarosa.core.model.data.DateTimeData.class,
            org.javarosa.core.model.data.DecimalData.class,
            org.javarosa.core.model.data.GeoPointData.class,
            org.javarosa.core.model.data.GeoShapeData.class,
            org.javarosa.core.model.data.GeoTraceData.class,
            org.javarosa.core.model.data.IntegerData.class,
            org.javarosa.core.model.data.LongData.class,
            org.javarosa.core.model.data.MultiPointerAnswerData.class,
            org.javarosa.core.model.data.PointerAnswerData.class,
            org.javarosa.core.model.data.SelectMultiData.class,
            org.javarosa.core.model.data.SelectOneData.class,
            org.javarosa.core.model.data.StringData.class,
            org.javarosa.core.model.data.TimeData.class,
            org.javarosa.core.model.data.UncastData.class,
            org.javarosa.core.model.data.helper.BasicDataPointer.class,
            org.javarosa.core.model.actions.SetValueAction.class
    };

    public static final String[] JR_CLASS_NAMES = Stream.of(CORE_SERVICE_CLASSES, CORE_MODEL_CLASSES)
            .flatMap(Arrays::stream)
            .map(Class::getCanonicalName)
            .collect(Collectors.toList())
            .toArray(new String[]{});
}
