package fb.messenger

import ai.api.model.ResponseMessage
import com.google.gson.Gson
import grails.converters.JSON
import grails.test.mixin.integration.Integration
import grails.transaction.Rollback
import groovy.json.JsonSlurper
import spock.lang.Specification

@Integration
@Rollback
class ViewHelperSpec extends Specification {

//    FbService fbService;
    def setup() {
    }

    def cleanup() {
    }


    void "test render fb card url button"() {
        when:
            String cardJson = '{"type":1,"platform":"facebook","title":"Taxi","subtitle":"Available from getting to and out the airport","imageUrl":"http://n.sinaimg.cn/translate/20151006/PQrV-fximeyk8799105.jpg","buttons":[{"text":"Details","postback":"https://www.hongkongairport.com/chi/transport/to-from-airport/taxi.html"}]}'
            Gson gson = new Gson();
            ResponseMessage.ResponseCard responseCard = gson.fromJson(cardJson, ResponseMessage.ResponseCard.class)
            assert responseCard.buttons.first().text == 'Details'

            def model = [recipientId: 'recipientId', elements: [responseCard] ]
            String jsonBody = ViewHelper.renderJsonView('/fb/cards', model);
            println jsonBody;
            def jsonResult = new JsonSlurper().parseText(jsonBody);


        then:
            jsonResult.recipient.id == 'recipientId'
            jsonResult.message.attachment.payload.elements.first().title == 'Taxi'
            jsonResult.message.attachment.payload.elements.first().image_url == 'http://n.sinaimg.cn/translate/20151006/PQrV-fximeyk8799105.jpg'

            jsonResult.message.attachment.payload.elements.first().buttons.first().type == 'web_url'
            jsonResult.message.attachment.payload.elements.first().buttons.first().url == 'https://www.hongkongairport.com/chi/transport/to-from-airport/taxi.html'
            jsonResult.message.attachment.payload.elements.first().buttons.first().webview_height_ratio == 'tall'
    }

    void "test render fb card text button"() {
        when:
            String cardJson = '{"type":1,"platform":"facebook","title":"Taxi","subtitle":"Available from getting to and out the airport","imageUrl":"http://n.sinaimg.cn/translate/20151006/PQrV-fximeyk8799105.jpg","buttons":[{"text":"plain text","postback":"Just some plain text"}]}';
            Gson gson = new Gson();
            ResponseMessage.ResponseCard responseCard = gson.fromJson(cardJson, ResponseMessage.ResponseCard.class)
            assert responseCard.buttons.first().text == 'plain text'
            assert responseCard.buttons.first().postback == 'Just some plain text'


            def model = [recipientId: 'recipientId', elements: [responseCard] ]
            String jsonBody = ViewHelper.renderJsonView('/fb/cards', model);
            println jsonBody;
            def jsonResult = new JsonSlurper().parseText(jsonBody);


        then:
            jsonResult.recipient.id == 'recipientId'
            jsonResult.message.attachment.payload.elements.first().title == 'Taxi'
            jsonResult.message.attachment.payload.elements.first().buttons.first().type == 'postback'
            jsonResult.message.attachment.payload.elements.first().buttons.first().title == 'plain text'
            jsonResult.message.attachment.payload.elements.first().buttons.first().payload == 'Just some plain text'



    }



}
