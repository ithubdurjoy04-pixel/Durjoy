package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.domain.model.AppLanguage
import com.example.domain.model.TempleTheme
import com.example.ui.components.ShankhodhwaniTopAppBar
import com.example.ui.screens.*
import com.example.ui.theme.BrandGold
import com.example.ui.theme.BrandMaroon
import com.example.ui.theme.BrandMaroonDark
import com.example.ui.theme.BrandMaroonLight
import com.example.ui.theme.ShankhodhwaniTheme
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.ShankhodhwaniViewModel
import com.example.util.Localization

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: ShankhodhwaniViewModel = viewModel()

            val language by viewModel.language.collectAsStateWithLifecycle()
            val activeTheme by viewModel.activeTheme.collectAsStateWithLifecycle()
            val isAutoTheme by viewModel.isAutoTheme.collectAsStateWithLifecycle()
            val dhakaTimeStr by viewModel.dhakaTimeString.collectAsStateWithLifecycle()
            val dhakaDateStr by viewModel.dhakaDateString.collectAsStateWithLifecycle()
            val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
            val currentAdminRole by viewModel.currentAdminRole.collectAsStateWithLifecycle()
            val financialSummary by viewModel.financialSummary.collectAsStateWithLifecycle()

            val allMembers by viewModel.allMembers.collectAsStateWithLifecycle()
            val allDeposits by viewModel.allDeposits.collectAsStateWithLifecycle()
            val allExpenses by viewModel.allExpenses.collectAsStateWithLifecycle()
            val allDonations by viewModel.allDonations.collectAsStateWithLifecycle()
            val publishedNotices by viewModel.publishedNotices.collectAsStateWithLifecycle()
            val publishedEvents by viewModel.publishedEvents.collectAsStateWithLifecycle()
            val publishedGallery by viewModel.publishedGallery.collectAsStateWithLifecycle()
            val allComplaints by viewModel.allComplaints.collectAsStateWithLifecycle()
            val socialLinks by viewModel.socialLinks.collectAsStateWithLifecycle()
            val auditLogs by viewModel.auditLogs.collectAsStateWithLifecycle()

            val feedbackMessage by viewModel.feedbackMessage.collectAsStateWithLifecycle()
            val lastReceipt by viewModel.lastDonationReceipt.collectAsStateWithLifecycle()
            val trackedComplaint by viewModel.trackedComplaintResult.collectAsStateWithLifecycle()
            val newTrackingId by viewModel.newComplaintTrackingId.collectAsStateWithLifecycle()

            var showThemeDialog by remember { mutableStateOf(false) }
            val snackbarHostState = remember { SnackbarHostState() }

            val isBn = language == AppLanguage.BENGALI

            LaunchedEffect(feedbackMessage) {
                feedbackMessage?.let { msg ->
                    snackbarHostState.showSnackbar(msg)
                    viewModel.clearFeedback()
                }
            }

            // Proper Back navigation handling
            BackHandler(enabled = currentScreen != Screen.Home) {
                when (currentScreen) {
                    Screen.AdminFinanceWorkflow, Screen.AdminMembers, Screen.AdminSettings -> {
                        viewModel.navigateTo(Screen.AdminDashboard)
                    }
                    else -> {
                        viewModel.navigateTo(Screen.Home)
                    }
                }
            }

            ShankhodhwaniTheme(activeTheme = activeTheme) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        ShankhodhwaniTopAppBar(
                            currentTheme = activeTheme,
                            language = language,
                            dhakaTimeStr = dhakaTimeStr,
                            onLanguageToggle = { viewModel.toggleLanguage() },
                            onThemeClick = { showThemeDialog = true },
                            onAdminClick = { viewModel.navigateTo(Screen.AdminDashboard) },
                            isAdminLoggedIn = currentAdminRole != null
                        )
                    },
                    bottomBar = {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = BrandMaroonLight,
                            windowInsets = WindowInsets.navigationBars,
                            modifier = Modifier.testTag("bottom_nav_bar")
                        ) {
                            NavigationBarItem(
                                selected = currentScreen == Screen.Home,
                                onClick = { viewModel.navigateTo(Screen.Home) },
                                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                                label = { Text(Localization.navHome(isBn), fontSize = 11.sp, maxLines = 1) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = BrandMaroonDark,
                                    indicatorColor = BrandGold
                                )
                            )
                            NavigationBarItem(
                                selected = currentScreen == Screen.NoticesEvents,
                                onClick = { viewModel.navigateTo(Screen.NoticesEvents) },
                                icon = { Icon(Icons.Default.Campaign, contentDescription = "Notices") },
                                label = { Text(Localization.navNotices(isBn), fontSize = 11.sp, maxLines = 1) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = BrandMaroonDark,
                                    indicatorColor = BrandGold
                                )
                            )
                            NavigationBarItem(
                                selected = currentScreen == Screen.FinancesDonation,
                                onClick = { viewModel.navigateTo(Screen.FinancesDonation) },
                                icon = { Icon(Icons.Default.AccountBalance, contentDescription = "Finances") },
                                label = { Text(Localization.navFinances(isBn), fontSize = 11.sp, maxLines = 1) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = BrandMaroonDark,
                                    indicatorColor = BrandGold
                                )
                            )
                            NavigationBarItem(
                                selected = currentScreen == Screen.HeritageGallery,
                                onClick = { viewModel.navigateTo(Screen.HeritageGallery) },
                                icon = { Icon(Icons.Default.PhotoLibrary, contentDescription = "Gallery") },
                                label = { Text(Localization.navHistory(isBn), fontSize = 11.sp, maxLines = 1) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = BrandMaroonDark,
                                    indicatorColor = BrandGold
                                )
                            )
                            NavigationBarItem(
                                selected = currentScreen is Screen.AdminDashboard ||
                                           currentScreen is Screen.AdminFinanceWorkflow ||
                                           currentScreen is Screen.AdminMembers ||
                                           currentScreen is Screen.AdminSettings,
                                onClick = { viewModel.navigateTo(Screen.AdminDashboard) },
                                icon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = "Portal") },
                                label = { Text(Localization.navPortal(isBn), fontSize = 11.sp, maxLines = 1) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = BrandMaroonDark,
                                    indicatorColor = BrandGold
                                )
                            )
                        }
                    },
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (currentScreen) {
                            Screen.Home -> HomeScreen(
                                language = language,
                                activeTheme = activeTheme,
                                isAutoTheme = isAutoTheme,
                                dhakaTimeStr = dhakaTimeStr,
                                dhakaDateStr = dhakaDateStr,
                                financialSummary = financialSummary,
                                notices = publishedNotices,
                                events = publishedEvents,
                                gallery = publishedGallery,
                                onDonateClick = { viewModel.navigateTo(Screen.FinancesDonation) },
                                onViewFinancesClick = { viewModel.navigateTo(Screen.FinancesDonation) },
                                onNoticesClick = { viewModel.navigateTo(Screen.NoticesEvents) },
                                onComplaintClick = { viewModel.navigateTo(Screen.ComplaintPortal) },
                                onAboutClick = { viewModel.navigateTo(Screen.AboutCommittee) }
                            )
                            Screen.NoticesEvents -> NoticeAndEventScreen(
                                language = language,
                                notices = publishedNotices,
                                events = publishedEvents
                            )
                            Screen.FinancesDonation -> FinanceAndDonationScreen(
                                language = language,
                                financialSummary = financialSummary,
                                deposits = allDeposits,
                                expenses = allExpenses,
                                donations = allDonations,
                                lastReceipt = lastReceipt,
                                onCloseReceipt = { viewModel.lastDonationReceipt.value = null },
                                onSubmitDonation = { name, phone, email, amount, purpose, method, anonymous, note ->
                                    viewModel.processDonation(name, phone, email, amount, purpose, method, anonymous, note)
                                }
                            )
                            Screen.HeritageGallery -> GalleryScreen(
                                language = language,
                                galleryItems = publishedGallery
                            )
                            Screen.ComplaintPortal -> ComplaintScreen(
                                language = language,
                                trackedComplaint = trackedComplaint,
                                newTrackingId = newTrackingId,
                                onSubmitComplaint = { name, phone, email, subject, message, anonymous ->
                                    viewModel.submitComplaint(name, phone, email, subject, message, anonymous)
                                },
                                onTrackComplaint = { trackingId ->
                                    viewModel.trackComplaint(trackingId)
                                },
                                onResetTrackedComplaint = {
                                    viewModel.trackedComplaintResult.value = null
                                }
                            )
                            Screen.AboutCommittee -> AboutAndCommitteeScreen(
                                language = language,
                                members = allMembers
                            )
                            Screen.AdminDashboard -> AdminDashboardScreen(
                                language = language,
                                currentRole = currentAdminRole,
                                financialSummary = financialSummary,
                                memberCount = allMembers.size,
                                complaintCount = allComplaints.size,
                                noticeCount = publishedNotices.size,
                                onLoginRole = { role -> viewModel.loginAsAdmin(role) },
                                onLogout = { viewModel.logoutAdmin() },
                                onNavigateToFinanceWorkflow = { viewModel.navigateTo(Screen.AdminFinanceWorkflow) },
                                onNavigateToMembers = { viewModel.navigateTo(Screen.AdminMembers) },
                                onNavigateToSettings = { viewModel.navigateTo(Screen.AdminSettings) },
                                onNavigateToComplaints = { viewModel.navigateTo(Screen.AdminSettings) }
                            )
                            Screen.AdminFinanceWorkflow -> {
                                if (currentAdminRole != null) {
                                    AdminFinanceWorkflowScreen(
                                        language = language,
                                        currentRole = currentAdminRole!!,
                                        members = allMembers,
                                        deposits = allDeposits,
                                        expenses = allExpenses,
                                        onBack = { viewModel.navigateTo(Screen.AdminDashboard) },
                                        onCreateDraftDeposit = { memberId, name, amt, date, method, ref, desc ->
                                            viewModel.createDraftDeposit(memberId, name, amt, date, method, ref, desc)
                                        },
                                        onCreateDraftExpense = { title, cat, amt, date, method, desc ->
                                            viewModel.createDraftExpense(title, cat, amt, date, method, desc)
                                        },
                                        onReviewDeposit = { id -> viewModel.markDepositUnderReview(id) },
                                        onReviewExpense = { id -> viewModel.markExpenseUnderReview(id) },
                                        onPublishDeposit = { id -> viewModel.publishDeposit(id) },
                                        onPublishExpense = { id -> viewModel.publishExpense(id) },
                                        onUnpublishDeposit = { id -> viewModel.unpublishDeposit(id) },
                                        onUnpublishExpense = { id -> viewModel.unpublishExpense(id) },
                                        onRejectDeposit = { id -> viewModel.rejectDeposit(id) },
                                        onRejectExpense = { id -> viewModel.rejectExpense(id) }
                                    )
                                } else {
                                    viewModel.navigateTo(Screen.AdminDashboard)
                                }
                            }
                            Screen.AdminMembers -> AdminMemberScreen(
                                language = language,
                                members = allMembers,
                                onBack = { viewModel.navigateTo(Screen.AdminDashboard) },
                                onAddMember = { name, phone, address, notes ->
                                    viewModel.addMember(name, phone, address, notes)
                                },
                                onToggleStatus = { member ->
                                    viewModel.toggleMemberStatus(member)
                                }
                            )
                            Screen.AdminSettings -> AdminSettingsScreen(
                                language = language,
                                isAutoTheme = isAutoTheme,
                                currentTheme = activeTheme,
                                socialLinks = socialLinks,
                                complaints = allComplaints,
                                auditLogs = auditLogs,
                                onBack = { viewModel.navigateTo(Screen.AdminDashboard) },
                                onToggleAutoTheme = { enabled -> viewModel.setAutoTheme(enabled) },
                                onSelectManualTheme = { theme -> viewModel.setManualTheme(theme) },
                                onUpdateSocialLink = { link -> viewModel.updateSocialLink(link) },
                                onUpdateComplaintStatus = { complaint, status, note ->
                                    viewModel.updateComplaintStatus(complaint, status, note)
                                }
                            )
                        }
                    }
                }

                // Quick Theme Picker Dialog
                if (showThemeDialog) {
                    AlertDialog(
                        onDismissRequest = { showThemeDialog = false },
                        confirmButton = {
                            TextButton(onClick = { showThemeDialog = false }) {
                                Text(if (isBn) "ঠিক আছে" else "Done")
                            }
                        },
                        title = {
                            Text(
                                text = if (isBn) "মন্দির পরিবেশ ও থিম পরিবর্তন" else "Temple Atmosphere & Theme",
                                fontWeight = FontWeight.Bold
                            )
                        },
                        text = {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { viewModel.setAutoTheme(true) }
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = if (isBn) "স্বয়ংক্রিয় বাংলাদেশ সময় (ঢাকা)" else "Auto Bangladesh Time (Dhaka)",
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = if (isBn) "দিন ও রাতের ৬টি প্রহরে পরিবর্তনশীল" else "Shifts across 6 periods of day & night",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                        )
                                    }
                                    RadioButton(selected = isAutoTheme, onClick = { viewModel.setAutoTheme(true) })
                                }

                                Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                                Text(
                                    text = if (isBn) "ম্যানুয়াল প্রহর নির্বাচন:" else "Manual Ambience Override:",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                )

                                TempleTheme.values().forEach { theme ->
                                    val isSelected = !isAutoTheme && activeTheme == theme
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { viewModel.setManualTheme(theme) }
                                            .padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = if (isBn) theme.nameBn else theme.nameEn,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                                )
                                            )
                                            Text(
                                                text = theme.timeSlot,
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                            )
                                        }
                                        RadioButton(selected = isSelected, onClick = { viewModel.setManualTheme(theme) })
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}
