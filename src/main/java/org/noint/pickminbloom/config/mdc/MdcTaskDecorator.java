package org.noint.pickminbloom.config.mdc;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

public class MdcTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return () -> {
            if (contextMap != null) MDC.setContextMap(contextMap);
            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}