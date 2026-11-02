package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.DonationEntity
import com.example.domain.model.AppLanguage
import com.example.domain.model.TempleTheme
import com.example.ui.theme.*
import com.example.util.DhakaTimeHelper
import com.example.util.Localization

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShankhodhwaniTopAppBar(
    currentTheme: TempleTheme,
    language: AppLanguage,
    dhakaTimeStr: String,
    onLanguageToggle: () -> Unit,
    onThemeClick: () -> Unit,
    onAdminClick: () -> Unit,
    isAdminLoggedIn: Boolean
) {
    val isBn = language == AppLanguage.BENGALI

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(BrandMaroon)
                        .border(1.5.dp, BrandGold, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = "Shankhodhwani Emblem",
                        modifier = Modifier.size(32.dp)
                    )
                }
                Column {
                    Text(
                        text = Localization.appBrand(isBn),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = BrandMaroonLight
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = Localization.templeName(isBn),
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        },
        actions = {
            // Theme Indicator & Switcher Button
            IconButton(
                onClick = onThemeClick,
                modifier = Modifier
                    .testTag("theme_toggle_btn")
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = when (currentTheme) {
                        TempleTheme.DAWN_MISTY -> Icons.Default.WbTwilight
                        TempleTheme.SUNRISE_MORNING -> Icons.Default.WbSunny
                        TempleTheme.DAYLIGHT -> Icons.Default.LightMode
                        TempleTheme.GOLDEN_HOUR -> Icons.Default.Brightness5
                        TempleTheme.EVENING_LAMPS -> Icons.Default.LocalFireDepartment
                        TempleTheme.NIGHT_DURGA -> Icons.Default.Nightlight
                    },
                    contentDescription = "Theme switcher",
                    tint = BrandGold
                )
            }

            // Language Switcher Button (বাংলা / EN)
            Surface(
                onClick = onLanguageToggle,
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier
                    .testTag("lang_toggle_btn")
                    .padding(horizontal = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Translate,
                        contentDescription = "Translate",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isBn) "EN" else "বাং",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            // Admin Portal Icon
            IconButton(
                onClick = onAdminClick,
                modifier = Modifier
                    .testTag("admin_portal_btn")
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = if (isAdminLoggedIn) Icons.Default.AdminPanelSettings else Icons.Default.Lock,
                    contentDescription = "Admin login",
                    tint = if (isAdminLoggedIn) StatusSuccess else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    )
}

@Composable
fun TimeThemePill(
    theme: TempleTheme,
    dhakaTimeStr: String,
    dhakaDateStr: String,
    isAuto: Boolean,
    isBn: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        ),
        modifier = modifier
            .fillMaxWidth()
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                RoundedCornerShape(16.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 14.dp, vertical = 10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(BrandGold.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Time",
                        tint = BrandGold,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "$dhakaTimeStr (${Localization.currentDhakaTime(isBn)})",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isBn) theme.nameBn else theme.nameEn,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isAuto) BrandMaroon.copy(alpha = 0.1f) else BrandGold.copy(alpha = 0.2f)
            ) {
                Text(
                    text = if (isAuto) Localization.autoThemeBadge(isBn) else Localization.manualThemeBadge(isBn),
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.SemiBold),
                    color = if (isAuto) BrandMaroonLight else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun FinancialHeroCard(
    totalIncome: Double,
    totalExpenses: Double,
    balance: Double,
    isBn: Boolean,
    onDonateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = BrandMaroon
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(1.5.dp, BrandGold.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccountBalance,
                        contentDescription = "Finance",
                        tint = BrandGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isBn) "আর্থিক স্বচ্ছতা হিসাব" else "Financial Transparency",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = BrandGold.copy(alpha = 0.25f)
                ) {
                    Text(
                        text = if (isBn) "যাচাইকৃত হিসাব" else "Audited",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = BrandGoldLight,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Balance Highlight
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black.copy(alpha = 0.2f))
                    .padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = Localization.currentBalance(isBn),
                    style = MaterialTheme.typography.labelMedium,
                    color = BrandGoldLight.copy(alpha = 0.9f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = DhakaTimeHelper.formatCurrency(balance, isBn),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = BrandGold
                    )
                )
                Text(
                    text = Localization.transparencyNotice(isBn),
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = Color.White.copy(alpha = 0.6f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Income vs Expense Sub-cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.12f))
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = "Income",
                                tint = StatusSuccess,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = Localization.totalCollected(isBn),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = DhakaTimeHelper.formatCurrency(totalIncome, isBn),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.12f))
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.TrendingDown,
                                contentDescription = "Expense",
                                tint = Color(0xFFF87171),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = Localization.totalExpenses(isBn),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = DhakaTimeHelper.formatCurrency(totalExpenses, isBn),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Quick Donate CTA Button
            Button(
                onClick = onDonateClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandGold,
                    contentColor = BrandMaroonDark
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hero_donate_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.VolunteerActivism,
                    contentDescription = "Donate",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = Localization.quickDonate(isBn),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
fun StatusBadge(
    status: String,
    isBn: Boolean,
    modifier: Modifier = Modifier
) {
    val (label, bg, fg) = when (status.uppercase()) {
        "PUBLISHED" -> Triple(
            if (isBn) "প্রকাশিত" else "Published",
            StatusSuccess.copy(alpha = 0.15f),
            StatusSuccess
        )
        "DRAFT" -> Triple(
            if (isBn) "খসড়া" else "Draft",
            StatusDraft.copy(alpha = 0.15f),
            StatusDraft
        )
        "UNDER_REVIEW" -> Triple(
            if (isBn) "পর্যালোচনাধীন" else "Under Review",
            StatusWarning.copy(alpha = 0.15f),
            StatusWarning
        )
        "REJECTED" -> Triple(
            if (isBn) "বাতিল" else "Rejected",
            StatusDanger.copy(alpha = 0.15f),
            StatusDanger
        )
        "UNPUBLISHED" -> Triple(
            if (isBn) "অপ্রকাশিত" else "Unpublished",
            Color.Gray.copy(alpha = 0.15f),
            Color.Gray
        )
        "RESOLVED" -> Triple(
            if (isBn) "নিষ্পত্তি হয়েছে" else "Resolved",
            StatusSuccess.copy(alpha = 0.15f),
            StatusSuccess
        )
        "NEW" -> Triple(
            if (isBn) "নতুন দাখিল" else "New",
            StatusInfo.copy(alpha = 0.15f),
            StatusInfo
        )
        else -> Triple(status, Color.LightGray.copy(alpha = 0.2f), Color.DarkGray)
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = bg,
        modifier = modifier
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 11.sp),
            color = fg,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

@Composable
fun SectionHeader(
    title: String,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = BrandMaroonLight,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(10.dp))
        Divider(
            modifier = Modifier.weight(1f),
            color = BrandGold.copy(alpha = 0.3f),
            thickness = 1.dp
        )
    }
}

