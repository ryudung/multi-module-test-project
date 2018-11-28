package com.ryudung.api.doc;

import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.util.StringUtils;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.snippet.Attributes.key;


public class Constraints {
    private ConstraintDescriptions constraintDescriptions;

    public Constraints(Class<?> input) {
        this.constraintDescriptions = new ConstraintDescriptions(input, new CustomConstraintResolver());
    }

    public ParameterDescriptor withParameter(String path) {
        return parameterWithName(path)
                .attributes(key("constraints").value(
                        StringUtils.collectionToDelimitedString(
                                this.constraintDescriptions.descriptionsForProperty(path), ".  ")
                        )
                );
    }

    public FieldDescriptor withPath(String path) {
        return fieldWithPath(path)
                .attributes(key("constraints").value(
                        StringUtils.collectionToDelimitedString(
                                this.constraintDescriptions.descriptionsForProperty(path), ".  ")
                        )
                );
    }
}
