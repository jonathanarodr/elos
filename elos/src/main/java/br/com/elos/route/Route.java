package br.com.elos.route;

import br.com.elos.App;
import br.com.elos.serialization.Json;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.reflections.Reflections;

//@WebServlet(name = "route", urlPatterns = "/*")
public class Route extends HttpServlet {
    
    private Map<String,String> paramMap = new HashMap<String,String>();
    
    @Override
    protected void service(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws ServletException, IOException {

        try {
            //instancia classe de configurações
            App app = App.getInstance();
            
            //captura classes com anotação @Controller
            Reflections reflections = new ReflectionBuilder(app.resource).build();
            Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
            String requestURI = httpRequest.getRequestURI();
            
            String mainpath = "";
            String path = "";
            boolean isValidURI = false;
            
            //percorre classes e localiza mapeamento de rota
            for (Class<?> controller : controllers) {
                mainpath = getServletContext().getContextPath() + controller.getAnnotation(Controller.class).value();
                
                //captura métodos da classe e verifica path
                //Set<Method> methods = reflections.getMethodsAnnotatedWith(Path.class);
                Method[] methods = controller.getMethods();
                
                for (Method method : methods) {
                    if (!method.isAnnotationPresent(Path.class)) {
                        continue;
                    }
                    
                    path = this.pathBuilder(mainpath, method.getAnnotation(Path.class).value());

                    //valida request
                    if ((this.find(path, requestURI)) && (this.validMethod(method, httpRequest.getMethod()))) {
                        isValidURI = true;

                        //extrai parâmetros da url
                        this.paramMap.entrySet().forEach((param) -> {
                            httpRequest.setAttribute(param.getKey(), param.getValue());
                        });

                        //executa método
                        Object object = controller.newInstance();
                        View response = (method.getParameterCount() > 0) ? (View) method.invoke(object, httpRequest, httpResponse) : (View) method.invoke(object);
                        
                        if (response == null) {
                            return;
                        }
                        
                        //configura response
                        httpResponse.setStatus(response.status);
                        
                        if (response.responseType != ResponseType.ERROR) {
                            httpResponse.addHeader("location", response.location);
                        }
                        
                        //registra session caso exista
                        if (response.session != null) {
                            httpRequest.getSession().setAttribute(response.sessionname, response.session);
                        }
                        
                        //efetua serialização da entidade caso exista
                        if (response.entity != null) {
                            httpResponse.setCharacterEncoding("utf-8");
                            
                            //se não houver definição de MediaType, configura padrão em json
                            if (!method.isAnnotationPresent(Produces.class)) {
                                httpResponse.setContentType(MediaType.APPLICATION_JSON);
                                httpResponse.getWriter().write(new Json().build().toJson(response.entity));
                                httpResponse.getWriter().write(new Json().build().toJson(response.responseMessage));
                                return;
                            } else {
                                switch (method.getAnnotation(Produces.class).value()[0]) {
                                    case MediaType.APPLICATION_FORM_URLENCODED:
                                        httpResponse.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                                        httpRequest.setAttribute(response.entityname, response.entity);
                                        break;
                                    case MediaType.APPLICATION_JSON: 
                                        httpResponse.setContentType(MediaType.APPLICATION_JSON);
                                        httpResponse.getWriter().write(new Json().build().toJson(response.entity));
                                        return;
                                    default: throw new IllegalArgumentException("MediaType " + method.getAnnotation(Produces.class).value()[0] + " not configured");
                                }
                            }
                        }
                        
                        //se response foi chamado, executa ação
                        switch (response.responseType) {
                            case DISPATCHER :
                                httpRequest.getRequestDispatcher(response.location).forward(httpRequest, httpResponse);
                                break;
                            case REDIRECT :
                                if (response.entity == null) { 
                                    httpResponse.sendRedirect(response.location);
                                } else {
                                    httpRequest.getRequestDispatcher(response.location).forward(httpRequest, httpResponse);
                                };
                                break;
                            case ERROR :
                                if (response.entity == null) {
                                    httpResponse.sendError(response.status); 
                                } else { 
                                    httpResponse.sendError(response.status, (String) response.entity);
                                };
                                break;
                        }
                        
                        break;
                    }
                }
                
                if (isValidURI) {
                    break;
                }
            }
            
            //se uri não foi encontrada, envia erro 404
            if (!isValidURI) {
                httpResponse.sendError(404, "Route " + requestURI + " not found");
                return;
            }
        } catch(Exception ex) {
            ex.printStackTrace();
            throw new ServletException(ex);
        }
    }
    
    private String pathBuilder(String mainpath, String path) {
        if (path.equals("/")) {
            return mainpath;
        }
        
        if (path.startsWith("/")) {
            return path;
        }
        
        return (mainpath.endsWith("/")) ? mainpath + path : mainpath + "/" + path;
    }
    
    private boolean find(String annotationURI, String requestURI) {
        if (annotationURI.equals(requestURI)) {
            return true;
        }
        
        if (requestURI.equals("/")) {
            return true;
        }
        
        boolean valid = false;
        String[] annotationSplit = annotationURI.split("/");
        String[] requestSplit = requestURI.split("/");
        
        if (annotationSplit.length != requestSplit.length) {
            return false;
        }
        
        //captura parâmetros da requisição
        String key;
        String value;
        for (int i=0; i<requestSplit.length; i++) {
            key = annotationSplit[i];
            value = requestSplit[i];
            
            if ((key.startsWith("{")) && (key.endsWith("}"))) {
                paramMap.put(key.replace("{", "").replace("}", ""), this.getParamURIDecode(value));
                annotationSplit[i] = "*";
            }
        }
        
        //valida mapeamento da requisição
        for (int i=0; i<requestSplit.length; i++) {
            if ((!annotationSplit[i].equals(requestSplit[i])) && (!annotationSplit[i].equals("*"))) {
                valid = false;
                break;
            }
            
            valid = true;
        }
        
        return valid;
    }
    
    private boolean validMethod(Method method, String requestMethod) {
        return (((requestMethod.equals(GET.class.getSimpleName())) && ((method.isAnnotationPresent(GET.class))))
              ||((requestMethod.equals(POST.class.getSimpleName())) && ((method.isAnnotationPresent(POST.class))))
              ||((requestMethod.equals(PUT.class.getSimpleName())) && ((method.isAnnotationPresent(PUT.class))))
              ||((requestMethod.equals(DELETE.class.getSimpleName())) && ((method.isAnnotationPresent(DELETE.class)))));
    }
    
    private String getParamURIDecode(String paramValue) {
        try {            
            return URLDecoder.decode(paramValue, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            return null;
        }
    }
    
    @Deprecated
    private Controller resolvePage(HttpServletRequest req) {
        String path = req.getRequestURI().substring(req.getContextPath().length());
        Object obj = req.getServletContext().getAttribute("pages");

        if (obj instanceof List) {
            Optional<Controller> first = ((List<Controller>) obj).stream()
                    .filter(p -> path.equals(p.value()))
                    .findFirst();

            if (first.isPresent()) {
                return first.get();
            }
        }
        return null;
    }
 
}
