package fb.messenger

import grails.web.Action
import grails.web.api.WebAttributes

/**
 * Created by jonathantse on 6/7/2017.
 */

trait MessengerWebhook implements WebAttributes {
    abstract receiveMessage(def facebookEvent);

    @Action
    def validateWebHook() {
        log.debug "validating webhook with params = " + params.toString();

        if (fbService.validateWebhook(params)) {
            render params.hub.challenge
        } else {
            response.status = 403
        }
    }


    @Action
    def handleWebHook() {
        log.debug "handleWebHook triggered"
        def data = request.JSON

        if (data.object == "page") {
            data.entry.each() { entry ->
                String pageId = entry.id
                String timeOfEvent = entry.time;

                entry.messaging.each() { event ->
                    if (event.message) {
                        receiveMessage(event);
                    } else {
                        log.info("Webhook received unknown event: ${event}");
                    }
                }
            }

            render ""

        } else {
            response.status = 400
        }
    }
}