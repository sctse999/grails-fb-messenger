package fb.messenger

import grails.plugin.json.view.JsonViewTemplateEngine
import grails.util.Holders
import groovy.text.Template
import groovy.util.logging.Slf4j
import org.springframework.context.ApplicationContext

/**
 * Created by jonathantse on 15/6/2017.
 */
@Slf4j
class ViewHelper {
    static String renderJsonView(String view, Map model) {
        ApplicationContext ctx = Holders.grailsApplication.mainContext
        JsonViewTemplateEngine templateEngine = (JsonViewTemplateEngine)ctx.getBean(JsonViewTemplateEngine)

        if (templateEngine) {
            Template template = templateEngine.resolveTemplate(view)
            Writable writable = template.make(model)

            StringWriter stringWriter = new StringWriter()
            writable.writeTo(stringWriter)

            log.debug(stringWriter.toString());
            return stringWriter.toString();

        } else {
            throw new Exception("Cannot get templateEngine");
        }
    }
}
