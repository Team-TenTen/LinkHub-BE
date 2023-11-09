package com.tenten.linkhub.domain.space;

import com.tenten.linkhub.domain.space.handler.dto.SpaceIncreaseViewCountDto;
import com.tenten.linkhub.domain.space.util.CheckSpaceView;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;

@RequiredArgsConstructor
public class SpaceViewCheckInterceptor implements HandlerInterceptor {
    private static final int COOKIE_EXPIRE_TIME = 60 * 60 * 24;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        if (!handlerMethod.getMethod().isAnnotationPresent(CheckSpaceView.class)) {
            return true;
        }

        Long spaceId = Long.parseLong(request.getRequestURI().split("/")[2]);
        Cookie spaceViewCookie = WebUtils.getCookie(request, "spaceView");
        checkSpaceViewCookie(spaceViewCookie, spaceId, response);

        return true;
    }

    private void checkSpaceViewCookie(Cookie spaceViewCookie, Long spaceId, HttpServletResponse response) {
        if (spaceViewCookie == null) {
            eventPublisher.publishEvent(
                    new SpaceIncreaseViewCountDto(spaceId)
            );

            Cookie newCookie = new Cookie("spaceView", "[" + spaceId + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(COOKIE_EXPIRE_TIME);
            response.addCookie(newCookie);
            return;
        }

        if (!spaceViewCookie.getValue().contains("[" + spaceId + "]")) {
            eventPublisher.publishEvent(
                    new SpaceIncreaseViewCountDto(spaceId)
            );

            spaceViewCookie.setValue(spaceViewCookie.getValue() + "_[" + spaceId + "]");
            spaceViewCookie.setPath("/");
            spaceViewCookie.setMaxAge(COOKIE_EXPIRE_TIME);
            response.addCookie(spaceViewCookie);
            return;
        }
    }

}
