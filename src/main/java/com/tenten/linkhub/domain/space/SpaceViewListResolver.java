package com.tenten.linkhub.domain.space;

import com.tenten.linkhub.domain.space.util.SpaceViewList;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.util.WebUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Component
public class SpaceViewListResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasParameterAnnotation = parameter.hasParameterAnnotation(SpaceViewList.class);
        boolean equals = parameter.getParameterType().equals(List.class);
        return hasParameterAnnotation && equals;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Cookie spaceViewCookie = WebUtils.getCookie(webRequest.getNativeRequest(HttpServletRequest.class), "spaceView");

        if (spaceViewCookie == null){
            return new ArrayList<>();
        }

        String spaceViewValue = spaceViewCookie.getValue();
        spaceViewValue = spaceViewValue.replaceAll("\\[|\\]|\\s", "");
        String[] spaceViewArr = spaceViewValue.split("_");

        return Stream.of(spaceViewArr)
                .map(s -> Long.parseLong(s))
                .toList();
    }

}
