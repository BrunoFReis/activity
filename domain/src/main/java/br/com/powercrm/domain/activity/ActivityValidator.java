package br.com.powercrm.domain.activity;

import br.com.powercrm.domain.validation.Error;
import br.com.powercrm.domain.validation.ValidationHandler;
import br.com.powercrm.domain.validation.Validator;

public class ActivityValidator extends Validator {

    public static final int NAME_MAX_LENGTH = 255;
    public static final int NAME_MIN_LENGTH = 3;
    private final Activity activity;

    public ActivityValidator(
            final Activity anActivity,
            final ValidationHandler aHandler
    ) {
        super(aHandler);
        this.activity = anActivity;
    }

    @Override
    public void validate() {
        checkDescriptionConstraints();

        if (this.activity.getCreativeCompanyUserId() == null) {
            this.validationHandler().append(new Error("'creativeCompanyUserId' should not be null"));
        }

        if (this.activity.getResponsibleCompanyUserId() == null) {
            this.validationHandler().append(new Error("'responsibleCompanyUserId' should not be null"));
        }

        if (this.activity.getCompanyId() == null) {
            this.validationHandler().append(new Error("'companyId' should not be null"));
        }

        if (this.activity.getNegotiationId() == null) {
            this.validationHandler().append(new Error("'negotiationId' should not be null"));
        }

        if (this.activity.getType() == null) {
            this.validationHandler().append(new Error("'type' should not be null"));
        }

        if (this.activity.getStatus() == null) {
            this.validationHandler().append(new Error("'status' should not be null"));
        }
    }

    private void checkDescriptionConstraints() {
        final var description = this.activity.getDescription();

        if (description == null) {
            this.validationHandler().append(new Error("'description' should not be null"));
            return;
        }

        if (description.isBlank()) {
            this.validationHandler().append(new Error("'description' should not be empty"));
            return;
        }

        final int lenght = description.trim().length();
        if (lenght > NAME_MAX_LENGTH || lenght < NAME_MIN_LENGTH){
            this.validationHandler().append(new Error("'description' must be between 3 and 255 characters"));
        }
    }
}
