package fb.messenger

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(TestController)
class TestControllerSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void 'test validateWebhook'() {
        when:
            params.hub = [challenge: 'yoakssjns']
            def fbServiceMock = Mock(FbService)
            1 * fbServiceMock.validateWebhook(_) >> true
            controller.fbService = fbServiceMock
            controller.validateWebHook()

        then:
            response.text == 'yoakssjns'
    }

    void 'test receiveMessage'() {
        when:
            def fbEvent = [:]
            FbUser fbUser = null;
            controller.receiveMessage(fbEvent);
        then:
            response.status == 200
            response.text == ''
    }
}
