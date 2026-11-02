package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.EventEntity
import com.example.data.local.GalleryEntity
import com.example.data.local.NoticeEntity
import com.example.domain.model.AppLanguage
import com.example.domain.model.FinancialSummary
import com.example.domain.model.TempleTheme
import com.example.ui.components.FinancialHeroCard
import com.example.ui.components.SectionHeader
import com.example.ui.components.TimeThemePill
import com.example.ui.theme.*
import com.example.util.Localization

@Composable
fun HomeScreen(
    language: AppLanguage,
    activeTheme: TempleTheme,
    isAutoTheme: Boolean,
    dhakaTimeStr: String,
    dhakaDateStr: String,
    financialSummary: FinancialSummary,
    notices: List<NoticeEntity>,
    events: List<EventEntity>,
    gallery: List<GalleryEntity>,
    onDonateClick: () -> Unit,
    onViewFinancesClick: () -> Unit,
    onNoticesClick: () -> Unit,
    onComplaintClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    val context = LocalContext.current
    val isBn = language == AppLanguage.BENGALI

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Banner Card with Temple Image
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.temple_hero_cover_1793557949499),
                        contentDescription = "Temple Hero Banner",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.85f)
                                    )
                                )
                            )
                    )

                    // Overlay Content
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = BrandGold.copy(alpha = 0.9f)
                        ) {
                            Text(
                                text = Localization.appBrand(isBn),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BrandMaroonDark
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = Localization.templeName(isBn),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Text(
                            text = Localization.tagline(isBn),
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = BrandGoldLight
                            )
                        )
                    }
                }
            }
        }

        // Live Dhaka Time & Dynamic Ambience Pill
        item {
            TimeThemePill(
                theme = activeTheme,
                dhakaTimeStr = dhakaTimeStr,
                dhakaDateStr = dhakaDateStr,
                isAuto = isAutoTheme,
                isBn = isBn
            )
        }

        // Quick Access Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickActionItem(
                    icon = Icons.Default.VolunteerActivism,
                    title = if (isBn) "দান করুন" else "Donate",
                    subtitle = if (isBn) "বিকাশ/নগদ" else "bKash/Nagad",
                    color = BrandMaroonLight,
                    modifier = Modifier.weight(1f),
                    onClick = onDonateClick
                )
                QuickActionItem(
                    icon = Icons.Default.Campaign,
                    title = if (isBn) "বিজ্ঞপ্তি" else "Notices",
                    subtitle = if (isBn) "জরুরি বার্তা" else "Announcements",
                    color = Color(0xFF0D9488),
                    modifier = Modifier.weight(1f),
                    onClick = onNoticesClick
                )
                QuickActionItem(
                    icon = Icons.Default.RateReview,
                    title = if (isBn) "অভিযোগ" else "Complaint",
                    subtitle = if (isBn) "ট্র্যাকিং পোর্টাল" else "Track Portal",
                    color = Color(0xFFD97706),
                    modifier = Modifier.weight(1f),
                    onClick = onComplaintClick
                )
                QuickActionItem(
                    icon = Icons.Default.AccountBalance,
                    title = if (isBn) "ইতিহাস" else "History",
                    subtitle = if (isBn) "কমিটি ও সেবা" else "Committee",
                    color = Color(0xFF4F46E5),
                    modifier = Modifier.weight(1f),
                    onClick = onAboutClick
                )
            }
        }

        // Financial Transparency Summary
        item {
            FinancialHeroCard(
                totalIncome = financialSummary.totalPublishedIncome,
                totalExpenses = financialSummary.totalPublishedExpense,
                balance = financialSummary.currentBalance,
                isBn = isBn,
                onDonateClick = onDonateClick
            )
        }

        // Important Notices Section
        item {
            SectionHeader(
                title = if (isBn) "জরুরি নোটিশ বোর্ড" else "Important Notice Board",
                icon = Icons.Default.NotificationsActive
            )
        }

        if (notices.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = if (isBn) "কোনো নতুন বিজ্ঞপ্তি নেই।" else "No active notices at this moment.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        } else {
            items(notices.take(3)) { notice ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNoticesClick() },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(if (notice.isUrgent) Color(0xFFFEE2E2) else BrandGold.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (notice.isUrgent) Icons.Default.PriorityHigh else Icons.Default.Campaign,
                                contentDescription = null,
                                tint = if (notice.isUrgent) Color(0xFFDC2626) else BrandMaroonLight,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isBn) notice.titleBn else notice.titleEn,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (isBn) notice.contentBn else notice.contentEn,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        // Upcoming Events Section
        item {
            SectionHeader(
                title = if (isBn) "আসন্ন ধর্মীয় ও সাংস্কৃতিক পর্ব" else "Upcoming Temple Events",
                icon = Icons.Default.Event
            )
        }

        if (events.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = if (isBn) "শীঘ্রই পর্বের সময়সূচী প্রকাশিত হবে।" else "Event schedule will be posted soon.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        } else {
            items(events.take(2)) { event ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, BrandGold.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isBn) event.titleBn else event.titleEn,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BrandMaroonLight
                                )
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = BrandMaroon.copy(alpha = 0.1f)
                            ) {
                                Text(
                                    text = event.dateStr,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = BrandMaroon,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Place,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isBn) event.locationBn else event.locationEn,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (isBn) event.descriptionBn else event.descriptionEn,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        // Contact & Location Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isBn) "মন্দির যোগাযোগ ও ঠিকানা" else "Temple Contact & Address",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isBn) "চারটভাঙ্গা মধ্যপাড়া, চারটভাঙ্গা দুর্গা মন্দির প্রাঙ্গণ" else "Chartabhanga Madhyapara Durga Mandir Premises",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:01779760693"))
                                context.startActivity(intent)
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = "Call", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("01779760693")
                        }

                        OutlinedButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:shankhodhwoni@gmail.com"))
                                context.startActivity(intent)
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Email, contentDescription = "Email", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isBn) "ইমেইল" else "Email")
                        }
                    }
                }
            }
        }

        // Social Media Slots (Coming Soon status as specified in Prompt Section 12)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isBn) "সামাজিক যোগাযোগ মাধ্যম" else "Social Media Channels",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = BrandGold.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = if (isBn) "শীঘ্রই আসছে" else "Coming Soon",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = BrandMaroonLight,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = if (isBn)
                            "অফিসিয়াল ফেসবুক, ইউটিউব ও হোয়াটসঅ্যাপ চ্যানেল অতি শীঘ্রই সক্রিয় করা হবে।"
                        else
                            "Official Facebook, YouTube, and WhatsApp links will be configured soon.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickActionItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 6.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                maxLines = 1
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}
