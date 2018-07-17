package br.gov.sibbr.api.integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

/**
 * Configuration properties security aplication
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    /**
     * Path file of mensagens properties
     */
    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

        messageSource.setBasenames("classpath:/messages/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(1200);

        return messageSource;
    }

    /**
     * Configuration cookie for store of files message properties
     */
    @Bean
    public CookieLocaleResolver localeResolver() {
        CookieLocaleResolver localeResolver = new CookieLocaleResolver();

        localeResolver.setDefaultLocale(Locale.ROOT); //new Locale("pt","BR")
        localeResolver.setCookieName("messages");
        localeResolver.setCookieName("messages");
        localeResolver.setCookieMaxAge(3600);

        return localeResolver;
    }

    /**
     * Set interceptor de locale for target of files message,
     * permissio the use internationalization od message
     */
    @Bean
    public LocaleChangeInterceptor localeInterceptor() {
        LocaleChangeInterceptor changeInterceptor = new LocaleChangeInterceptor();
        changeInterceptor.setParamName("lang");
        return changeInterceptor;
    }

    /**
     * Configuration interceptadores for register of locale
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeInterceptor());
    }

    /**
     * Configuration Content Negotiation for strategy of acceptable mediaType
     * both strategies (extension and parameter) enabled
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false)
                .favorParameter(true)
                .useJaf(false)
                .parameterName("mediaType")
                .defaultContentType(MediaType.APPLICATION_JSON)
                .mediaType("xml", MediaType.APPLICATION_XML)
                .mediaType("pdf", MediaType.APPLICATION_PDF)
                .mediaType("html", MediaType.TEXT_HTML)
                .mediaType("text", MediaType.TEXT_PLAIN)
                .mediaType("json", MediaType.APPLICATION_JSON);
    }
}