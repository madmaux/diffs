package org.mq.diff.service

import ma.glasnost.orika.MapperFacade
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mq.diff.domain.Base64BinaryData
import org.mq.diff.domain.Base64BinaryDataId
import org.mq.diff.domain.Side
import org.mq.diff.dto.ContentDTO
import org.mq.diff.dto.DiffEdDTO
import org.mq.diff.exception.IsNotABase64DataException
import org.mq.diff.exception.MissingDataParameterException
import org.mq.diff.repository.Base64BinaryDataRepository
import spock.lang.Specification
import spock.lang.Unroll

import java.nio.charset.StandardCharsets

class ContentDataManagerServiceTests extends Specification {

    @Mock
    Base64BinaryDataRepository base64BinaryDataRepository

    @Mock
    MapperFacade mapperFacade

    @InjectMocks
    ContentDataManagerService dataManager

    def setup() {
        MockitoAnnotations.initMocks(this)
    }

    def "check that save return a non empty DataContentDTO"() {
        given:
        def diffEdDTO = new DiffEdDTO(data: Base64.getEncoder().encodeToString('TEST'.getBytes(StandardCharsets.UTF_8)))
        def diffId = '123'
        def side = Side.LEFT

        def base64BinaryData = Base64BinaryData.builder().data(diffEdDTO.data).id(
                Base64BinaryDataId.builder().diffId(diffId).side(side).build()
        ).build()

        def contentDTO = ContentDTO.builder()
                .diffId(diffId)
                .side(side)
                .data()
                .build()

        Mockito.when(base64BinaryDataRepository.save(base64BinaryData)).thenReturn(base64BinaryData)
        Mockito.when(mapperFacade.map(base64BinaryData, ContentDTO.class)).thenReturn(contentDTO)

        when:
        def result = dataManager.save(diffId, diffEdDTO, side)

        then:
        assert result != null
        assert diffId == result.getDiffId()
        assert side == result.getSide()
    }

    @Unroll
    def "check service throw error when no data is provided: #diffEdDTO"() {
        given: 'given diffID and side arguments provided'
        def diffId = '123'
        def side = Side.LEFT

        when: 'calling to the save method'
        dataManager.save diffId, diffEdDTO, side

        then: 'a ResponseStatusException should be thrown'
        thrown MissingDataParameterException

        where: 'no data is provided'
        diffEdDTO << [
                null,
                DiffEdDTO.builder().data("").build(),
                DiffEdDTO.builder().build()
        ]
    }

    def "check service throw error when no data is note base 64"() {
        given: 'given diffID and side arguments provided'
        def diffId = '123'
        def side = Side.LEFT
        def diffEdDTO = DiffEdDTO.builder().data('TE ST').build()

        when: 'calling to the save method'
        dataManager.save diffId, diffEdDTO, side

        then: 'a ResponseStatusException should be thrown'
        thrown IsNotABase64DataException
    }
}
