package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.AdminRole
import com.example.domain.model.AppLanguage
import com.example.domain.model.FinancialSummary
import com.example.ui.components.SectionHeader
import com.example.ui.theme.*
import com.example.util.DhakaTimeHelper
import com.example.util.Localization

@Composable
fun AdminDashboardScreen(
    language: AppLanguage,
    currentRole: AdminRole?,
    financialSummary: FinancialSummary,
    memberCount: Int,
    complaintCount: Int,
    noticeCount: Int,
    onLoginRole: (AdminRole) -> Unit,
    onLogout: () -> Unit,
    onNavigateToFinanceWorkflow: () -> Unit,
    onNavigateToMembers: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToComplaints: () -> Unit
) {
    val isBn = language == AppLanguage.BENGALI

    if (currentRole == null) {
        // ADMIN LOGIN / ROLE SELECTOR
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("admin_login_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = BrandMaroon),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = BrandGold, modifier = Modifier.size(32.dp))
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (isBn) "প্রশাসনিক নিয়ন্ত্রণ কক্ষ" else "Administrative Control Center",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = Color.White)
                        )
                        Text(
                            text = if (isBn) "ভূমিকা নির্বাচন করে প্রবেশ করুন" else "Select official role to access authorized portal",
                            style = MaterialTheme.typography.bodySmall,
                            color = BrandGoldLight
                        )
                    }
                }
            }

            item {
                Text(
                    text = if (isBn) "প্রশাসনিক ৩টি প্রধান ভূমিকা:" else "Three Official Admin Roles:",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            item {
                RoleLoginCard(
                    role = AdminRole.PRESIDENT,
                    badgeColor = Color(0xFF6B21A8),
                    description = if (isBn) "সার্বিক তত্ত্বাবধান, আর্থিক অনুমোদন/বাতিল, প্রকাশ/অপ্রকাশ এবং অডিট লগ পর্যবেক্ষণ।" else "Overall oversight, financial approval, publish/unpublish, and audit log inspection.",
                    isBn = isBn,
                    onSelect = { onLoginRole(AdminRole.PRESIDENT) }
                )
            }

            item {
                RoleLoginCard(
                    role = AdminRole.SECRETARY,
                    badgeColor = Color(0xFF0D9488),
                    description = if (isBn) "সদস্য ব্যবস্থাপনা, নোটিশ বোর্ড, ধর্মীয় পর্ব, চিত্রশালা ও অভিযোগ নিষ্পত্তিকরণ।" else "Member management, announcements, events, gallery, and complaint resolution.",
                    isBn = isBn,
                    onSelect = { onLoginRole(AdminRole.SECRETARY) }
                )
            }

            item {
                RoleLoginCard(
                    role = AdminRole.TREASURER,
                    badgeColor = BrandGold,
                    description = if (isBn) "সদস্য জমা এন্ট্রি, খরচ হিসাব দাখিল, খসড়া প্রস্তুত এবং আর্থিক রসিদ ও প্রতিবেদন প্রস্তুত।" else "Member deposit entries, expense drafts, receipt generation, and financial reports.",
                    isBn = isBn,
                    onSelect = { onLoginRole(AdminRole.TREASURER) }
                )
            }
        }
    } else {
        // LOGGED-IN ADMIN DASHBOARD
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("admin_dashboard_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Active Role Header Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.5.dp, BrandGold),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(BrandMaroon),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = BrandGold, modifier = Modifier.size(22.dp))
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = if (isBn) currentRole.titleBn else currentRole.titleEn,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = BrandMaroonLight)
                                    )
                                    Text(
                                        text = if (isBn) "সক্রিয় সেশন • চারটভাঙ্গা মন্দির" else "Active Session • Chartabhanga Mandir",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                }
                            }

                            TextButton(
                                onClick = onLogout,
                                colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFDC2626))
                            ) {
                                Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(if (isBn) "লগআউট" else "Logout")
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = BrandMaroon.copy(alpha = 0.08f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = when (currentRole) {
                                    AdminRole.PRESIDENT -> if (isBn) "অনুমতি: পূর্ণ প্রশাসনিক অধিকার, আর্থিক অনুমোদন/বাতিল ও সার্বিক প্রকাশনা।" else "Permissions: Full system governance, financial approval/rejection & publication."
                                    AdminRole.SECRETARY -> if (isBn) "অনুমতি: সদস্য তালিকা, নোটিশ, ধর্মীয় পর্ব ও অভিযোগ তদারকি।" else "Permissions: Member directory, notices, events, and complaints management."
                                    AdminRole.TREASURER -> if (isBn) "অনুমতি: জমা ও খরচের খসড়া তৈরি, প্রতিবেদন তৈরি ও ভাউচার যাচাই।" else "Permissions: Deposit & expense entry drafts, reports, and financial auditing."
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = BrandMaroonLight,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }

            // Financial Summary Metrics
            item {
                SectionHeader(title = if (isBn) "আর্থিক কর্মপ্রবাহ সারসংক্ষেপ" else "Financial Workflow Status")
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = if (isBn) "খসড়া জমা" else "Draft Deposits",
                        value = DhakaTimeHelper.formatCurrency(financialSummary.totalDraftDeposits, isBn),
                        color = StatusWarning,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = if (isBn) "খসড়া ব্যয়" else "Draft Expenses",
                        value = DhakaTimeHelper.formatCurrency(financialSummary.totalDraftExpenses, isBn),
                        color = Color(0xFFEF4444),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = if (isBn) "প্রকাশিত উদ্বৃত্ত" else "Published Balance",
                        value = DhakaTimeHelper.formatCurrency(financialSummary.currentBalance, isBn),
                        color = StatusSuccess,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = if (isBn) "পর্যালোচনা বাকি" else "Pending Review",
                        value = "${financialSummary.pendingReviewCount}",
                        color = BrandMaroonLight,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Management Action Tiles
            item {
                SectionHeader(title = if (isBn) "প্রশাসনিক মডিউলসমূহ" else "Administrative Modules")
            }

            item {
                AdminModuleTile(
                    icon = Icons.Default.PriceCheck,
                    title = if (isBn) "আর্থিক কর্মপ্রবাহ (খসড়া → পর্যালোচনা → প্রকাশ)" else "Financial Workflow (Draft → Review → Publish)",
                    subtitle = if (isBn) "জমা/ব্যয় এন্ট্রি, অনুমোদন ও প্রকাশ ব্যবস্থাপনা" else "Deposit/expense draft review and public ledger sync",
                    color = BrandMaroonLight,
                    onClick = onNavigateToFinanceWorkflow
                )
            }

            item {
                AdminModuleTile(
                    icon = Icons.Default.People,
                    title = if (isBn) "সদস্য ব্যবস্থাপনা ও জমা রেকর্ড" else "Member Management & Ledger",
                    subtitle = if (isBn) "নতুন সদস্য অন্তর্ভুক্তি ও ব্যক্তিগত খতিয়ান" else "Register members and view deposit history",
                    color = Color(0xFF0D9488),
                    onClick = onNavigateToMembers
                )
            }

            item {
                AdminModuleTile(
                    icon = Icons.Default.RateReview,
                    title = if (isBn) "অভিযোগ ও পরামর্শ পোর্টাল পরিচালনা" else "Complaint & Feedback Portal",
                    subtitle = if (isBn) "প্রাপ্ত অভিযোগ পর্যালোচনা, নিষ্পত্তি ও মন্তব্য" else "Review complaints, update status, and add notes",
                    color = Color(0xFFD97706),
                    onClick = onNavigateToComplaints
                )
            }

            item {
                AdminModuleTile(
                    icon = Icons.Default.Tune,
                    title = if (isBn) "সিস্টেম সেটিংস, থিম ও অডিট লগ" else "System Settings, Themes & Audit Log",
                    subtitle = if (isBn) "বাংলাদেশ সময়সূচী, সামাজিক মাধ্যম লিংক ও অডিট" else "Dhaka time schedules, social links, and audit history",
                    color = Color(0xFF4F46E5),
                    onClick = onNavigateToSettings
                )
            }
        }
    }
}

@Composable
private fun RoleLoginCard(
    role: AdminRole,
    badgeColor: Color,
    description: String,
    isBn: Boolean,
    onSelect: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(badgeColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Badge, contentDescription = null, tint = badgeColor, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isBn) role.titleBn else role.titleEn,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            Icon(Icons.Default.ArrowForwardIos, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = color)
            )
        }
    }
}

@Composable
private fun AdminModuleTile(
    icon: ImageVector,
    title: String,
    subtitle: String,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                )
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
        }
    }
}
