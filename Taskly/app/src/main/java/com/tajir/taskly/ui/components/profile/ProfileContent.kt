package com.tajir.taskly.ui.components.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tajir.taskly.R
import com.tajir.taskly.data.stateModels.UserState
import com.tajir.taskly.events.UserEvent
import com.tajir.taskly.ui.components.authentication.NameInput
import com.tajir.taskly.ui.components.home.ConfirmDialog

@Composable
fun ProfileContent(
    isEdit: Boolean,
    onEditChanged: () -> Unit,
    handleEvent: (event: UserEvent) -> Unit,
    user: UserState,
    navController: NavController,
    isUpdateValid: Boolean
) {
    var showDeleteConfirm by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .padding(horizontal = 64.dp)
            .fillMaxWidth()
            .fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo"
                )
            }

            // First Name
            Column(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = "First Name")
                if (!isEdit) {
                    Text(
                        text = user.first_name ?: "",
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                } else {
                    NameInput(
                        name = user.first_name ?: "",
                        onNameChanged = { fname ->
                            handleEvent(
                                UserEvent.SetFirstName(fname)
                            )
                        },
                        label_res = R.string.label_first_name
                    )
                }
            }


            // Last Name
            Column(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = "Last Name")
                if (!isEdit) {
                    Text(
                        text = user.last_name ?: "",
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                } else {
                    NameInput(
                        name = user.last_name ?: "",
                        onNameChanged = { lname ->
                            handleEvent(
                                UserEvent.SetLastName(lname)
                            )
                        },
                        label_res = R.string.label_last_name
                    )
                }
            }


            // Email
            Column(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = "Email")
                if (!isEdit) {
                    Text(
                        text = user.email ?: "",
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                } else {
                    NameInput(
                        name = user.email ?: "",
                        onNameChanged = { email ->
                            handleEvent(
                                UserEvent.SetEmail(email)
                            )
                        },
                        label_res = R.string.label_email
                    )
                }
            }


            // Buttons
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(vertical = 16.dp)
            ) {

                Button(
                    onClick = {
                        showDeleteConfirm = true
                    },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White,
                        contentColor = Color.Red
                    ),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 6.dp,
                        pressedElevation = 8.dp,
                        disabledElevation = 0.dp
                    ),
                    modifier = Modifier.padding(end = 3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete"
                    )
                }

                Button(
                    onClick = {
                        navController.navigate("login_screen")
                    },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White,
                        contentColor = Color.Red
                    ),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 6.dp,
                        pressedElevation = 8.dp,
                        disabledElevation = 0.dp
                    ),
                    modifier = Modifier.padding(end = 3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = "Logout"
                    )
                }

                if (isEdit) {
                    Button(
                        onClick = {
                            handleEvent(
                                UserEvent.UpdateUserCall
                            )
                            onEditChanged()
                        },
                        enabled = isUpdateValid,
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.White
                        ),
                        elevation = ButtonDefaults.elevation(
                            defaultElevation = 6.dp,
                            pressedElevation = 8.dp,
                            disabledElevation = 0.dp
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Update"
                        )
                    }
                } else {
                    Button(
                        onClick = {
                            onEditChanged()
                        },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.White
                        ),
                        elevation = ButtonDefaults.elevation(
                            defaultElevation = 6.dp,
                            pressedElevation = 8.dp,
                            disabledElevation = 0.dp
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit"
                        )
                    }
                }
            }
        }
    }

    if (showDeleteConfirm) {
        ConfirmDialog(
            content = String.format("Delete Account"),
            onDismiss = { showDeleteConfirm = false },
            onConfirm = {
                showDeleteConfirm = false
                handleEvent(
                    UserEvent.DeleteUserCall
                )
                navController.navigate("login_screen")
            }
        )
    }
}