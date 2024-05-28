package com.morteza.assignment.favoriterecipes.logging;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Optional;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.morteza.assignment.favoriterecipes.controller..*(..))")
    public void logBeforeApiCall(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication != null && authentication.isAuthenticated())
                ? authentication.getName()
                : "Anonymous";

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String endpoint = Optional.ofNullable(request.getRequestURI()).orElse("UNKNOWN");
        String method = request.getMethod();

        logger.info("API called by user: {} - Method: {} - Endpoint: {}", username, method, endpoint);
    }

    private String getHttpMethod(Method method) {
        if (method.isAnnotationPresent(GetMapping.class)) {
            return "GET";
        } else if (method.isAnnotationPresent(PostMapping.class)) {
            return "POST";
        } else if (method.isAnnotationPresent(PutMapping.class)) {
            return "PUT";
        } else if (method.isAnnotationPresent(DeleteMapping.class)) {
            return "DELETE";
        } else if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            RequestMethod[] methods = requestMapping.method();
            if (methods.length > 0) {
                return methods[0].name();
            }
        }
        return "UNKNOWN";
    }

    private String getEndpoint(Method method) {
        if (method.isAnnotationPresent(GetMapping.class)) {
            GetMapping getMapping = method.getAnnotation(GetMapping.class);
            return getMapping.value().length > 0 ? getMapping.value()[0] : "UNKNOWN";
        } else if (method.isAnnotationPresent(PostMapping.class)) {
            PostMapping postMapping = method.getAnnotation(PostMapping.class);
            return postMapping.value().length > 0 ? postMapping.value()[0] : "UNKNOWN";
        } else if (method.isAnnotationPresent(PutMapping.class)) {
            PutMapping putMapping = method.getAnnotation(PutMapping.class);
            return putMapping.value().length > 0 ? putMapping.value()[0] : "UNKNOWN";
        } else if (method.isAnnotationPresent(DeleteMapping.class)) {
            DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
            return deleteMapping.value().length > 0 ? deleteMapping.value()[0] : "UNKNOWN";
        } else if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            return requestMapping.value().length > 0 ? requestMapping.value()[0] : "UNKNOWN";
        }
        return "UNKNOWN";
    }
}


