package br.com.powercrm.application.activity.retrieve.list;

import br.com.powercrm.IntegrationTest;
import br.com.powercrm.application.utils.DateUtils;
import br.com.powercrm.domain.activity.Activity;
import br.com.powercrm.domain.activity.ActivitySearchQuery;
import br.com.powercrm.domain.activity.ActivityType;
import br.com.powercrm.infrastructure.activity.persistence.ActivityJpaEntity;
import br.com.powercrm.infrastructure.activity.persistence.ActivityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.stream.Stream;

@IntegrationTest
public class ListActivitiesUseCaseIT {

    @Autowired
    private ListActivitiesUseCase useCase;

    @Autowired
    private ActivityRepository activityRepository;

    @BeforeEach
    void mockUp() throws ParseException {
        final var activities = Stream.of(
                        Activity.newActivity(
                                1L, 1L, 1L,
                                1L, "venda", "venda", ActivityType.VISIT,
                                DateUtils.convertStringDateToInstant("01/12/2022")
                        ),
                        Activity.newActivity(
                                2L, 2L, 1L,
                                2L, "ligar", "ligar", ActivityType.CALL,
                                DateUtils.convertStringDateToInstant("02/12/2022")
                        ),
                        Activity.newActivity(
                                3L, 3L, 1L,
                                3L, "telefonar", "telefonar", ActivityType.CALL,
                                DateUtils.convertStringDateToInstant("03/12/2022")
                        ),
                        Activity.newActivity(
                                4L, 4L, 1L,
                                4L, "email", "email", ActivityType.EMAIL,
                                DateUtils.convertStringDateToInstant("04/12/2022")
                        ),
                        Activity.newActivity(
                                5L, 5L, 1L,
                                5L, "retentar", "retentar", ActivityType.VISIT,
                                DateUtils.convertStringDateToInstant("05/12/2022")
                        ),
                        Activity.newActivity(
                                6L, 6L, 1L,
                                6L, "wats", "wats", ActivityType.WHATSAPP,
                                DateUtils.convertStringDateToInstant("06/12/2022")
                        ),
                        Activity.newActivity(
                                7L, 7L, 1L,
                                7L, "telefone", "telefone", ActivityType.CALL,
                                DateUtils.convertStringDateToInstant("07/12/2022")
                        )
                )
                .map(ActivityJpaEntity::from)
                .toList();

        activityRepository.saveAllAndFlush(activities);
    }

    @Test
    public void givenAValidTerm_whenTermDoesntMatchsPrePersisted_shouldReturnEmptyPage() throws ParseException {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "ji1j3i 1j3i1oj";
        final var expectedSort = "description";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 0;
        final var expectedTotal = 0;

        final var aQuery =
                new ActivitySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
    }

    @ParameterizedTest
    @CsvSource({
            "ven,0,10,1,1,venda",
            "lig,0,10,1,1,ligar",
            "nar,0,10,1,1,telefonar",
            "ema,0,10,1,1,email",
            "re,0,10,1,1,retentar",
            "wa,0,10,1,1,wats",
    })
    public void givenAValidTerm_whenCallsListActivities_shouldReturnActivitiesFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedActivityDescription
    ) throws ParseException {
        final var expectedSort = "description";
        final var expectedDirection = "asc";

        final var aQuery =
                new ActivitySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedActivityDescription, actualResult.items().get(0).description());
    }

    @ParameterizedTest
    @CsvSource({
            "description,asc,0,10,7,7,email",
            "description,desc,0,10,7,7,wats",
            "createdAt,asc,0,10,7,7,venda",
            "createdAt,desc,0,10,7,7,telefone",
    })
    public void givenAValidSortAndDirection_whenCallsListActivities_thenShouldReturnActivitiesOrdered(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedActivityDescription
    ) throws ParseException {
        final var expectedTerms = "";

        final var aQuery =
                new ActivitySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedActivityDescription, actualResult.items().get(0).description());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,7,email;ligar",
            "1,2,2,7,retentar;telefonar",
            "2,2,2,7,telefone;venda",
            "3,2,1,7,wats",
    })
    public void givenAValidPage_whenCallsListActivities_shouldReturnActivitiesPaginated(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedActivityDescription
    ) throws ParseException {
        final var expectedSort = "description";
        final var expectedDirection = "asc";
        final var expectedTerms = "";

        final var aQuery =
                new ActivitySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());

        int index = 0;
        for (final String expectedDescription : expectedActivityDescription.split(";")) {
            final String actualDescription = actualResult.items().get(index).description();
            Assertions.assertEquals(expectedDescription, actualDescription);
            index++;
        }
    }

}
