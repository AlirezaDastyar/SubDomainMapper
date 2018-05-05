package me.dastyar.lib.subdomainmapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.reflections.Reflections;

/**
 * <p>
 * The {@code SubDomainFilter} interface represent SubDomain mapping
 * functionality by filter method.</p>
 *
 * @author Alireza Dastyar
 * @since V0.1
 */
public interface SubDomainFilter {

    /**
     * Store classes that are annotated with <code>@SubDomain()</code>
     */
    static Map<String, Class<?>> classes = new HashMap<>();
 
    /**
     * <p> 
     * Filter method forwards the request that has a RequestURL with
     * <i>SubDomain</i>
     * to the servlet that is annotated with <code>@SubDomain</code> and has
     * same SubDomain name as RequestURL.</p>
     * Example:
     * <blockquote><pre>
     *
     *   <code>@WebFilter(filterName = "MyFilter", urlPatterns = {"/*"})</code>
     *   public class MyFilter extends Filter{
     *        ....
     *        public doFilter(ServletRequest request, ServletResponse response,FilterChain chain){
     *              //beforeFiltering 
     *              // for http://www.example.com  domainParts is 2 [ example, com]
     *              // for http://www.example.co.uk  domainParts is 3 [example, co, uk]
     *              boolean sub=SubDomainFilter.filter(request,response,2);
     *              if(!sub){
     *                  //If no SubDomain mapping happened
     *              }
     *              //other filtering code
     *        }
     *   }
     * </pre></blockquote>
     * <p>
     * <strong>Note:</strong> You most call this method in
     * javax.servlet.Filter#doFilter() or in subclasses.</p>
     * <p>
     * <strong>Note:</strong> You most call this method in
     * javax.servlet.Filter#doFilter() or in subclasses.</p>
     *
     * @param request is the same as ServletRequest in
     * javax.servlet.Filter#doFilter()<br/>
     * @param response is the same ServletRequest in
     * javax.servlet.Filter#doFilter()<br/>
     * @param domainParts is very important field and indicates parts of splitted
     * (on dot character) of domin name without <i>www</i><br/>
     * @return a boolean which indicates that any mapping happened or not
     * @throws ServletException in failure on getRequestDispatcher().forward()
     * @throws IOException in failure on getRequestDispatcher().forward()
     */
    public static boolean fillter(ServletRequest request, ServletResponse response, int domainParts) throws ServletException, IOException {
        if (classes.isEmpty()) {
            populateList();
        }
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String url = req.getRequestURL().toString();
        if (url.split("/")[2].split("\\.").length <= domainParts) {
            return false;
        }
        String sub = url.split("/")[2].split("\\.")[0];
        Class<?> clazz = classes.entrySet().stream()
                .filter(x -> x.getKey().equalsIgnoreCase(sub))
                .map(x -> x.getValue())
                .findFirst().orElse(null);
        if (clazz == null) {
            return false;
        }
        String path = clazz.getDeclaredAnnotation(WebServlet.class).urlPatterns()[0];
        request.getRequestDispatcher(path).forward(request, response);
        return true;
    }

    /**
     * Find all annotated classes with <code>@SubDomain()</code> that are
     * available in ClassPath and adds to <i>classes</i> field.
     */
    static void populateList() {
        Reflections rf = new Reflections("");
        Set<Class<?>> set = rf.getTypesAnnotatedWith(SubDomain.class);
        set.stream().filter(x -> x.getDeclaredAnnotation(WebServlet.class) != null)
                .filter(x -> x.getDeclaredAnnotation(WebServlet.class).urlPatterns().length > 0)
                .forEach(x -> classes.put(x.getDeclaredAnnotation(SubDomain.class).name(), x));
    }
}
