package io.github.dilluter.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Locale;

@Configuration
public class InternacionalizacaoConfig {
    @Bean
    public MessageSource messageSource(){
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setDefaultEncoding("ISO-8859-1");
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultLocale(Locale.getDefault());
        return messageSource;
    }
    @Bean
    public LocalValidatorFactoryBean validatorFactoryBean() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }
}
