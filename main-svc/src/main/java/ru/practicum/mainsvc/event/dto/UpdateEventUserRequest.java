package ru.practicum.mainsvc.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateEventUserRequest extends UpdateEventAbstractRequest {
    private UserStateAction stateAction;
}
