package com.example.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null; // Əgər Database/Security konfiqurasiyan varsa, bura yazarsan
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] { WebConfig.class }; // Yuxarıda yaratdığımız WebConfig-i bura bağlayırıq
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" }; // Gələn bütün sorğuları ("/") DispatcherServlet qarşılasın
    }
}