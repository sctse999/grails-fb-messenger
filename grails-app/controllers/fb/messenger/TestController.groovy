package fb.messenger


import grails.rest.*
import grails.converters.*

class TestController implements MessengerWebhook {
	static responseFormats = ['json', 'xml']

    def receiveMessage(def fbEvent) {
        render ""
    }

    def index() {

    }
}
