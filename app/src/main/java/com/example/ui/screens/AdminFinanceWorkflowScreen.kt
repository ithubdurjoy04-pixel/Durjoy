package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.DepositEntity
import com.example.data.local.ExpenseEntity
import com.example.data.local.MemberEntity
import com.example.domain.model.AdminRole
import com.example.domain.model.AppLanguage
import com.example.domain.model.TransactionStatus
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*
import com.example.util.DhakaTimeHelper

sealed class FinancialRecord {
    data class Deposit(val data: DepositEntity) : FinancialRecord()
    data class Expense(val data: ExpenseEntity) : FinancialRecord()
}

@Composable
fun AdminFinanceWorkflowScreen(
    language: AppLanguage,
    currentRole: AdminRole,
    members: List<MemberEntity>,
    deposits: List<DepositEntity>,
    expenses: List<ExpenseEntity>,
    onBack: () -> Unit,
    onCreateDraftDeposit: (memberId: Long, memberName: String, amount: Double, date: String, method: String, ref: String, desc: String) -> Unit,
    onCreateDraftExpense: (title: String, category: String, amount: Double, date: String, method: String, desc: String) -> Unit,
    onReviewDeposit: (Long) -> Unit,
    onReviewExpense: (Long) -> Unit,
    onPublishDeposit: (Long) -> Unit,
    onPublishExpense: (Long) -> Unit,
    onUnpublishDeposit: (Long) -> Unit,
    onUnpublishExpense: (Long) -> Unit,
    onRejectDeposit: (Long) -> Unit,
    onRejectExpense: (Long) -> Unit
) {
    val isBn = language == AppLanguage.BENGALI

    var selectedStatusFilter by remember { mutableStateOf<String?>("ALL") }
    var showAddDepositDialog by remember { mutableStateOf(false) }
    var showAddExpenseDialog by remember { mutableStateOf(false) }

    // Combine records
    val combinedRecords = remember(deposits, expenses, selectedStatusFilter) {
        val list = mutableListOf<FinancialRecord>()
        deposits.forEach { list.add(FinancialRecord.Deposit(it)) }
        expenses.forEach { list.add(FinancialRecord.Expense(it)) }

        val filtered = if (selectedStatusFilter == "ALL" || selectedStatusFilter == null) {
            list
        } else {
            list.filter {
                when (it) {
                    is FinancialRecord.Deposit -> it.data.status == selectedStatusFilter
                    is FinancialRecord.Expense -> it.data.status == selectedStatusFilter
                }
            }
        }
        filtered.sortedByDescending {
            when (it) {
                is FinancialRecord.Deposit -> it.data.createdAt
                is FinancialRecord.Expense -> it.data.createdAt
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("admin_finance_workflow_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header with Back Button
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isBn) "আর্থিক কর্মপ্রবাহ ও পর্যালোচনা" else "Financial Draft → Review → Publish",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = BrandMaroonLight)
                    )
                    Text(
                        text = if (isBn) "ভূমিকা: ${currentRole.titleBn} হিসেবে পরিচালিত" else "Role: Operating as ${currentRole.titleEn}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }

        // Action Buttons: Add Draft Deposit / Expense
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { showAddDepositDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandMaroon),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.AddCircle, contentDescription = null, modifier = Modifier.size(16.dp), tint = BrandGold)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (isBn) "নতুন জমা খসড়া" else "Draft Deposit", fontSize = 12.sp)
                }

                Button(
                    onClick = { showAddExpenseDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.RemoveCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (isBn) "নতুন ব্যয় খসড়া" else "Draft Expense", fontSize = 12.sp)
                }
            }
        }

        // Filter Chips: ALL, DRAFT, UNDER_REVIEW, PUBLISHED, UNPUBLISHED
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val filters = listOf(
                    "ALL" to (if (isBn) "সকল" else "All"),
                    "DRAFT" to (if (isBn) "খসড়া" else "Draft"),
                    "UNDER_REVIEW" to (if (isBn) "পর্যালোচনা" else "Review"),
                    "PUBLISHED" to (if (isBn) "প্রকাশিত" else "Published"),
                    "UNPUBLISHED" to (if (isBn) "অপ্রকাশিত" else "Unpublished")
                )
                filters.forEach { (statusKey, label) ->
                    FilterChip(
                        selected = selectedStatusFilter == statusKey,
                        onClick = { selectedStatusFilter = statusKey },
                        label = { Text(label, fontSize = 11.sp) }
                    )
                }
            }
        }

        if (combinedRecords.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.PendingActions, contentDescription = null, tint = BrandGold, modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isBn) "কোনো লেনদেন রেকর্ড পাওয়া যায়নি" else "No financial records found in this category",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = if (isBn) "উপরে বোতাম চেপে নতুন জমা বা খরচের খসড়া তৈরি করুন।" else "Use the buttons above to create a new draft deposit or expense.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }

        // Records List
        items(combinedRecords) { record ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    when (record) {
                        is FinancialRecord.Deposit -> {
                            val dep = record.data
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(StatusSuccess.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.ArrowDownward, contentDescription = null, tint = StatusSuccess, modifier = Modifier.size(18.dp))
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = if (isBn) "সদস্য জমা: ${dep.memberName}" else "Deposit: ${dep.memberName}",
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                        )
                                        Text(
                                            text = "${dep.paymentMethod} • ${dep.depositDate}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                        )
                                    }
                                }

                                Text(
                                    text = "+${DhakaTimeHelper.formatCurrency(dep.amount, isBn)}",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = StatusSuccess)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                StatusBadge(status = dep.status, isBn = isBn)
                                Text(
                                    text = if (isBn) "প্রস্তুতকারক: ${dep.createdByAdmin}" else "Entered by: ${dep.createdByAdmin}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            // Action buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (dep.status == "DRAFT") {
                                    OutlinedButton(
                                        onClick = { onReviewDeposit(dep.id) },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(if (isBn) "পর্যালোচনা করুন" else "Review", fontSize = 11.sp)
                                    }
                                    OutlinedButton(
                                        onClick = { onRejectDeposit(dep.id) },
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626)),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(if (isBn) "বাতিল" else "Reject", fontSize = 11.sp)
                                    }
                                } else if (dep.status == "UNDER_REVIEW") {
                                    Button(
                                        onClick = { onPublishDeposit(dep.id) },
                                        colors = ButtonDefaults.buttonColors(containerColor = StatusSuccess),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(Icons.Default.Publish, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(if (isBn) "অনুমোদন ও প্রকাশ" else "Publish", fontSize = 11.sp)
                                    }
                                    OutlinedButton(
                                        onClick = { onRejectDeposit(dep.id) },
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626)),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(if (isBn) "বাতিল" else "Reject", fontSize = 11.sp)
                                    }
                                } else if (dep.status == "PUBLISHED") {
                                    OutlinedButton(
                                        onClick = { onUnpublishDeposit(dep.id) },
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = StatusWarning),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(Icons.Default.VisibilityOff, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(if (isBn) "অপ্রকাশিত করুন" else "Unpublish", fontSize = 11.sp)
                                    }
                                } else if (dep.status == "UNPUBLISHED") {
                                    Button(
                                        onClick = { onPublishDeposit(dep.id) },
                                        colors = ButtonDefaults.buttonColors(containerColor = BrandMaroon),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(if (isBn) "পুনরায় প্রকাশ করুন" else "Re-publish", fontSize = 11.sp)
                                    }
                                }
                            }
                        }

                        is FinancialRecord.Expense -> {
                            val exp = record.data
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFFEE2E2)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.ArrowUpward, contentDescription = null, tint = Color(0xFFDC2626), modifier = Modifier.size(18.dp))
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = exp.title,
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                        )
                                        Text(
                                            text = "${exp.category} • ${exp.expenseDate}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                        )
                                    }
                                }

                                Text(
                                    text = "-${DhakaTimeHelper.formatCurrency(exp.amount, isBn)}",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                StatusBadge(status = exp.status, isBn = isBn)
                                Text(
                                    text = if (isBn) "প্রস্তুতকারক: ${exp.createdByAdmin}" else "Entered by: ${exp.createdByAdmin}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            // Action buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (exp.status == "DRAFT") {
                                    OutlinedButton(
                                        onClick = { onReviewExpense(exp.id) },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(if (isBn) "পর্যালোচনা করুন" else "Review", fontSize = 11.sp)
                                    }
                                    OutlinedButton(
                                        onClick = { onRejectExpense(exp.id) },
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626)),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(if (isBn) "বাতিল" else "Reject", fontSize = 11.sp)
                                    }
                                } else if (exp.status == "UNDER_REVIEW") {
                                    Button(
                                        onClick = { onPublishExpense(exp.id) },
                                        colors = ButtonDefaults.buttonColors(containerColor = StatusSuccess),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(Icons.Default.Publish, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(if (isBn) "অনুমোদন ও প্রকাশ" else "Publish", fontSize = 11.sp)
                                    }
                                    OutlinedButton(
                                        onClick = { onRejectExpense(exp.id) },
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626)),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(if (isBn) "বাতিল" else "Reject", fontSize = 11.sp)
                                    }
                                } else if (exp.status == "PUBLISHED") {
                                    OutlinedButton(
                                        onClick = { onUnpublishExpense(exp.id) },
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = StatusWarning),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(Icons.Default.VisibilityOff, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(if (isBn) "অপ্রকাশিত করুন" else "Unpublish", fontSize = 11.sp)
                                    }
                                } else if (exp.status == "UNPUBLISHED") {
                                    Button(
                                        onClick = { onPublishExpense(exp.id) },
                                        colors = ButtonDefaults.buttonColors(containerColor = BrandMaroon),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(if (isBn) "পুনরায় প্রকাশ করুন" else "Re-publish", fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // DIALOG: ADD DRAFT DEPOSIT
    if (showAddDepositDialog) {
        var memberNameInput by remember { mutableStateOf("") }
        var amountInput by remember { mutableStateOf("") }
        var dateInput by remember { mutableStateOf("২০২৬") }
        var methodInput by remember { mutableStateOf("Cash") }
        var refInput by remember { mutableStateOf("") }
        var descInput by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddDepositDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        val amt = amountInput.toDoubleOrNull() ?: 0.0
                        if (memberNameInput.isNotBlank() && amt > 0) {
                            onCreateDraftDeposit(0L, memberNameInput, amt, dateInput, methodInput, refInput, descInput)
                            showAddDepositDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandMaroon)
                ) {
                    Text(if (isBn) "খসড়া হিসেবে সংরক্ষণ করুন" else "Save as Draft")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDepositDialog = false }) {
                    Text(if (isBn) "বাতিল" else "Cancel")
                }
            },
            title = {
                Text(
                    text = if (isBn) "নতুন সদস্য জমা এন্ট্রি (খসড়া)" else "New Member Deposit Draft",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = memberNameInput,
                        onValueChange = { memberNameInput = it },
                        label = { Text(if (isBn) "সদস্যের নাম *" else "Member Name *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = amountInput,
                        onValueChange = { amountInput = it },
                        label = { Text(if (isBn) "জমার পরিমাণ (টাকা) *" else "Amount (BDT) *") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = methodInput,
                        onValueChange = { methodInput = it },
                        label = { Text(if (isBn) "পরিশোধ পদ্ধতি (নগদ/বিকাশ/ব্যাংক)" else "Payment Method") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = descInput,
                        onValueChange = { descInput = it },
                        label = { Text(if (isBn) "বিবরণ বা নোট" else "Description / Note") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        )
    }

    // DIALOG: ADD DRAFT EXPENSE
    if (showAddExpenseDialog) {
        var titleInput by remember { mutableStateOf("") }
        var categoryInput by remember { mutableStateOf("Puja Expense") }
        var amountInput by remember { mutableStateOf("") }
        var dateInput by remember { mutableStateOf("২০২৬") }
        var methodInput by remember { mutableStateOf("Cash") }
        var descInput by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddExpenseDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        val amt = amountInput.toDoubleOrNull() ?: 0.0
                        if (titleInput.isNotBlank() && amt > 0) {
                            onCreateDraftExpense(titleInput, categoryInput, amt, dateInput, methodInput, descInput)
                            showAddExpenseDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Text(if (isBn) "ব্যয় খসড়া সংরক্ষণ করুন" else "Save Expense Draft")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddExpenseDialog = false }) {
                    Text(if (isBn) "বাতিল" else "Cancel")
                }
            },
            title = {
                Text(
                    text = if (isBn) "নতুন খরচ হিসাব এন্ট্রি (খসড়া)" else "New Expense Draft",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = titleInput,
                        onValueChange = { titleInput = it },
                        label = { Text(if (isBn) "খরচের খাত / শিরোনাম *" else "Expense Title *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = categoryInput,
                        onValueChange = { categoryInput = it },
                        label = { Text(if (isBn) "বিভাগ (পূজা/সংস্কার/বিদ্যুৎ/ভোগ)" else "Category") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = amountInput,
                        onValueChange = { amountInput = it },
                        label = { Text(if (isBn) "খরচের পরিমাণ (টাকা) *" else "Amount (BDT) *") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = descInput,
                        onValueChange = { descInput = it },
                        label = { Text(if (isBn) "ভাউচার / অতিরিক্ত বিবরণ" else "Voucher / Description") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        )
    }
}
