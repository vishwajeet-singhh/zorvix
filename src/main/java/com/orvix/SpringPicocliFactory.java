package com.orvix;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import picocli.CommandLine;
import picocli.CommandLine.IFactory;

/**
 * Picocli {@link IFactory} backed by the Spring {@link ApplicationContext}, so commands and
 * their collaborators are resolved as Spring beans (constructor injection) rather than being
 * instantiated by picocli's default reflective factory.
 *
 * <p>Deliberately avoids {@code picocli-spring-boot-starter} to keep Orvix decoupled from that
 * starter's Spring Boot version coupling.
 */
@Component
public class SpringPicocliFactory implements IFactory {

    private final ApplicationContext applicationContext;
    private final IFactory fallback = CommandLine.defaultFactory();

    public SpringPicocliFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public <K> K create(Class<K> cls) throws Exception {
        try {
            return applicationContext.getBean(cls);
        } catch (NoSuchBeanDefinitionException ex) {
            // Not a managed bean (e.g. picocli's own internal types) — fall back to default.
            return fallback.create(cls);
        }
    }
}
