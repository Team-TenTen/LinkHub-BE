package com.tenten.linkhub.global;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.type.StandardBasicTypes;

public class CustomFunctionContributor implements FunctionContributor {

    @Override
    public void contributeFunctions(FunctionContributions functionContributions) {
        functionContributions
                .getFunctionRegistry()
                .registerPattern("match_against", "match (?1) against (?2 in boolean mode)", functionContributions.getTypeConfiguration().getBasicTypeRegistry().resolve(StandardBasicTypes.DOUBLE));
    }

}
