package org.mq.diff.controller

import org.mq.diff.domain.Side
import org.mq.diff.dto.DiffEdDTO
import org.mq.diff.dto.DiffResultDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.hateoas.Resource
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DiffControllerTests extends Specification {

    @Autowired
    TestRestTemplate testRestTemplate

    @LocalServerPort
    int randomServerPort

    def baseURL = ''

    def setup() {
        baseURL = "http://localhost:$randomServerPort/v1/diff/999"
    }

    def "check error is thrown when calling get diffs without operands left and right"() {
        when: 'calling the get method of the diff service'
        def response = testRestTemplate.getForEntity("$baseURL", Resource)

        then:
        response.status == 400
    }

    def "check error is thrown when calling get diffs without jut one operand"() {
        given: 'one base64 binary in the form of diffEdDTO and persist it in the database'
        def diffEdDTOLeft = DiffEdDTO.builder().data('YSB2AAA5IGxvbmcgc3RyaW5n').build()
        testRestTemplate.postForEntity("$baseURL/left", diffEdDTOLeft, Resource)

        when: 'calling the get method of the diff service'
        def response = testRestTemplate.getForEntity("$baseURL", DiffResultDTO)

        then:
        response.status == 400
    }

    def "check diff response contains right operand only"() {
        given: 'a base64 binary'
        def data = 'YSB2AAA5IGxvbmcgc3RyaW5n'

        and: 'create two diffEdDTO with the same base64 binary'
        def diffEdDTOLeft = DiffEdDTO.builder().data(data).build()
        def diffEdDTORight = DiffEdDTO.builder().data(data).build()

        and: 'persisted in both sides, left and right'
        testRestTemplate.postForEntity("$baseURL/left", diffEdDTOLeft, Resource)
        testRestTemplate.postForEntity("$baseURL/right", diffEdDTORight, Resource)

        when: 'calling the get method of the diff service'
        def response = testRestTemplate.getForEntity("$baseURL", DiffResultDTO)

        then:
        assert response.status == 200
        assert response.body.diffId == '999'
        assert response.body.data[Side.RIGHT] == diffEdDTORight.data
        assert !response.body.diffsLocations
    }

    def "check diff response contains right and left operands and diffs locations"() {
        given: 'two base64 binaries in the form of diffEdDTOs'
        def diffEdDTOLeft = DiffEdDTO.builder().data('YSB2AAA5IGxvbmcgc3RyaW5n').build()
        def diffEdDTORight = DiffEdDTO.builder().data('AAAYSB25IGxvbmcgc3RyaW5n').build()

        and: 'saved to the data base'
        testRestTemplate.postForEntity("$baseURL/left", diffEdDTOLeft, Resource)
        testRestTemplate.postForEntity("$baseURL/right", diffEdDTORight, Resource)

        when: 'calling the get method of the diff service'
        def response = testRestTemplate.getForEntity("$baseURL", DiffResultDTO)

        then:
        assert response.status == 200
        assert response.body.diffId == '999'
        assert response.body.data[Side.RIGHT] == diffEdDTORight.data
        assert response.body.data[Side.LEFT] == diffEdDTOLeft.data
        assert response.body.diffsLocations
    }
}
