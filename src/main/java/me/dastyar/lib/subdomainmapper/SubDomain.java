package me.dastyar.lib.subdomainmapper;

import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**</p>
 * The {@code SubDomain} interface represent annotation
 * that allow to map a <i>SubDomain</i> to a servlet.</p>
 * <p>
 * You can gain this functionality by using {@code @SubDomain}
 * in your code. For example:
 * <blockquote><pre>
 *   <code>@SubDomain(name = "sub")</code>
 *   <code>@WebServlet(name = "MyServlet",urlPatterns = {"/MyServlet"})</code>
 *   public class MyServlet extends HttpServlet{
 *        ....
 *   }
 * </pre></blockquote>
 * Above code maps <code>http://sub.example.com/</code> URL
 * to the <i>MyServlet</i>.
 * <strong>Note:</strong> Using <i>@WebServlet()</i> with a valid value for <i>urlPatterns</i> is required.
 * </p>
 * @author Alireza Dastyar
 * @since V0.1
 */
@Retention(RUNTIME)
@Target({TYPE})
public @interface SubDomain {
    /**
     * <p>
     * Variable <i>name</i> refers to the Sub-Domain that is mapped to 
     * the servlet.</p>
     * For example:<br/>
     * <code>@SubDomain(name = "sub" )</code><br/> maps the
     * <code>http://sub.example.com/</code> to the <code>Servlet</code>.
     */
    String name();
}
