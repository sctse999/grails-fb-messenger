package fb.messenger

import ai.api.model.ResponseMessage.ResponseCard
import ai.api.model.ResponseMessage.ResponseImage
import ai.api.model.ResponseMessage.ResponseQuickReply
import ai.api.model.ResponseMessage.ResponseSpeech
import grails.transaction.Transactional
import grails.util.Holders
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import org.apache.commons.lang.StringUtils

@Transactional
class FbService {
    def fbMessengerConfig = Holders.config.fbMessenger
    def proxyConfig = Holders.config.proxy

    /**
     * Validate webhook
     * @param params
     * @return
     */
    boolean validateWebhook(def params) {
        if (params.hub != null && params.hub.mode == "subscribe" && params.hub.verify_token == fbMessengerConfig.verifyToken) {
            return true
        } else {
            log.debug("validate webhook failed")
            return false
        }
    }

    def setProxy(httpBuilder) {
        if (proxyConfig.enabled) {
            httpBuilder.setProxy(proxyConfig.host, proxyConfig.port , null)
        }
    }

    def getUserProfile(String userId) {
        def result = [:];

        HTTPBuilder httpBuilder = new HTTPBuilder(fbMessengerConfig.baseUrl)
        setProxy(httpBuilder)

        httpBuilder.request(Method.GET, ContentType.JSON) {
            headers.'Content-Type' = 'application/json'

            uri.path = fbMessengerConfig.userProfileApi + "${userId}"

            uri.query = [
                    fields: 'first_name,last_name,profile_pic,locale,timezone,gender',
                    access_token: fbMessengerConfig.pageAccessToken
            ]

            response.success = { resp, json ->
                result = [resp: resp, json: json]
            }

            response.failure = { resp, json ->
                result = [resp: resp, json: json]
            }
        }

        return result;
    }

    void sendMessage(ResponseSpeech responseSpeech, String recipientId) {
        responseSpeech.speech.each() {
            if (StringUtils.isNotBlank(it)) {
                def model = [recipientId: recipientId, text:  it]
                String jsonBody = ViewHelper.renderJsonView('/fb/text', model);
                sendMessage(jsonBody);
            }
        }
    }

    void sendMessage(ResponseQuickReply responseQuickReply, String recipientId) {
        def model = [recipientId: recipientId, title: responseQuickReply.title, replies: responseQuickReply.replies]
        String jsonBody = ViewHelper.renderJsonView('/fb/quick_replies', model);
        sendMessage(jsonBody);

    }

    void sendMessage(ResponseCard responseCard, String recipientId) {

    }

    void sendMessage(List<ResponseCard> responseCards, String recipientId) {
        if (responseCards.size() > 0) {
            def model = [recipientId: recipientId, elements: responseCards ]
            String jsonBody = ViewHelper.renderJsonView('/fb/cards', model);
            sendMessage(jsonBody);
        }
    }

    void sendMessage(ResponseImage responseImage, String recipientId) {
        def model = [recipientId: recipientId, responseImage: responseImage]
        String jsonBody = ViewHelper.renderJsonView('/fb/image', model);
        sendMessage(jsonBody);
    }


    def sendMessage(String payload) {
        def result = [:];

        HTTPBuilder httpBuilder = new HTTPBuilder(fbMessengerConfig.baseUrl)
        setProxy(httpBuilder)

        httpBuilder.request(Method.POST, ContentType.JSON) {
            headers.'Content-Type' = 'application/json'

            uri.path = fbMessengerConfig.sendApi

            uri.query = [
                    access_token: fbMessengerConfig.pageAccessToken
            ]

            body = payload

            response.success = { resp, json ->
                def rId = json.recipient_id
                def mId = json.message_id
                log.info "Successfully sent message with id ${mId} to recipient ${rId}"
                result = [resp: resp, json: json]
            }

            response.failure = { resp, json ->
                log.error "Unable to send message."
                log.error resp.status.toString()
                result = [resp: resp, json: json]

            }
        }

        return result;
    }
}
