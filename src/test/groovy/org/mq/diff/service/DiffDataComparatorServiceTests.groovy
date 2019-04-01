package org.mq.diff.service

import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mq.diff.domain.Base64BinaryData
import org.mq.diff.domain.Base64BinaryDataId
import org.mq.diff.domain.Side
import org.mq.diff.exception.MissingDiffIdParameterException
import org.mq.diff.exception.NonDiffIdFoundException
import org.mq.diff.exception.NotEnoughDataToCompareException
import org.mq.diff.repository.Base64BinaryDataRepository
import spock.lang.Specification
import spock.lang.Unroll

class DiffDataComparatorServiceTests extends Specification {
    @Mock
    Base64BinaryDataRepository base64BinaryDataRepository;

    @InjectMocks
    DiffDataComparatorService dataComparator

    def setup() {
        MockitoAnnotations.initMocks(this)
    }

    def "check service throw error when diffId parameter is missing"() {
        when: 'calling to the compare method with empty diffId'
        dataComparator.compare ''

        then: 'a ResponseStatusException should be thrown'
        thrown MissingDiffIdParameterException
    }

    def "check service throw error when diffId is not found"() {
        given: 'a non existing id'
        def id = '999'
        Mockito.when(base64BinaryDataRepository.findAllByIdDiffId(id)).thenReturn(new ArrayList<Base64BinaryData>())

        when: 'calling to the compare method with empty diffId'
        dataComparator.compare id

        then: 'a ResponseStatusException should be thrown'
        thrown NonDiffIdFoundException
    }

    def "check service throw error when there are not enough data to be compared"() {
        given: 'a existing id'
        def id = '999'
        Mockito.when(base64BinaryDataRepository.findAllByIdDiffId(id))
                .thenReturn(Arrays.asList(Base64BinaryData.builder()
                        .id(Base64BinaryDataId.builder()
                                .diffId(id)
                                .side(Side.LEFT)
                                .build())
                        .data('ABC')
                        .build()
                ))

        when: 'calling to the compare method with empty diffId'
        dataComparator.compare id

        then: 'a ResponseStatusException should be thrown'
        thrown NotEnoughDataToCompareException
    }

    @Unroll
    def "check return right data when left and right are equal or has different sizes having left: #leftData and right: #rightData"() {
        given: 'a existing id'
        def id = '999'

        and: 'having two base64 binaries some equals and some with different size'
        Mockito.when(base64BinaryDataRepository.findAllByIdDiffId(id))
                .thenReturn(Arrays.asList(
                        Base64BinaryData.builder()
                                .id(Base64BinaryDataId.builder().diffId(id).side(Side.LEFT).build())
                                .data(leftData).build(),
                        Base64BinaryData.builder()
                                .id(Base64BinaryDataId.builder().diffId(id).side(Side.RIGHT).build())
                                .data(rightData).build()
                ))

        when: 'calling to the compare method'
        def diffResultDTO = dataComparator.compare id

        then:
        assert diffResultDTO.diffId == id
        assert diffResultDTO.data[Side.RIGHT] == rightData

        where:
        leftData << ['ABC', 'ABCDE', 'ABC']
        rightData << ['ABC', 'ABC', 'ABCDE']
    }

    def "check diffLocations contains a map of offset: length, representing differences positions"() {
        given: 'a existing id'
        def id = '999'

        and: 'having two base64 binaries with the same size but different content to compare'
        Mockito.when(base64BinaryDataRepository.findAllByIdDiffId(id))
                .thenReturn(Arrays.asList(
                        Base64BinaryData.builder()
                                .id(Base64BinaryDataId.builder().diffId(id).side(Side.LEFT).build())
                                .data(leftData).build(),
                        Base64BinaryData.builder()
                                .id(Base64BinaryDataId.builder().diffId(id).side(Side.RIGHT).build())
                                .data(rightData).build()
                ))

        when: 'calling to the compare method'
        def diffResultDTO = dataComparator.compare id

        then:
        assert diffResultDTO.diffId == id
        assert diffResultDTO.data[Side.RIGHT] == rightData
        assert diffResultDTO.data[Side.LEFT] == leftData
        assert diffResultDTO.diffsLocations.size() == diffSize
        assert diffResultDTO.diffsLocations == diffLocations

        where:
        leftData << ['AABBFF', 'AABBFF', 'AABBFF']
        rightData << ['AABB1F', 'ACCBFF', 'CACCFF']
        diffSize << [1, 1, 2]
        diffLocations << [[4: 1], [1: 2], [0: 1, 2: 2]]
    }
}
