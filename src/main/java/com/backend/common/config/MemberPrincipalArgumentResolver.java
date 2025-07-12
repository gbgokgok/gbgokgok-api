package com.backend.common.config;

import com.backend.controller.cookie.CookieResolver;
import com.backend.domain.member.Member;
import com.backend.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
@Component
public class MemberPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private final CookieResolver cookieResolver;
    private final MemberService memberService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Member.class.isAssignableFrom(parameter.getParameterType())
                && parameter.hasParameterAnnotation(MemberPrincipal.class);
    }

    @Override
    public Member resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        cookieResolver.checkLoginRequired(request);
        String token = cookieResolver.extractAccessToken(request);
        return memberService.getAuthMember(token);
    }
}
