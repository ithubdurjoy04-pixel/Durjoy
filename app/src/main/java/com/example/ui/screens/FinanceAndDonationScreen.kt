package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.example.data.local.DonationEntity
import com.example.data.local.ExpenseEntity
import com.example.domain.model.AppLanguage
import com.example.domain.model.FinancialSummary
import com.example.ui.components.FinancialHeroCard
import com.example.ui.components.ReceiptCard
import com.example.ui.components.SectionHeader
import com.example.ui.theme.*
import com.example.util.DhakaTimeHelper
import com.example.util.Localization

@Composable
fun FinanceAndDonationScreen(
    language: AppLanguage,
    financialSummary: FinancialSummary,
    deposits: List<DepositEntity>,
    expenses: List<ExpenseEntity>,
    donations: List<DonationEntity>,
    lastReceipt: DonationEntity?,
    onCloseReceipt: () -> Unit,
    onSubmitDonation: (
        name: String,
        phone: String,
        email: String,
        amount: Double,
        purpose: String,
        method: String,
        isAnonymous: Boolean,
        prayerNote: String
    ) -> Unit
) {
    val isBn = language == AppLanguage.BENGALI
    var selectedTab by remember { mutableStateOf(0) } // 0: Financials, 1: Donate

    // Donation Form States
    var selectedAmountPreset by remember { mutableStateOf(500.0) }
    var customAmountText by remember { mutableStateOf("") }
    var selectedPurpose by remember { mutableStateOf(if (isBn) "সাধারণ মন্দির তহবিল" else "General Temple Fund") }
    var selectedMethod by remember { mutableStateOf("bKash") }
    var donorName by remember { mutableStateOf("") }
    var donorPhone by remember { mutableStateOf("") }
    var donorEmail by remember { mutableStateOf("") }
    var prayerNote by remember { mutableStateOf("") }
    var isAnonymous by remember { mutableStateOf(false) }
    var manualTxnId by remember { mutableStateOf("") }

    val publishedDeposits = remember(deposits) { deposits.filter { it.status == "PUBLISHED" } }
    val publishedExpenses = remember(expenses) { expenses.filter { it.status == "PUBLISHED" } }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("finance_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Tab Switcher
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = BrandMaroonLight,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = if (isBn) "আর্থিক স্বচ্ছতা" else "Transparency",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    icon = { Icon(Icons.Default.AccountBalance, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = if (isBn) "অনলাইন অনুদান" else "Donate",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    icon = { Icon(Icons.Default.VolunteerActivism, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
            }
        }

        // Receipt Display if just completed
        if (lastReceipt != null) {
            item {
                ReceiptCard(
                    donation = lastReceipt,
                    isBn = isBn,
                    onClose = onCloseReceipt
                )
            }
        }

        if (selectedTab == 0) {
            // TAB 0: FINANCIAL TRANSPARENCY
            item {
                FinancialHeroCard(
                    totalIncome = financialSummary.totalPublishedIncome,
                    totalExpenses = financialSummary.totalPublishedExpense,
                    balance = financialSummary.currentBalance,
                    isBn = isBn,
                    onDonateClick = { selectedTab = 1 }
                )
            }

            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = BrandMaroonLight, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isBn) "স্বচ্ছতার নীতি" else "Transparency Policy",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isBn)
                                "উদ্বৃত্ত হিসাব = মোট প্রকাশিত আয় − মোট প্রকাশিত ব্যয়। কোনো প্রকার খসড়া বা অপ্রকাশিত লেনদেন এই হিসাবে যুক্ত হয় না।"
                            else
                                "Current Balance = Total Published Income - Total Published Expenses. Unreviewed or draft records are excluded.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                        )
                    }
                }
            }

            // Published Transactions
            item {
                SectionHeader(
                    title = if (isBn) "অনুমোদিত লেনদেন ইতিহাস" else "Published Transaction History",
                    icon = Icons.Default.ReceiptLong
                )
            }

            if (publishedDeposits.isEmpty() && publishedExpenses.isEmpty() && donations.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.Savings, contentDescription = null, tint = BrandGold, modifier = Modifier.size(40.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (isBn) "কোনো প্রকাশিত লেনদেন নেই" else "No published transactions yet",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = if (isBn) "প্রশাসনিক অনুমোদন ও প্রকাশের পর এখানে সর্বজনীন হিসাব দৃশ্যমান হবে।" else "Once administrative review and publication occur, records will appear here.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }

            // Published Deposits
            items(publishedDeposits) { deposit ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(StatusSuccess.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.ArrowDownward, contentDescription = null, tint = StatusSuccess, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isBn) "সদস্য জমা: ${deposit.memberName}" else "Member Deposit: ${deposit.memberName}",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "${deposit.paymentMethod} • ${deposit.depositDate}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        Text(
                            text = "+${DhakaTimeHelper.formatCurrency(deposit.amount, isBn)}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = StatusSuccess)
                        )
                    }
                }
            }

            // Published Expenses
            items(publishedExpenses) { expense ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFEE2E2)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.ArrowUpward, contentDescription = null, tint = Color(0xFFDC2626), modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = expense.title,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "${expense.category} • ${expense.expenseDate}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        Text(
                            text = "-${DhakaTimeHelper.formatCurrency(expense.amount, isBn)}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                        )
                    }
                }
            }
        } else {
            // TAB 1: DONATION CHECKOUT SYSTEM
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = if (isBn) "অনুদানের পরিমাণ নির্ধারণ করুন" else "Select Donation Amount",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )

                        // Amount Presets
                        val presets = listOf(100.0, 500.0, 1000.0, 2500.0, 5000.0)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            presets.forEach { preset ->
                                val isSelected = selectedAmountPreset == preset && customAmountText.isEmpty()
                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        selectedAmountPreset = preset
                                        customAmountText = ""
                                    },
                                    label = { Text("৳${preset.toInt()}") },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = BrandMaroon,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }

                        // Custom Amount Field
                        OutlinedTextField(
                            value = customAmountText,
                            onValueChange = { customAmountText = it },
                            label = { Text(if (isBn) "অন্যান্য পরিমাণ লিখুন (টাকা)" else "Other Amount (BDT)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            leadingIcon = { Text("৳", fontWeight = FontWeight.Bold) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                        // Purpose Dropdown / Selector
                        Text(
                            text = if (isBn) "অনুদানের খাত / উদ্দেশ্য" else "Donation Purpose",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        val purposes = listOf(
                            if (isBn) "সাধারণ মন্দির তহবিল" else "General Temple Fund",
                            if (isBn) "মন্দির সংস্কার ও উন্নয়ন" else "Temple Renovation",
                            if (isBn) "শারদীয়া দুর্গোৎসব সেবা" else "Durga Puja Sponsorship",
                            if (isBn) "নিত্য ভোগ সেবা" else "Daily Bhog Offering",
                            if (isBn) "অন্নদান ও প্রসাদ বিতরণ" else "Prasad Distribution"
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            purposes.take(2).forEach { p ->
                                FilterChip(
                                    selected = selectedPurpose == p,
                                    onClick = { selectedPurpose = p },
                                    label = { Text(p, fontSize = 12.sp) }
                                )
                            }
                        }

                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                        // Payment Method Selection
                        Text(
                            text = if (isBn) "পেমেন্ট মাধ্যম বেছে নিন" else "Choose Payment Method",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("bKash", "Nagad", "Bank", "Card").forEach { method ->
                                FilterChip(
                                    selected = selectedMethod == method,
                                    onClick = { selectedMethod = method },
                                    label = { Text(method, fontWeight = FontWeight.Bold) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = BrandGold,
                                        selectedLabelColor = BrandMaroonDark
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        // Configured payment instructions shown inside flow per Prompt Section 10
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = when (selectedMethod) {
                                        "bKash" -> if (isBn) "বিকাশ মার্চেন্ট / পার্সোনাল নম্বর: 01779760693 (Send Money / Payment)" else "bKash Account: 01779760693 (Send Money / Payment)"
                                        "Nagad" -> if (isBn) "নগদ পার্সোনাল নম্বর: 01779760693 (Send Money)" else "Nagad Account: 01779760693 (Send Money)"
                                        "Bank" -> if (isBn) "ব্যাংক: সোনালী ব্যাংক পিএলসি, হিসাব: চারটভাঙ্গা মধ্যপাড়া দুর্গা মন্দির" else "Bank: Sonali Bank PLC, A/C: Chartabhanga Durga Mandir"
                                        else -> if (isBn) "SSLCOMMERZ ভিসা/মাস্টারকার্ড সুরক্ষিত পেমেন্ট গেটওয়ে" else "SSLCOMMERZ Secure Card Gateway"
                                    },
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = BrandMaroonLight
                                )
                            }
                        }

                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                        // Donor Details
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { isAnonymous = !isAnonymous }
                        ) {
                            Checkbox(
                                checked = isAnonymous,
                                onCheckedChange = { isAnonymous = it }
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isBn) "বেনামী / অজ্ঞাত ভক্ত হিসেবে দান করুন" else "Donate anonymously",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        AnimatedVisibility(visible = !isAnonymous) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = donorName,
                                    onValueChange = { donorName = it },
                                    label = { Text(if (isBn) "আপনার নাম" else "Your Name") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                OutlinedTextField(
                                    value = donorPhone,
                                    onValueChange = { donorPhone = it },
                                    label = { Text(if (isBn) "মোবাইল নম্বর (ঐচ্ছিক)" else "Mobile Phone (Optional)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        OutlinedTextField(
                            value = prayerNote,
                            onValueChange = { prayerNote = it },
                            label = { Text(if (isBn) "প্রার্থনা বা আশীর্বাদ প্রার্থনা (ঐচ্ছিক)" else "Prayer request / Notes (Optional)") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Submit Button
                        Button(
                            onClick = {
                                val finalAmount = customAmountText.toDoubleOrNull() ?: selectedAmountPreset
                                if (finalAmount > 0) {
                                    onSubmitDonation(
                                        donorName,
                                        donorPhone,
                                        donorEmail,
                                        finalAmount,
                                        selectedPurpose,
                                        selectedMethod,
                                        isAnonymous,
                                        prayerNote
                                    )
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BrandMaroon),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("submit_donation_btn")
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BrandGold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isBn) "অনুদানের অর্থ প্রদান নিশ্চিত করুন" else "Confirm & Complete Donation",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }
    }
}
