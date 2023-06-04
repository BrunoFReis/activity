package br.com.powercrm.infrastructure.api;

import br.com.powercrm.domain.pagination.Pagination;
import br.com.powercrm.infrastructure.activity.models.ActivityResponse;
import br.com.powercrm.infrastructure.activity.models.CreateActivityRequest;
import br.com.powercrm.infrastructure.activity.models.UpdateActivityRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;

@RequestMapping(value = "activities")
@Tag(name = "Activities")
public interface ActivityAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new Activity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    ResponseEntity<?> createActivity(@RequestBody @Valid CreateActivityRequest input) throws ParseException;

    @GetMapping
    @Operation(summary = "List all activities paginated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listed successfully"),
            @ApiResponse(responseCode = "422", description = "A invalid parameter was received"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    Pagination<?> listActivities(
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "description") final String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") final String direction
    ) throws ParseException;

    @GetMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Get a Activity by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activity retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Activity was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    ActivityResponse getById(@PathVariable(name = "id") String id) throws Exception;

    @PutMapping(
            value = "{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Update a activity by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activity updated successfully"),
            @ApiResponse(responseCode = "404", description = "Activity was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    ResponseEntity<?> updateById(@PathVariable(name = "id") String id, @RequestBody UpdateActivityRequest input) throws ParseException;

    @PutMapping(value = "/cancele/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Cancele a activity by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Activity canceled successfully"),
            @ApiResponse(responseCode = "404", description = "Activity was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    void deleteById(@PathVariable(name = "id") String id);

    @PutMapping(value = "/complete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Complete a activity by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Activity completed successfully"),
            @ApiResponse(responseCode = "404", description = "Activity was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    void completeById(@PathVariable(name = "id") String id);
}
