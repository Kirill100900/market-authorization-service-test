package market.dto;

import market.model.Account;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public record AccountDto(
        @NotNull
        Account account,
        @Positive
        Long profileId
) {
}
