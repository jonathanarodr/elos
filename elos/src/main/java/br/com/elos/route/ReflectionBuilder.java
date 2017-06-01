package br.com.elos.route;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

public class ReflectionBuilder {
    
    private final String resourcePath;
    
    protected ReflectionBuilder(String resourcePath) {
        this.resourcePath = resourcePath;
    }
    
    protected Reflections build() {
        return new Reflections(new ConfigurationBuilder()
            .setUrls(ClasspathHelper.forPackage(this.resourcePath))
            .filterInputsBy(new FilterBuilder().includePackage(this.resourcePath))
            .setScanners(new SubTypesScanner(false)
                        ,new TypeAnnotationsScanner()
                        ,new MethodAnnotationsScanner()));
    }    
    
}
