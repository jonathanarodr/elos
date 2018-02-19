package br.com.elos.route;

import br.com.elos.App;

import java.lang.reflect.Method;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.HandlesTypes;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.ws.rs.Path;

@HandlesTypes({Controller.class})
public class RouteInitializer implements ServletContainerInitializer {
    
    @Override
    public void onStartup (Set<Class<?>> classes, ServletContext context) throws ServletException {
        String parameter = context.getInitParameter("elos");

        if (parameter == null) {
            return;
        }

        try {
            App app = App.getInstance(parameter);
            app.writeProperties();
            
            List<String> routes = new ArrayList<>();
            
            if (classes != null) {
                for (Class<?> classe : classes) {
                    if (!classe.isAnnotationPresent(Controller.class)) {
                        continue;
                    }
            
                    //captura classes com anotação @Controller
                    Method[] methods = classe.getMethods();
                    String mainpath = classe.getAnnotation(Controller.class).value();
                    
                    for (Method method : methods) {
                        if (!method.isAnnotationPresent(Path.class)) {
                            continue;
                        }
                        
                        String path = this.pathBuilder(mainpath, method.getAnnotation(Path.class).value());
                        routes.add(path);
                        System.out.println("Mapping route " + path);
                    }
                }
            }
            
            //confiura base url
            context.setAttribute("url", app.server_url);
            
            //configura mapeamento de rotas
            if (routes.size() > 0) {
                context.setAttribute("routes", routes);
                ServletRegistration.Dynamic servletRegistration = context.addServlet("route", Route.class);
                servletRegistration.setLoadOnStartup(1);
                routes.forEach(value -> {
                    servletRegistration.addMapping(value);
                });
            }
        } catch (Throwable ex) {
            throw new ServletException("Failed to route initializer", ex);
        }
    }
    
    private String pathParam(String path) {
        if (!path.contains("{")) {
            return path;
        }
        
        String[] pathSplit = path.split("/");
        
        for (int i=0; i<pathSplit.length; i++) {
            if ((pathSplit[i].startsWith("{")) && (pathSplit[i].endsWith("}"))) {
                pathSplit[i] = "*";
            }
        }
        
        return String.join("/", pathSplit);
    }
    
    private String pathBuilder(String mainpath, String path) {
        mainpath = this.pathParam(mainpath);
        path = this.pathParam(path);
        
        if (path.equals("/")) {
            return mainpath;
        }
        
        if (path.startsWith("/")) {
            return path;
        }
        
        return (mainpath.endsWith("/")) ? mainpath + path : mainpath + "/" + path;
    }
    
}
