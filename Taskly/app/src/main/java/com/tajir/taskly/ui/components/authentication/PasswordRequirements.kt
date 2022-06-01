package com.tajir.taskly.ui.components.authentication

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tajir.taskly.data.stateModels.PasswordRequirement

@Composable
fun PasswordRequirements(
    modifier: Modifier = Modifier,
    satisfiedRequirements: List<PasswordRequirement>
) {
    Column(
        modifier = modifier
    ) {
        PasswordRequirement.values().forEach { requirement ->
            RequirementComp(
                message = stringResource(
                    id = requirement.label),
                satisfied = satisfiedRequirements.contains(
                    requirement
                )
            )
        }
    }
}
