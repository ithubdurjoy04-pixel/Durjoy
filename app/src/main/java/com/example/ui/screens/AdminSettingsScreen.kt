package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.AuditLogEntity
import com.example.data.local.ComplaintEntity
import com.example.data.local.SocialLinkEntity
import com.example.domain.model.AppLanguage
import com.example.domain.model.TempleTheme
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdminSettingsScreen(
    language: AppLanguage,
    isAutoTheme: Boolean,
    currentTheme: TempleTheme,
    socialLinks: List<SocialLinkEntity>,
    complaints: List<ComplaintEntity>,
    auditLogs: List<AuditLogEntity>,
    onBack: () -> Unit,
    onToggleAutoTheme: (Boolean) -> Unit,
    onSelectManualTheme: (TempleTheme) -> Unit,
    onUpdateSocialLink: (SocialLinkEntity) -> Unit,
    onUpdateComplaintStatus: (ComplaintEntity, String, String) -> Unit
) {
    val isBn = language == AppLanguage.BENGALI
    var selectedTab by remember { mutableStateOf(0) } // 0: Themes, 1: Socials, 2: Complaints, 3: Audit Logs

    var editingSocialLink by remember { mutableStateOf<SocialLinkEntity?>(null) }
    var editingComplaint by remember { mutableStateOf<ComplaintEntity?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("admin_settings_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
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
                        text = if (isBn) "সিস্টেম সেটিংস ও অডিট লগ" else "System Settings & Audit Log",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = BrandMaroonLight)
                    )
                    Text(
                        text = if (isBn) "থিম সময়সূচী, সামাজিক মাধ্যম ও অডিট ট্রেইল" else "Theme schedules, social media slots & audit trail",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }

        // Tab Selector
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
                    text = { Text(if (isBn) "থিম" else "Themes", fontSize = 11.sp) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text(if (isBn) "সোশ্যাল" else "Social", fontSize = 11.sp) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text(if (isBn) "অভিযোগ" else "Complaints", fontSize = 11.sp) }
                )
                Tab(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    text = { Text(if (isBn) "অডিট লগ" else "Audit Log", fontSize = 11.sp) }
                )
            }
        }

        if (selectedTab == 0) {
            // THEME SETTINGS (Prompt Section 4)
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isBn) "স্বয়ংক্রিয় সময়-ভিত্তিক থিম (ঢাকা সময়)" else "Auto Time-Based Theme (Dhaka Time)",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = if (isBn) "বাংলাদেশ স্থানীয় সময় অনুযায়ী প্রতি প্রহরের আবহ পরিবর্তিত হবে।" else "Automatically shifts temple ambience per Bangladesh local time.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                        Switch(
                            checked = isAutoTheme,
                            onCheckedChange = onToggleAutoTheme,
                            colors = SwitchDefaults.colors(checkedThumbColor = BrandGold, checkedTrackColor = BrandMaroon)
                        )
                    }
                }
            }

            item {
                SectionHeader(title = if (isBn) "৬টি প্রহর ও পরিবেশ তালিকা" else "Six Temple Ambience Schedules")
            }

            items(TempleTheme.values().toList()) { theme ->
                val isCurrent = currentTheme == theme
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCurrent) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surface
                    ),
                    border = if (isCurrent) BorderStroke(1.5.dp, BrandGold) else null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectManualTheme(theme) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (isBn) theme.nameBn else theme.nameEn,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = BrandMaroon.copy(alpha = 0.1f)
                                ) {
                                    Text(
                                        text = theme.timeSlot,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = BrandMaroonLight),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (isBn) theme.descriptionBn else theme.descriptionEn,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }

                        if (isCurrent) {
                            Icon(Icons.Default.CheckCircle, contentDescription = "Active", tint = BrandGold)
                        }
                    }
                }
            }
        } else if (selectedTab == 1) {
            // SOCIAL MEDIA CHANNELS (Prompt Section 12)
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = if (isBn) "সামাজিক যোগাযোগ মাধ্যম কনফিগারেশন" else "Social Media Configuration",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isBn)
                                "প্রাথমিকভাবে কোনো লিংক যুক্ত নেই (শীঘ্রই আসছে)। অ্যাডমিন লিংক প্রদান করলে সেটি অ্যাপ ও ওয়েবসাইটে সরাসরি কাজ করবে।"
                            else
                                "Initially unconfigured (Coming Soon). Once valid URLs are entered by Admin, links activate immediately.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                        )
                    }
                }
            }

            items(socialLinks) { link ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = link.name,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (link.isEnabled && link.url.isNotBlank()) StatusSuccess.copy(alpha = 0.15f) else BrandGold.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = if (link.isEnabled && link.url.isNotBlank()) (if (isBn) "সক্রিয়" else "Active") else (if (isBn) "শীঘ্রই আসছে" else "Coming Soon"),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (link.isEnabled && link.url.isNotBlank()) StatusSuccess else BrandMaroonLight
                                        ),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (link.url.isBlank()) (if (isBn) "লিংক কনফিগার করা হয়নি" else "No URL configured") else link.url,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }

                        IconButton(onClick = { editingSocialLink = link }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = BrandMaroonLight)
                        }
                    }
                }
            }
        } else if (selectedTab == 2) {
            // COMPLAINTS MANAGEMENT
            item {
                SectionHeader(title = if (isBn) "দাখিলকৃত সকল অভিযোগ ও পরামর্শ" else "All Received Complaints")
            }

            if (complaints.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = if (isBn) "বর্তমানে কোনো অভিযোগ জমা পড়েনি।" else "No complaints submitted yet.",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(20.dp)
                        )
                    }
                }
            } else {
                items(complaints) { complaint ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = complaint.trackingId,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = BrandMaroonLight)
                                )
                                StatusBadge(status = complaint.status, isBn = isBn)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${if (isBn) "বিষয়: " else "Subject: "}${complaint.subject}",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = complaint.message,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                            if (complaint.complainantName.isNotBlank() && complaint.complainantName != "অজ্ঞাত (Anonymous)") {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${if (isBn) "প্রেরক: " else "From: "}${complaint.complainantName} • ${complaint.phone}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                            if (complaint.adminNote.isNotBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = BrandGold.copy(alpha = 0.15f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "${if (isBn) "অফিসিয়াল মন্তব্য: " else "Official Note: "}${complaint.adminNote}",
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = { editingComplaint = complaint },
                                colors = ButtonDefaults.buttonColors(containerColor = BrandMaroon),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(if (isBn) "অগ্রগতি ও মন্তব্য হালনাগাদ করুন" else "Update Status & Note", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        } else {
            // AUDIT LOGS (Prompt Section 16 & 20)
            item {
                SectionHeader(title = if (isBn) "সিস্টেম অডিট ট্রেইল ও কার্যক্রম লগ" else "System Audit History")
            }

            if (auditLogs.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = if (isBn) "কোনো অডিট লগ পাওয়া যায়নি।" else "No audit logs recorded yet.",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(20.dp)
                        )
                    }
                }
            } else {
                items(auditLogs) { log ->
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = BrandMaroon.copy(alpha = 0.1f)
                                ) {
                                    Text(
                                        text = "${log.adminRole} • ${log.action}",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = BrandMaroonLight),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                val dateStr = SimpleDateFormat("HH:mm, dd/MM", Locale.US).format(Date(log.timestamp))
                                Text(
                                    text = dateStr,
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = log.details,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }
        }
    }

    // EDIT SOCIAL LINK DIALOG
    if (editingSocialLink != null) {
        var urlInput by remember { mutableStateOf(editingSocialLink!!.url) }
        var isEnabled by remember { mutableStateOf(editingSocialLink!!.isEnabled) }

        AlertDialog(
            onDismissRequest = { editingSocialLink = null },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateSocialLink(editingSocialLink!!.copy(url = urlInput, isEnabled = isEnabled, isComingSoon = urlInput.isBlank()))
                        editingSocialLink = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandMaroon)
                ) {
                    Text(if (isBn) "হালনাগাদ করুন" else "Update")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingSocialLink = null }) {
                    Text(if (isBn) "বাতিল" else "Cancel")
                }
            },
            title = {
                Text(text = "${editingSocialLink!!.name} ${if (isBn) "লিংক সেটিংস" else "Link Settings"}", fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = urlInput,
                        onValueChange = { urlInput = it },
                        label = { Text("URL (https://...)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = isEnabled, onCheckedChange = { isEnabled = it })
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isBn) "প্লাটফর্ম সক্রিয় করুন" else "Enable platform")
                    }
                }
            }
        )
    }

    // UPDATE COMPLAINT STATUS DIALOG
    if (editingComplaint != null) {
        var selectedStatus by remember { mutableStateOf(editingComplaint!!.status) }
        var adminNoteInput by remember { mutableStateOf(editingComplaint!!.adminNote) }

        AlertDialog(
            onDismissRequest = { editingComplaint = null },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateComplaintStatus(editingComplaint!!, selectedStatus, adminNoteInput)
                        editingComplaint = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandMaroon)
                ) {
                    Text(if (isBn) "হালনাগাদ সংরক্ষণ করুন" else "Save Update")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingComplaint = null }) {
                    Text(if (isBn) "বাতিল" else "Cancel")
                }
            },
            title = {
                Text(
                    text = "${if (isBn) "অভিযোগ সমাধান: " else "Resolve Complaint: "}${editingComplaint!!.trackingId}",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(if (isBn) "বর্তমান অবস্থা পরিবর্তন করুন:" else "Change Status:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        listOf("NEW", "UNDER_REVIEW", "RESOLVED", "REJECTED").forEach { status ->
                            FilterChip(
                                selected = selectedStatus == status,
                                onClick = { selectedStatus = status },
                                label = { Text(status, fontSize = 10.sp) }
                            )
                        }
                    }
                    OutlinedTextField(
                        value = adminNoteInput,
                        onValueChange = { adminNoteInput = it },
                        label = { Text(if (isBn) "কর্তৃপক্ষের আনুষ্ঠানিক মন্তব্য" else "Official Authority Note") },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        )
    }
}
