package net.contargo.iris.api;

import net.contargo.iris.security.UserAuthenticationService;

import org.joda.time.DateTime;

import org.slf4j.Logger;

import org.springframework.security.core.Authentication;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import java.lang.invoke.MethodHandles;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.slf4j.LoggerFactory.getLogger;


/**
 * This ExceptionHandler generates detailed error reports if an unexpected error resp. Exception occurs.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class WebExceptionHandler extends SimpleMappingExceptionResolver implements HandlerExceptionResolver {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());
    private static final int MAX_RANDOM_NUMBER = 9999;

    private final File basePath;
    private final Random random = new Random();
    private final UserAuthenticationService userAuthenticationService;

    public WebExceptionHandler(File basePath, UserAuthenticationService userAuthenticationService) {

        this.basePath = basePath;
        this.userAuthenticationService = userAuthenticationService;

        createDirectoryIfNotExists(basePath);
    }

    String textualRepresentationOfCurrentUser() {

        Authentication currentUser = userAuthenticationService.getCurrentUser();

        return currentUser == null ? "?" : String.format("user: %s%n", currentUser.getName());
    }


    @Override
    public ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
        Exception e) {

        // generate unique id for exception
        DateTime now = new DateTime();
        String id = String.format("%s-%4d", now.toString("yyyy-MM-dd_HH-mm-ss"), random.nextInt(MAX_RANDOM_NUMBER));

        request.setAttribute("exception_id", id);

        String user = textualRepresentationOfCurrentUser();

        addExceptionStackTraceToRequest(request, e);

        File file = new File(basePath, id + "-report.txt");

        try {
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            writer.append(String.format(
                    "id: %s%ndate: %s%n%s%n" + "requesturl: %s%ncontroller: %s%n%n%n"
                    + "stacktrace:%n", id, now.toString("dd.mm.yyyy HH:mm:ss"), user,
                    request.getRequestURL().toString(), handler.getClass().getName()));

            e.printStackTrace(writer);
            writer.append("\n\n");

            writer.close();
        } catch (FileNotFoundException fileNotFoundException) {
            LOG.warn("File not found.", fileNotFoundException);
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            LOG.warn("Encoding not supported.", unsupportedEncodingException);
        }

        LOG.error("A new error report was generated due to an exception. See " + file.getPath()
            + " for further information.", e);

        return super.doResolveException(request, response, handler, e);
    }


    private void addExceptionStackTraceToRequest(HttpServletRequest request, Exception exception) {

        StringWriter writer = new StringWriter();
        exception.printStackTrace(new PrintWriter(writer));
        request.setAttribute("exception_trace", writer.toString());
    }


    private void createDirectoryIfNotExists(File dir) {

        if (dir.exists() && !dir.isDirectory()) {
            throw new IllegalStateException("Directory " + dir.getAbsolutePath()
                + " exists and is a file (needed a directory)");
        } else if (!dir.exists() && !dir.mkdirs()) {
            throw new IllegalStateException("Directory " + dir.getAbsolutePath()
                + " can not be created");
        }
    }
}
