package fb.messenger

import ai.api.model.ResponseMessage.ResponseCard
import com.google.gson.Gson
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import groovy.json.JsonSlurper
import spock.lang.IgnoreRest
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestFor(FbService)
@Mock(FbUser)
class FbServiceSpec extends Specification {
    def setup() {
    }

    def cleanup() {
    }

    void "test addUser"() {
        when:
            FbUser fbUser = service.addUser(1403706049714863);
        then:
            fbUser.firstName == 'Jonathan'
            fbUser.lastName == 'Tse'
            fbUser.gender == 'male'
            fbUser.locale == 'en_US'
            fbUser.timezone == 8
            FbUser.count() == 1
    }


    void "test getUserProfile"() {
        when:
            FbUserProfile fbUserProfile = service.getUserProfile(1403706049714863);
        then:
            fbUserProfile.firstName == 'Jonathan'
            fbUserProfile.lastName == 'Tse'
            fbUserProfile.gender == 'male'
            fbUserProfile.locale == 'en_US'
            fbUserProfile.timezone == 8
    }

    void "test validateWebhook success"() {
        when:
            def params = [hub: [mode: 'subscribe', verify_token: 'secretToken']]
            boolean result = service.validateWebhook(params)
        then:
            result == true
    }

    void "test validateWebhook fail"() {
        when:
            def params = [hub: [mode: 'subscribe', verify_token: 'wrong_token']]
            boolean result = service.validateWebhook(params)
        then:
            result == false
    }

}