@Composable
fun ReceiptCard(
    donation: DonationEntity,
    isBn: Boolean,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(1.5.dp, BrandGold, RoundedCornerShape(18.dp))
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Receipt Header
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(BrandMaroon),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Success",
                    tint = BrandGold,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (isBn) "শ্রী শ্রী দুর্গা পূজা ও মন্দির সেবা" else "Chartabhanga Durga Mandir",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = if (isBn) "অফিসিয়াল দানপত্র ও ই-রসিদ" else "Official Donation e-Receipt",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = BrandMaroonLight
            )

            Spacer(modifier = Modifier.height(14.dp))
            Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(12.dp))

            // Details table
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ReceiptRow(
                    label = if (isBn) "রসিদ নং:" else "Receipt No:",
                    value = donation.receiptNumber
                )
                ReceiptRow(
                    label = if (isBn) "ট্রানজ্যাকশন আইডি:" else "Txn Ref:",
                    value = donation.transactionRef
                )
                ReceiptRow(
                    label = if (isBn) "দাতার নাম:" else "Donor:",
                    value = donation.donorName
                )
                ReceiptRow(
                    label = if (isBn) "উদ্দেশ্য:" else "Purpose:",
                    value = donation.purpose
                )
                ReceiptRow(
                    label = if (isBn) "পেমেন্ট মাধ্যম:" else "Method:",
                    value = donation.paymentMethod
                )
                ReceiptRow(
                    label = if (isBn) "অনুদানের পরিমাণ:" else "Amount:",
                    value = DhakaTimeHelper.formatCurrency(donation.amount, isBn),
                    isBold = true,
                    highlightColor = BrandMaroonLight
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Simulated QR Verification Box
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray.copy(alpha = 0.15f))
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.QrCode,
                    contentDescription = "QR Code",
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = if (isBn) "ডিজিটাল সুরক্ষা কোড" else "Digital Verification Token",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = if (isBn) "এই রসিদটি মন্দির ডাটাবেজে আনুষ্ঠানিকভাবে সংরক্ষিত।" else "Digitally validated & recorded in temple ledger.",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onClose,
                colors = ButtonDefaults.buttonColors(containerColor = BrandMaroon),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(Localization.btnClose(isBn))
            }
        }
    }
}

@Composable
private fun ReceiptRow(
    label: String,
    value: String,
    isBold: Boolean = false,
    highlightColor: Color = Color.Unspecified
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (isBold) FontWeight.Bold else FontWeight.Medium
            ),
            color = if (highlightColor != Color.Unspecified) highlightColor else MaterialTheme.colorScheme.onSurface
        )
    }
}
