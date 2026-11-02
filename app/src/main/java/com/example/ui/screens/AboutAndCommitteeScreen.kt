package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.MemberEntity
import com.example.domain.model.AppLanguage
import com.example.ui.components.SectionHeader
import com.example.ui.theme.BrandGold
import com.example.ui.theme.BrandMaroon
import com.example.ui.theme.BrandMaroonLight
import com.example.ui.theme.StatusSuccess
import com.example.util.Localization

data class CommitteeProfile(
    val roleBn: String,
    val roleEn: String,
    val nameBn: String,
    val nameEn: String,
    val descriptionBn: String,
    val descriptionEn: String
)

@Composable
fun AboutAndCommitteeScreen(
    language: AppLanguage,
    members: List<MemberEntity>
) {
    val isBn = language == AppLanguage.BENGALI
    var selectedTab by remember { mutableStateOf(0) } // 0: Temple Heritage, 1: Committee, 2: Public Members

    val committeeList = listOf(
        CommitteeProfile(
            "সভাপতি", "President",
            "শ্রী অমল কৃষ্ণ মজুমদার", "Shri Amal Krishna Majumdar",
            "মন্দিরের সার্বিক পরিচালনা, সমন্বয় ও ধর্মীয় নীতি নির্ধারণ।",
            "Overall temple administration, coordination, and institutional governance."
        ),
        CommitteeProfile(
            "সাধারণ সম্পাদক", "General Secretary",
            "শ্রী সুব্রত রায়", "Shri Subrata Roy",
            "সদস্য ব্যবস্থাপনা, নোটিশ ও সামাজিক উন্নয়ন কার্যক্রমের বাস্তবায়ন।",
            "Member relations, announcements, communications, and executive execution."
        ),
        CommitteeProfile(
            "কোষাধ্যক্ষ", "Treasurer",
            "শ্রী নিখিল চন্দ্র পাল", "Shri Nikhil Chandra Paul",
            "আর্থিক হিসাব, সদস্য জমা গ্রহণ, ব্যয় নিয়ন্ত্রণ ও বার্ষিক প্রতিবেদন।",
            "Financial accounting, member deposits, expense reviews, and fiscal transparency."
        ),
        CommitteeProfile(
            "সাংগঠনিক সম্পাদক", "Organizing Secretary",
            "শ্রী বিশ্বজিৎ দাস", "Shri Biswajit Das",
            "উৎসব প্রস্তুতি, ভলান্টিয়ার ব্যবস্থাপনা ও মন্দির নিরাপত্তা তদারকি।",
            "Festival preparation, volunteer coordination, and logistics management."
        )
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Tab row
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
                    text = { Text(if (isBn) "ঐতিহ্য ও ইতিহাস" else "History", fontSize = 12.sp) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text(if (isBn) "কমিটি" else "Committee", fontSize = 12.sp) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text(if (isBn) "সদস্যবৃন্দ" else "Members", fontSize = 12.sp) }
                )
            }
        }

        if (selectedTab == 0) {
            // ABOUT & HERITAGE
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.TempleHindu, contentDescription = null, tint = BrandMaroonLight, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = Localization.templeName(isBn),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = BrandMaroonLight
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = Localization.tagline(isBn),
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold, color = BrandGold)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (isBn)
                                "চারটভাঙ্গা মধ্যপাড়া দুর্গা মন্দির অত্র অঞ্চলের সনাতনী ধর্মাবলম্বীদের এক ঐতিহ্যবাহী ও জাগ্রত পুণ্যস্থান। বহু দশক ধরে ঐতিহ্যবাহী শ্রী শ্রী শারদীয়া দুর্গোৎসব, শ্যামা পূজা, সরস্বতী পূজা ও নিয়মিত ধর্মীয় কীর্তন অনুষ্ঠানের মাধ্যমে মন্দিরটি সাম্প্রদায়িক সম্প্রীতি ও আধ্যাত্মিক চেতনার কেন্দ্রবিন্দু হিসেবে পরিচালিত হয়ে আসছে।"
                            else
                                "Chartabhanga Madhyapara Durga Mandir is a historic and sacred sanctuary for devotees in the region. For decades, it has served as a beacon of spiritual devotion, cultural heritage, and communal harmony through Durga Puja, Kali Puja, and devotional assemblies.",
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 22.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        SectionHeader(title = if (isBn) "উদ্দেশ্য ও দর্শন" else "Mission & Vision")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isBn)
                                "১. সনাতন ধর্মীয় অনুশাসন, নিত্য পূজা ও পবিত্র ঐতিহ্য সংরক্ষণ।\n২. মন্দিরের সার্বিক অবকাঠামো সংস্কার ও আধুনিক ভক্ত সেবাকেন্দ্র গড়ে তোলা।\n৩. আর্থিক লেনদেনে সম্পূর্ণ স্বচ্ছতা ও জবাবদিহিতা নিশ্চিতকরণ।\n৪. তরুণ সমাজকে সমাজসেবা ও ধর্মীয় মূল্যবোধে উদ্বুদ্ধ করা।"
                            else
                                "1. Preserving sacred Sanatan traditions, daily prayers, and festive heritage.\n2. Modernizing temple infrastructure and devotee facilities.\n3. Ensuring absolute financial integrity, transparency, and public accountability.\n4. Inspiring youth towards charitable work and spiritual ethics.",
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 24.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
                        )
                    }
                }
            }
        } else if (selectedTab == 1) {
            // COMMITTEE
            item {
                SectionHeader(
                    title = if (isBn) "মন্দির পরিচালনা কমিটি" else "Executive Committee Members",
                    icon = Icons.Default.Groups
                )
            }

            items(committeeList) { profile ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(BrandMaroon),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isBn) profile.roleBn.take(2) else profile.roleEn.take(2),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = BrandGold)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = BrandMaroon.copy(alpha = 0.1f)
                            ) {
                                Text(
                                    text = if (isBn) profile.roleBn else profile.roleEn,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = BrandMaroonLight),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isBn) profile.nameBn else profile.nameEn,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (isBn) profile.descriptionBn else profile.descriptionEn,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        } else {
            // PUBLIC MEMBERS DIRECTORY (Privacy Protected)
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = StatusSuccess, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isBn)
                                "গোপনীয়তা সুরক্ষা: সদস্যদের ব্যক্তিগত মোবাইল নম্বর ও পূর্ণ ঠিকানা সর্বজনীনভাবে প্রকাশ করা হয় না।"
                            else
                                "Privacy Protected: Member mobile numbers and addresses are strictly concealed from the public directory.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            if (members.isEmpty()) {
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
                            Icon(Icons.Default.PersonOff, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(36.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (isBn) "কোনো সদস্য তালিকাভুক্ত নেই" else "No members registered yet",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = if (isBn) "প্রশাসনিক প্যানেল থেকে নতুন সদস্য অন্তর্ভুক্ত করা যাবে।" else "Members can be added via the administrative panel.",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            } else {
                items(members) { member ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(BrandMaroon.copy(alpha = 0.1f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = BrandMaroon, modifier = Modifier.size(20.dp))
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = member.fullName,
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = if (isBn) "সদস্য আইডি: ${member.memberCode}" else "ID: ${member.memberCode}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (member.status == "ACTIVE") StatusSuccess.copy(alpha = 0.15f) else Color.Gray.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = if (member.status == "ACTIVE") (if (isBn) "সক্রিয়" else "Active") else (if (isBn) "নিষ্ক্রিয়" else "Inactive"),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (member.status == "ACTIVE") StatusSuccess else Color.Gray
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
