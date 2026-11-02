package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.data.local.ComplaintEntity
import com.example.domain.model.AppLanguage
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatusBadge
import com.example.ui.theme.BrandGold
import com.example.ui.theme.BrandMaroon
import com.example.ui.theme.BrandMaroonLight
import com.example.ui.theme.StatusSuccess

@Composable
fun ComplaintScreen(
    language: AppLanguage,
    trackedComplaint: ComplaintEntity?,
    newTrackingId: String?,
    onSubmitComplaint: (name: String, phone: String, email: String, subject: String, message: String, isAnonymous: Boolean) -> Unit,
    onTrackComplaint: (trackingId: String) -> Unit,
    onResetTrackedComplaint: () -> Unit
) {
    val isBn = language == AppLanguage.BENGALI
    var selectedTab by remember { mutableStateOf(0) } // 0: Submit, 1: Track

    // Form fields
    var isAnonymous by remember { mutableStateOf(false) }
    var complainantName by remember { mutableStateOf("") }
    var complainantPhone by remember { mutableStateOf("") }
    var complainantEmail by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var searchTrackingId by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("complaint_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Tab switcher
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
                    text = { Text(if (isBn) "অভিযোগ দাখিল" else "Submit Feedback") },
                    icon = { Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text(if (isBn) "ট্র্যাকিং করুন" else "Track Status") },
                    icon = { Icon(Icons.Default.TrackChanges, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
            }
        }

        // Newly Generated Tracking ID Banner
        if (newTrackingId != null) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = BrandMaroon),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BrandGold, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (isBn) "আপনার অভিযোগ সফলভাবে দাখিল হয়েছে!" else "Complaint Submitted Successfully!",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color.White)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = BrandGold.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "${if (isBn) "ট্র্যাকিং আইডি: " else "Tracking ID: "}$newTrackingId",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = BrandGold),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (isBn) "ভবিষ্যতে অগ্রগতির খোঁজ নিতে এই আইডিটি সংরক্ষণ করুন।" else "Save this tracking ID to follow up on your complaint status.",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        if (selectedTab == 0) {
            // SUBMISSION FORM
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = if (isBn) "মন্দির কর্তৃপক্ষকে জানান" else "Submit Feedback or Complaint",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = BrandMaroonLight)
                        )
                        Text(
                            text = if (isBn)
                                "মন্দিরের উন্নয়ন, পূজা প্রস্তুতি বা ব্যবস্থাপনায় কোনো অসংগতি থাকলে সরাসরি অবহিত করুন। অভিযোগের তথ্য সম্পূর্ণ গোপন রাখা হয়।"
                            else
                                "Share feedback or report discrepancies directly to the temple committee. Submissions are strictly confidential.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )

                        // Anonymous Switch
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
                                text = if (isBn) "নাম গোপন রেখে বেনামে অভিযোগ দাখিল করুন" else "Submit anonymously without revealing identity",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        AnimatedVisibility(visible = !isAnonymous) {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedTextField(
                                    value = complainantName,
                                    onValueChange = { complainantName = it },
                                    label = { Text(if (isBn) "আপনার নাম" else "Full Name") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                OutlinedTextField(
                                    value = complainantPhone,
                                    onValueChange = { complainantPhone = it },
                                    label = { Text(if (isBn) "মোবাইল নম্বর" else "Phone Number") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                OutlinedTextField(
                                    value = complainantEmail,
                                    onValueChange = { complainantEmail = it },
                                    label = { Text(if (isBn) "ইমেইল (ঐচ্ছিক)" else "Email (Optional)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        OutlinedTextField(
                            value = subject,
                            onValueChange = { subject = it },
                            label = { Text(if (isBn) "অভিযোগ / পরামর্শের বিষয় *" else "Subject / Title *") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = message,
                            onValueChange = { message = it },
                            label = { Text(if (isBn) "বিস্তারিত বিবরণ *" else "Detailed Description *") },
                            minLines = 4,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = {
                                if (subject.isNotBlank() && message.isNotBlank()) {
                                    onSubmitComplaint(
                                        complainantName,
                                        complainantPhone,
                                        complainantEmail,
                                        subject,
                                        message,
                                        isAnonymous
                                    )
                                    // Reset inputs
                                    subject = ""
                                    message = ""
                                    complainantName = ""
                                    complainantPhone = ""
                                    complainantEmail = ""
                                }
                            },
                            enabled = subject.isNotBlank() && message.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(containerColor = BrandMaroon),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("submit_complaint_btn")
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null, tint = BrandGold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isBn) "অভিযোগ দাখিল করুন" else "Submit Complaint",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        } else {
            // TRACKING TAB
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = if (isBn) "অভিযোগের বর্তমান অবস্থা জানুন" else "Track Complaint Status",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = BrandMaroonLight)
                        )

                        OutlinedTextField(
                            value = searchTrackingId,
                            onValueChange = { searchTrackingId = it },
                            label = { Text(if (isBn) "ট্র্যাকিং আইডি লিখুন (উদাঃ SHK-1234-56)" else "Enter Tracking ID") },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = { onTrackComplaint(searchTrackingId) },
                            colors = ButtonDefaults.buttonColors(containerColor = BrandMaroon),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(if (isBn) "অনুসন্ধান করুন" else "Track Status")
                        }
                    }
                }
            }

            if (trackedComplaint != null) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, BrandGold.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = trackedComplaint.trackingId,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = BrandMaroonLight)
                                )
                                StatusBadge(status = trackedComplaint.status, isBn = isBn)
                            }

                            Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                            Text(
                                text = "${if (isBn) "বিষয়: " else "Subject: "}${trackedComplaint.subject}",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )

                            Text(
                                text = trackedComplaint.message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
                            )

                            if (trackedComplaint.adminNote.isNotBlank()) {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = BrandGold.copy(alpha = 0.15f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = if (isBn) "কর্তৃপক্ষের আনুষ্ঠানিক মন্তব্য:" else "Official Authority Note:",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = BrandMaroonLight)
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = trackedComplaint.adminNote,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
