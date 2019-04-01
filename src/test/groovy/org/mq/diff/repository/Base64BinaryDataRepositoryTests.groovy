package org.mq.diff.repository

import org.mq.diff.domain.Base64BinaryData
import org.mq.diff.domain.Base64BinaryDataId
import org.mq.diff.domain.Side
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import spock.lang.Specification
import spock.lang.Unroll

@DataJpaTest
class Base64BinaryDataRepositoryTests extends Specification {

    @Autowired
    Base64BinaryDataRepository repository

    @Unroll
    def "check data is persisted in the data base for side: #side"() {
        given: 'a base64BinaryData'
        def base64BinaryData = Base64BinaryData.builder().data("AAFFBB123").id(
                Base64BinaryDataId.builder().diffId("123").side(side).build()
        ).build()

        when: 'calling save method on Base64BinaryRepository'
        def result = repository.save base64BinaryData

        then: 'when searching the recently added data result should match with the given one'
        def persistedData = repository.findById(base64BinaryData.id).get()

        assert persistedData != null
        assert base64BinaryData.data == persistedData.data
        assert base64BinaryData.id == result.id

        where:
        side << [Side.LEFT, Side.RIGHT]
    }

    def "check data is updated when entering same id but different data"() {
        given: 'a base64BinaryData'
        def base64BinaryData = Base64BinaryData.builder().data("AAFFBB123").id(
                Base64BinaryDataId.builder().diffId("123").side(Side.LEFT).build()
        ).build()

        and: 'persisting on database'
        repository.save base64BinaryData

        and: 'change the data on the given base64BinaryData to be persisted'
        base64BinaryData.data = 'FFFFFF'

        when: 'calling save method on Base64BinaryRepository'
        def result = repository.save base64BinaryData

        then: 'when searching the existing base64BinaryData result data should match with changed one'
        def persistedData = repository.findById(base64BinaryData.id).get()

        assert persistedData != null
        assert base64BinaryData.data == persistedData.data
        assert base64BinaryData.id == result.id
    }

    def "check data is removed"() {
        given: 'a base64BinaryData'
        def base64BinaryData = Base64BinaryData.builder().data("AAFFBB123").id(
                Base64BinaryDataId.builder().diffId("123").side(Side.LEFT).build()
        ).build()

        and: 'persisting on database'
        repository.save base64BinaryData

        when: 'calling delete method on Base64BinaryRepository'
        repository.delete base64BinaryData

        then: 'when searching the existing base64BinaryData result data should match with changed one'
        assert repository.findById(base64BinaryData.id).isEmpty() == true

    }
}
