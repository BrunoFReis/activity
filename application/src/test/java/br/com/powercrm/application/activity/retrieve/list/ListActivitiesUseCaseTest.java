package br.com.powercrm.application.activity.retrieve.list;

import br.com.powercrm.application.activity.UseCaseTest;
import br.com.powercrm.application.utils.DateUtils;
import br.com.powercrm.domain.activity.Activity;
import br.com.powercrm.domain.activity.ActivityGateway;
import br.com.powercrm.domain.activity.ActivitySearchQuery;
import br.com.powercrm.domain.activity.ActivityType;
import br.com.powercrm.domain.pagination.Pagination;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

public class ListActivitiesUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultListActivitiesUseCase useCase;

    @Mock
    private ActivityGateway activityGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(activityGateway);
    }

    @Test
    public void givenAValidQuery_whenCallsListActivities_thenShouldReturnActivities() throws ParseException {
        final var activities = List.of(
                Activity.newActivity(
                        1L, 1L, 1L,
                        1L, "atividade1", "atividade1", ActivityType.NOTE,
                        DateUtils.convertStringDateToInstant("01/12/2022")
                ),
                Activity.newActivity(
                        2L, 2L, 2L,
                        2L, "atividade2", "atividade2", ActivityType.CALL,
                        DateUtils.convertStringDateToInstant("02/12/2022")
                )
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var aQuery =
                new ActivitySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var expectedPagination =
                new Pagination<>(expectedPage, expectedPerPage, activities.size(), activities);

        final var expectedItemsCount = 2;
        final var expectedResult = expectedPagination.map(ActivityListOutput::from);

        when(activityGateway.findAll(eq(aQuery)))
                .thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedResult, actualResult);
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(activities.size(), actualResult.total());
    }

    @Test
    public void givenAValidQuery_whenHasNoResults_thenShouldReturnEmptyActivities() throws ParseException {
        final var activities = List.<Activity>of();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var aQuery =
                new ActivitySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var expectedPagination =
                new Pagination<>(expectedPage, expectedPerPage, activities.size(), activities);

        final var expectedItemsCount = 0;
        final var expectedResult = expectedPagination.map(ActivityListOutput::from);

        when(activityGateway.findAll(eq(aQuery)))
                .thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedResult, actualResult);
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(activities.size(), actualResult.total());
    }

    @Test
    public void givenAValidQuery_whenGatewayThrowsRandomException_thenShouldAException() throws ParseException {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedErrorMessage = "Gateway error";

        final var aQuery =
                new ActivitySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        when(activityGateway.findAll(eq(aQuery)))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException =
                assertThrows(IllegalStateException.class, () -> useCase.execute(aQuery));

        assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
