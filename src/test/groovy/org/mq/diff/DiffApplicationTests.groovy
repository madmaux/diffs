package org.mq.diff

import org.mq.diff.controller.DiffController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class DiffApplicationTests extends Specification {

    @Autowired(required = false)
    private DiffController diffController

    def "when context is loaded then all expected beans are created"() {
        expect: "the WebController is created"
        diffController
    }
}
