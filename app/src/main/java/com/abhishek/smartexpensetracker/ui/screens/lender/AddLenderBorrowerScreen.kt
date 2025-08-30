import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLenderBorrowerScreen(
    onSave: (String, String, String, Boolean, String, String) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var isGiven by remember { mutableStateOf(true) }
    var dueDate by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Lender/Borrower") }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = { onCancel() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }
                Button(
                    onClick = {
                        onSave(name, mobile, amount, isGiven, dueDate, notes)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Save")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Person's Name
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Person's Name") },
                modifier = Modifier.fillMaxWidth()
            )

            // Mobile Number
            OutlinedTextField(
                value = mobile,
                onValueChange = { mobile = it },
                label = { Text("Mobile Number (optional)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            // Amount
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Transaction Type Toggle
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Transaction Type", fontSize = 16.sp)
                AssistChip(
                    onClick = { isGiven = true },
                    label = { Text("Given") },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (isGiven) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                    )
                )
                AssistChip(
                    onClick = { isGiven = false },
                    label = { Text("Taken") },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (!isGiven) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                    )
                )
            }

            // Due Date (date picker simplified as text input for now)
            OutlinedTextField(
                value = dueDate,
                onValueChange = { dueDate = it },
                label = { Text("Due Date (dd MMM yyyy)") },
                modifier = Modifier.fillMaxWidth()
            )

            // Notes
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                maxLines = 4
            )
        }
    }
}


@Preview
@Composable
private fun PreviewAddLenderBorrowerScreen() {
    AddLenderBorrowerScreen(
        onSave = { name, mobile, amount, isGiven, dueDate, notes ->
            // Handle save logic here
        },
        onCancel = {}
    )
}