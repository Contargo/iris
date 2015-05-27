package net.contargo.iris.web;

import net.contargo.iris.security.UserAuthenticationService;

import org.joda.time.DateTime;

import org.slf4j.Logger;

import org.springframework.security.core.Authentication;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import java.lang.invoke.MethodHandles;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.slf4j.LoggerFactory.getLogger;

import static java.lang.String.format;

import static java.nio.charset.StandardCharsets.UTF_8;


/**
 * This ExceptionHandler generates detailed error reports if an unexpected error resp. Exception occurs.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
public class WebExceptionHandler extends SimpleMappingExceptionResolver {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());
    private static final int MIN = 1000;
    private static final int MAX = 9999;

    private final File baseDirectory;
    private final UserAuthenticationService userAuthenticationService;

    public WebExceptionHandler(File baseDirectory, UserAuthenticationService userAuthenticationService) {

        this.baseDirectory = baseDirectory;
        this.userAuthenticationService = userAuthenticationService;

        createDirectoryIfNotExists(baseDirectory);
    }

    @Override
    public ModelAndView doResolveException(HttpServletRequest req, HttpServletResponse resp, Object h, Exception ex) {

        Authentication user = userAuthenticationService.getCurrentUser();
        String now = new DateTime().toString("yyyy-MM-dd_HH-mm-ss");
        String id = format("%s-%4d", now, randomInRange(MIN, MAX));

        File errorReport = new File(baseDirectory, id + "-report.txt");

        try(PrintWriter writer = new PrintWriter(errorReport, UTF_8.name())) {
            String errorMessage = format(""
                    + "Id: %s%n%n"
                    + "Date: %s%n"
                    + "Username: %s%n%n"
                    + "Requested URL: %s%n"
                    + "Controller: %s%n%n%n"
                    + "Stacktrace:%n", id, now, user.getName(), req.getRequestURL().toString(), h.getClass().getName());

            writer.append(errorMessage);
            ex.printStackTrace(writer);
            writer.append("\n\n");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            LOG.warn("File not found or encoding not supported", e);
        }

        LOG.error("New error report was generated due to an exception.{} for further information.",
            errorReport.getPath());

        return super.doResolveException(req, resp, h, ex);
    }


    private void createDirectoryIfNotExists(File dir) {

        if (dir.exists() && !dir.isDirectory()) {
            throw new IllegalStateException(dir.getAbsolutePath() + " should be an directory but is a file");
        } else if (!dir.exists() && !dir.mkdirs()) {
            throw new IllegalStateException("Directory " + dir.getAbsolutePath() + " can not be created");
        }
    }


    private int randomInRange(int min, int max) {

        return new Random().nextInt((max - min + 1)) + min;
    }
}
