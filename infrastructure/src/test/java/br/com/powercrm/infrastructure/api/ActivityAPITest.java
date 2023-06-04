package br.com.powercrm.infrastructure.api;

import br.com.powercrm.ControllerTest;
import br.com.powercrm.application.activity.complete.CompleteActivityUseCase;
import br.com.powercrm.application.activity.create.CreateActivityOutput;
import br.com.powercrm.application.activity.create.CreateActivityUseCase;
import br.com.powercrm.application.activity.delete.DeleteActivityUseCase;
import br.com.powercrm.application.activity.retrieve.get.ActivityOutput;
import br.com.powercrm.application.activity.retrieve.get.GetActivityByIdUseCase;
import br.com.powercrm.application.activity.retrieve.list.ActivityListOutput;
import br.com.powercrm.application.activity.retrieve.list.ListActivitiesUseCase;
import br.com.powercrm.application.activity.update.UpdateActivityOutput;
import br.com.powercrm.application.activity.update.UpdateActivityUseCase;
import br.com.powercrm.application.utils.DateUtils;
import br.com.powercrm.domain.activity.Activity;
import br.com.powercrm.domain.activity.ActivityID;
import br.com.powercrm.domain.activity.ActivityStatus;
import br.com.powercrm.domain.activity.ActivityType;
import br.com.powercrm.domain.exceptions.DomainException;
import br.com.powercrm.domain.exceptions.NotFoundException;
import br.com.powercrm.domain.pagination.Pagination;
import br.com.powercrm.domain.validation.Error;
import br.com.powercrm.domain.validation.handler.Notification;
import br.com.powercrm.infrastructure.activity.models.CreateActivityRequest;
import br.com.powercrm.infrastructure.activity.models.UpdateActivityRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Right;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = ActivityAPI.class)
class ActivityAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateActivityUseCase createActivityUseCase;

    @MockBean
    private GetActivityByIdUseCase getActivityByIdUseCase;

    @MockBean
    private UpdateActivityUseCase updateActivityUseCase;

    @MockBean
    private DeleteActivityUseCase deleteActivityUseCase;

    @MockBean
    private CompleteActivityUseCase completeActivityUseCase;

    @MockBean
    private ListActivitiesUseCase listActivitiesUseCase;

    @Test
    public void givenAValidCommand_whenCallCreateActivity_thenShouldReturnActivityId() throws Exception {
        final var expectedCreativeCompanyUserId = 1L;
        final var expectedResponsibleCompanyUserId = 2L;
        final var expectedCompanyId = 2L;
        final var expectedNegotiationId = 1L;
        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final var expectedType = ActivityType.NOTE;
        final var expectedScheduleDateString = "01/01/2023";

        final var aInput = new CreateActivityRequest(
                expectedCreativeCompanyUserId,
                expectedResponsibleCompanyUserId,
                expectedCompanyId,
                expectedNegotiationId,
                expectedDescription,
                expectedResponse,
                expectedType,
                expectedScheduleDateString
        );

        when(createActivityUseCase.execute(any()))
                .thenReturn(Right(CreateActivityOutput.from("123")));

        final var request = MockMvcRequestBuilders.post("/activities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/activities/123"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo("123")));

        verify(createActivityUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedCreativeCompanyUserId, cmd.creativeCompanyUserId())
                        && Objects.equals(expectedResponsibleCompanyUserId, cmd.responsibleCompanyUserId())
                        && Objects.equals(expectedCompanyId, cmd.companyId())
                        && Objects.equals(expectedNegotiationId, cmd.negotiationId())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedResponse, cmd.response())
                        && Objects.equals(expectedType, cmd.type())
                        && Objects.equals(expectedScheduleDateString, cmd.scheduleDate())
        ));
    }

    @Test
    public void givenAInvalidCommand_whenCallCreateActivity_thenShouldReturnNotification() throws Exception {
        final Long expectedCreativeCompanyUserId = null;
        final var expectedResponsibleCompanyUserId = 2L;
        final var expectedCompanyId = 2L;
        final var expectedNegotiationId = 1L;
        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final var expectedType = ActivityType.NOTE;
        final var expectedScheduleDateString = "01/01/2023";
        final var expectedMessageError = "'creativeCompanyUserId' should not be null";

        final var aInput = new CreateActivityRequest(
                expectedCreativeCompanyUserId,
                expectedResponsibleCompanyUserId,
                expectedCompanyId,
                expectedNegotiationId,
                expectedDescription,
                expectedResponse,
                expectedType,
                expectedScheduleDateString
        );

        when(createActivityUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedMessageError))));

        final var request = MockMvcRequestBuilders.post("/activities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessageError)));

        verify(createActivityUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedCreativeCompanyUserId, cmd.creativeCompanyUserId())
                        && Objects.equals(expectedResponsibleCompanyUserId, cmd.responsibleCompanyUserId())
                        && Objects.equals(expectedCompanyId, cmd.companyId())
                        && Objects.equals(expectedNegotiationId, cmd.negotiationId())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedResponse, cmd.response())
                        && Objects.equals(expectedType, cmd.type())
                        && Objects.equals(expectedScheduleDateString, cmd.scheduleDate())
        ));
    }

    @Test
    public void givenAInvalidCommand_whenCallCreateActivity_thenShouldReturnDomainException() throws Exception {
        final Long expectedCreativeCompanyUserId = null;
        final var expectedResponsibleCompanyUserId = 2L;
        final var expectedCompanyId = 2L;
        final var expectedNegotiationId = 1L;
        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final var expectedType = ActivityType.NOTE;
        final var expectedScheduleDateString = "01/01/2023";
        final var expectedMessageError = "'creativeCompanyUserId' should not be null";

        final var aInput = new CreateActivityRequest(
                expectedCreativeCompanyUserId,
                expectedResponsibleCompanyUserId,
                expectedCompanyId,
                expectedNegotiationId,
                expectedDescription,
                expectedResponse,
                expectedType,
                expectedScheduleDateString
        );

        when(createActivityUseCase.execute(any()))
                .thenThrow(DomainException.with(new Error(expectedMessageError)));

        final var request = MockMvcRequestBuilders.post("/activities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedMessageError)))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessageError)));

        verify(createActivityUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedCreativeCompanyUserId, cmd.creativeCompanyUserId())
                        && Objects.equals(expectedResponsibleCompanyUserId, cmd.responsibleCompanyUserId())
                        && Objects.equals(expectedCompanyId, cmd.companyId())
                        && Objects.equals(expectedNegotiationId, cmd.negotiationId())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedResponse, cmd.response())
                        && Objects.equals(expectedType, cmd.type())
                        && Objects.equals(expectedScheduleDateString, cmd.scheduleDate())
        ));
    }

    @Test
    public void givenAValidId_whenCallGetActivityById_thenShouldReturnActivity() throws Exception {
        final var expectedCreativeCompanyUserId = 1L;
        final var expectedResponsibleCompanyUserId = 2L;
        final var expectedCompanyId = 2L;
        final var expectedNegotiationId = 1L;
        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final var expectedScheduleDateString = "01/01/2023";
        final var expectedTypeString = ActivityType.NOTE.getDescription();
        final var expectedStatusString = ActivityStatus.ACTIVE.getDescription();

        final var anActivity = Activity.newActivityFromId(
                "123",
                expectedCreativeCompanyUserId,
                expectedResponsibleCompanyUserId,
                expectedCompanyId,
                expectedNegotiationId,
                expectedDescription,
                expectedResponse,
                ActivityType.NOTE,
                DateUtils.convertStringDateToInstant(expectedScheduleDateString)
        );

        final var expectedId = anActivity.getId().getValue();

        when(getActivityByIdUseCase.execute(any()))
                .thenReturn(ActivityOutput.from(anActivity));


        final var request = get("/activities/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(Integer.parseInt(expectedId))))
                .andExpect(jsonPath("$.creative", equalTo((int) expectedCreativeCompanyUserId)))
                .andExpect(jsonPath("$.responsible", equalTo((int) expectedResponsibleCompanyUserId)))
                .andExpect(jsonPath("$.company", equalTo((int) expectedCompanyId)))
                .andExpect(jsonPath("$.negotiation", equalTo((int) expectedNegotiationId)))
                .andExpect(jsonPath("$.description", equalTo(expectedDescription)))
                .andExpect(jsonPath("$.response", equalTo(expectedResponse)))
                .andExpect(jsonPath("$.type", equalTo(expectedTypeString)))
                .andExpect(jsonPath("$.status", equalTo(expectedStatusString)))
                .andExpect(jsonPath("$.scheduleDate", equalTo(expectedScheduleDateString)))
                .andExpect(jsonPath("$.created_at", equalTo(DateUtils.convertInstantToStringDateTime(anActivity.getCreatedAt()))))
                .andExpect(jsonPath("$.updated_at", equalTo(DateUtils.convertInstantToStringDateTime(anActivity.getUpdatedAt()))))
                .andExpect(jsonPath("$.finish_at", equalTo(anActivity.getFinishedAt())));

        verify(getActivityByIdUseCase, times(1)).execute(eq(expectedId));
    }

    @Test
    public void givenAInvalidId_whenCallsGetActivity_shouldReturnNotFound() throws Exception {
        final var expectedErrorMessage = "Activity with ID 123 was not found";
        final var expectedId = ActivityID.from("123");

        when(getActivityByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Activity.class, expectedId));

        // when
        final var request = get("/activities/{id}", expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateActivity_shouldReturnActivityId() throws Exception {
        // given
        final var expectedId = "123";
        final var expectedCreativeCompanyUserId = 1L;
        final var expectedResponsibleCompanyUserId = 2L;
        final var expectedCompanyId = 2L;
        final var expectedNegotiationId = 1L;
        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final var expectedType = ActivityType.NOTE;
        final var expectedScheduleDateString = "01/01/2023";

        when(updateActivityUseCase.execute(any()))
                .thenReturn(Right(UpdateActivityOutput.from(expectedId)));

        final var aCommand =
                new UpdateActivityRequest(
                        expectedCreativeCompanyUserId,
                        expectedResponsibleCompanyUserId,
                        expectedCompanyId,
                        expectedNegotiationId,
                        expectedDescription,
                        expectedResponse,
                        expectedType,
                        ActivityStatus.ACTIVE,
                        expectedScheduleDateString
                );

        // when
        final var request = put("/activities/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)));

        verify(updateActivityUseCase, times(1)).execute(argThat(aUpdateActivity ->
                Objects.equals(expectedCreativeCompanyUserId, aUpdateActivity.creativeCompanyUserId())
                        && Objects.equals(expectedId, aUpdateActivity.id())
                        && Objects.equals(expectedResponsibleCompanyUserId, aUpdateActivity.responsibleCompanyUserId())
                        && Objects.equals(expectedCompanyId, aUpdateActivity.companyId())
                        && Objects.equals(expectedNegotiationId, aUpdateActivity.negotiationId())
                        && Objects.equals(expectedDescription, aUpdateActivity.description())
                        && Objects.equals(expectedResponse, aUpdateActivity.response())
                        && Objects.equals(expectedType, aUpdateActivity.type())
                        && Objects.equals(ActivityStatus.ACTIVE, aUpdateActivity.status())
                        && Objects.equals(expectedScheduleDateString, aUpdateActivity.scheduleDate())
        ));
    }

    @Test
    public void givenAInvalidName_whenCallsUpdateActivity_thenShouldReturnDomainException() throws Exception {
        // given
        final var expectedId = "123";
        final Long expectedCreativeCompanyUserId = null;
        final var expectedResponsibleCompanyUserId = 2L;
        final var expectedCompanyId = 2L;
        final var expectedNegotiationId = 1L;
        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final var expectedType = ActivityType.NOTE;
        final var expectedScheduleDateString = "01/01/2023";

        final var expectedErrorCount = 1;
        final var expectedMessage = "'expectedCreativeCompanyUserId' should not be null";

        when(updateActivityUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedMessage))));

        final var aCommand =
                new UpdateActivityRequest(
                        expectedCreativeCompanyUserId,
                        expectedResponsibleCompanyUserId,
                        expectedCompanyId,
                        expectedNegotiationId,
                        expectedDescription,
                        expectedResponse,
                        expectedType,
                        ActivityStatus.ACTIVE,
                        expectedScheduleDateString
                );

        // when
        final var request = put("/activities/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(expectedErrorCount)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));

        verify(updateActivityUseCase, times(1)).execute(argThat(aUpdateActivity ->
                Objects.equals(expectedCreativeCompanyUserId, aUpdateActivity.creativeCompanyUserId())
                        && Objects.equals(expectedId, aUpdateActivity.id())
                        && Objects.equals(expectedResponsibleCompanyUserId, aUpdateActivity.responsibleCompanyUserId())
                        && Objects.equals(expectedCompanyId, aUpdateActivity.companyId())
                        && Objects.equals(expectedNegotiationId, aUpdateActivity.negotiationId())
                        && Objects.equals(expectedDescription, aUpdateActivity.description())
                        && Objects.equals(expectedResponse, aUpdateActivity.response())
                        && Objects.equals(expectedType, aUpdateActivity.type())
                        && Objects.equals(ActivityStatus.ACTIVE, aUpdateActivity.status())
                        && Objects.equals(expectedScheduleDateString, aUpdateActivity.scheduleDate())
        ));
    }

    @Test
    public void givenACommandWithInvalidID_whenCallsUpdateActivity_shouldReturnNotFoundException() throws Exception {
        // given
        final var expectedId = "123";
        final var expectedCreativeCompanyUserId = 1L;
        final var expectedResponsibleCompanyUserId = 2L;
        final var expectedCompanyId = 2L;
        final var expectedNegotiationId = 1L;
        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final var expectedType = ActivityType.NOTE;
        final var expectedScheduleDateString = "01/01/2023";

        final var expectedErrorMessage = "Activity with ID 123 was not found";

        when(updateActivityUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Activity.class, ActivityID.from(expectedId)));

        final var aCommand =
                new UpdateActivityRequest(
                        expectedCreativeCompanyUserId,
                        expectedResponsibleCompanyUserId,
                        expectedCompanyId,
                        expectedNegotiationId,
                        expectedDescription,
                        expectedResponse,
                        expectedType,
                        ActivityStatus.ACTIVE,
                        expectedScheduleDateString
                );

        // when
        final var request = put("/activities/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(updateActivityUseCase, times(1)).execute(argThat(aUpdateActivity ->
                Objects.equals(expectedCreativeCompanyUserId, aUpdateActivity.creativeCompanyUserId())
                        && Objects.equals(expectedId, aUpdateActivity.id())
                        && Objects.equals(expectedResponsibleCompanyUserId, aUpdateActivity.responsibleCompanyUserId())
                        && Objects.equals(expectedCompanyId, aUpdateActivity.companyId())
                        && Objects.equals(expectedNegotiationId, aUpdateActivity.negotiationId())
                        && Objects.equals(expectedDescription, aUpdateActivity.description())
                        && Objects.equals(expectedResponse, aUpdateActivity.response())
                        && Objects.equals(expectedType, aUpdateActivity.type())
                        && Objects.equals(ActivityStatus.ACTIVE, aUpdateActivity.status())
                        && Objects.equals(expectedScheduleDateString, aUpdateActivity.scheduleDate())
        ));
    }

    @Test
    public void givenAValidId_whenCallsDeleteAcitivity_shouldReturnNoContent() throws Exception {
        // given
        final var expectedId = "123";

        doNothing()
                .when(deleteActivityUseCase).execute(any());

        // when
        final var request = put("/activities/cancele/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isNoContent());

        verify(deleteActivityUseCase, times(1)).execute(eq(expectedId));
    }

    @Test
    public void givenAValidId_whenCallsCompleteAcitivity_shouldReturnNoContent() throws Exception {
        // given
        final var expectedId = "123";

        doNothing()
                .when(completeActivityUseCase).execute(any());

        // when
        final var request = put("/activities/complete/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isNoContent());

        verify(completeActivityUseCase, times(1)).execute(eq(expectedId));
    }

    @Test
    public void givenValidParams_whenCallsListActivities_shouldReturnActivities() throws Exception {
        final var expectedCreativeCompanyUserId = 1L;
        final var expectedResponsibleCompanyUserId = 2L;
        final var expectedCompanyId = 2L;
        final var expectedNegotiationId = 1L;
        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final var expectedScheduleDateString = "01/01/2023";

        final var anActivity = Activity.newActivityFromId(
                "123",
                expectedCreativeCompanyUserId,
                expectedResponsibleCompanyUserId,
                expectedCompanyId,
                expectedNegotiationId,
                expectedDescription,
                expectedResponse,
                ActivityType.NOTE,
                DateUtils.convertStringDateToInstant(expectedScheduleDateString)
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "atividade";
        final var expectedSort = "description";
        final var expectedDirection = "desc";
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(ActivityListOutput.from(anActivity));

        when(listActivitiesUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        // when
        final var request = get("/activities")
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(Integer.parseInt(anActivity.getId().getValue()))))
                .andExpect(jsonPath("$.items[0].creative", equalTo(anActivity.getCreativeCompanyUserId().intValue())))
                .andExpect(jsonPath("$.items[0].responsible", equalTo(anActivity.getResponsibleCompanyUserId().intValue())))
                .andExpect(jsonPath("$.items[0].company", equalTo(anActivity.getCompanyId().intValue())))
                .andExpect(jsonPath("$.items[0].negotiation", equalTo(anActivity.getNegotiationId().intValue())))
                .andExpect(jsonPath("$.items[0].description", equalTo(anActivity.getDescription())))
                .andExpect(jsonPath("$.items[0].response", equalTo(anActivity.getResponse())))
                .andExpect(jsonPath("$.items[0].type", equalTo(anActivity.getType().getDescription())))
                .andExpect(jsonPath("$.items[0].status", equalTo(anActivity.getStatus().getDescription())))
                .andExpect(jsonPath("$.items[0].scheduleDate", equalTo(DateUtils.convertInstantToStringDate(anActivity.getScheduled()))))
                .andExpect(jsonPath("$.items[0].createdAt", equalTo(DateUtils.convertInstantToStringDateTime(anActivity.getCreatedAt()))))
                .andExpect(jsonPath("$.items[0].updatedAt", equalTo(DateUtils.convertInstantToStringDateTime(anActivity.getUpdatedAt()))))
                .andExpect(jsonPath("$.items[0].finishAt", equalTo(anActivity.getFinishedAt())));

        verify(listActivitiesUseCase, times(1)).execute(argThat(query ->
                Objects.equals(expectedPage, query.page())
                        && Objects.equals(expectedPerPage, query.perPage())
                        && Objects.equals(expectedDirection, query.direction())
                        && Objects.equals(expectedSort, query.sort())
                        && Objects.equals(expectedTerms, query.terms())
        ));
    }

}