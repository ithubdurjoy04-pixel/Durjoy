package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        MemberEntity::class,
        DepositEntity::class,
        ExpenseEntity::class,
        DonationEntity::class,
        NoticeEntity::class,
        EventEntity::class,
        GalleryEntity::class,
        ComplaintEntity::class,
        SocialLinkEntity::class,
        AuditLogEntity::class,
        SiteSettingsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun memberDao(): MemberDao
    abstract fun financeDao(): FinanceDao
    abstract fun donationDao(): DonationDao
    abstract fun noticeDao(): NoticeDao
    abstract fun eventDao(): EventDao
    abstract fun galleryDao(): GalleryDao
    abstract fun complaintDao(): ComplaintDao
    abstract fun socialDao(): SocialDao
    abstract fun auditDao(): AuditDao
    abstract fun settingsDao(): SettingsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "shankhodhwani.db"
                ).addCallback(DatabaseCallback(context))
                 .fallbackToDestructiveMigration()
                 .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(
        private val context: Context
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Initial seed for system settings and platform placeholders.
            // Per project requirement: Business tables (members, deposits, expenses, donations)
            // start completely empty (Initial balance: ৳0.00, Income: ৳0.00, Expenses: ৳0.00).
            CoroutineScope(Dispatchers.IO).launch {
                val database = getInstance(context)
                
                // Initialize Site Settings
                database.settingsDao().saveSettings(
                    SiteSettingsEntity(
                        id = 1,
                        templeNameBn = "চারটভাঙ্গা মধ্যপাড়া দুর্গা মন্দির",
                        templeNameEn = "Chartabhanga Madhyapara Durga Mandir",
                        taglineBn = "“ঐতিহ্য আমাদের, দায়িত্বও আমাদের।”",
                        taglineEn = "\"Our tradition, our responsibility.\"",
                        contactPhone = "01779760693",
                        contactEmail = "shankhodhwoni@gmail.com",
                        addressBn = "চারটভাঙ্গা মধ্যপাড়া, চারটভাঙ্গা",
                        addressEn = "Chartabhanga Madhyapara, Chartabhanga",
                        currentThemeMode = "AUTO",
                        manualThemeId = "DAYLIGHT",
                        language = "bn"
                    )
                )

                // Initialize Social Links in Coming Soon status
                val defaultSocials = listOf(
                    SocialLinkEntity("facebook", "Facebook", "", isEnabled = false, isComingSoon = true),
                    SocialLinkEntity("youtube", "YouTube", "", isEnabled = false, isComingSoon = true),
                    SocialLinkEntity("instagram", "Instagram", "", isEnabled = false, isComingSoon = true),
                    SocialLinkEntity("whatsapp", "WhatsApp", "", isEnabled = false, isComingSoon = true),
                    SocialLinkEntity("telegram", "Telegram", "", isEnabled = false, isComingSoon = true),
                    SocialLinkEntity("tiktok", "TikTok", "", isEnabled = false, isComingSoon = true),
                    SocialLinkEntity("twitter", "X (Twitter)", "", isEnabled = false, isComingSoon = true)
                )
                database.socialDao().insertSocialLinks(defaultSocials)

                // Initialize welcome announcement
                database.noticeDao().insertNotice(
                    NoticeEntity(
                        titleBn = "শঙ্খধ্বনি ডিজিটাল ইকোসিস্টেমে স্বাগতম",
                        titleEn = "Welcome to Shankhodhwani Digital Ecosystem",
                        contentBn = "চারটভাঙ্গা মধ্যপাড়া দুর্গা মন্দিরের সার্বিক উন্নয়ন, আর্থিক স্বচ্ছতা ও ভক্ত সেবায় শঙ্খধ্বনি প্ল্যাটফর্ম চালু করা হলো।",
                        contentEn = "Shankhodhwani platform is officially launched for temple development, financial transparency, and community service.",
                        publishDate = "২০২৬",
                        isUrgent = true,
                        isPublished = true
                    )
                )

                // Initial Temple Upcoming Events
                database.eventDao().insertEvent(
                    EventEntity(
                        titleBn = "শ্রী শ্রী শারদীয়া দুর্গোৎসব ২০২৬",
                        titleEn = "Shree Shree Sharadiya Durga Puja 2026",
                        dateStr = "২০২৬ শরৎকাল",
                        timeStr = "প্রতিদিন সকাল ৮:০০ - রাত ১০:০০",
                        locationBn = "মন্দির প্রাঙ্গণ, চারটভাঙ্গা মধ্যপাড়া",
                        locationEn = "Temple Premises, Chartabhanga Madhyapara",
                        descriptionBn = "শ্রীশ্রী দুর্গা মায়ের মহাস্নান, অঞ্জলি, চণ্ডীপাঠ ও প্রসাদ বিতরণ মহোৎসব।",
                        descriptionEn = "Maha Snan, Anjali, Chandi recitation, and sacred Mahaprasad distribution.",
                        isUpcoming = true,
                        isPublished = true
                    )
                )
                database.eventDao().insertEvent(
                    EventEntity(
                        titleBn = "মাসিক পূর্ণিমা পূজা ও নামকীর্তন",
                        titleEn = "Monthly Purnima Puja & Kirtan",
                        dateStr = "প্রতি পূর্ণিমা তিথি",
                        timeStr = "সন্ধ্যা ৬:৩০",
                        locationBn = "মূল নাটমন্দির",
                        locationEn = "Main Natmandir",
                        descriptionBn = "বিশেষ আরতি, ভোগ নিবেদন ও ভক্তিগীতি পরিবেশন।",
                        descriptionEn = "Special evening Aarti, Bhog offering, and spiritual devotional singing.",
                        isUpcoming = true,
                        isPublished = true
                    )
                )

                // Initial Gallery Highlights
                database.galleryDao().insertGallery(
                    GalleryEntity(
                        titleBn = "মন্দিরের মূল গর্ভগৃহ ও মহালক্ষ্মী আভা",
                        titleEn = "Temple Sanctum Sanctorum & Sacred Aura",
                        albumName = "মন্দির পরিচিতি",
                        mediaType = "IMAGE",
                        mediaUrl = "",
                        caption = "শ্রী শ্রী দুর্গা মায়ের পরম পবিত্র বেদি ও আলোকময় মন্দির প্রাঙ্গণ।",
                        isPublished = true
                    )
                )

                // Initial Audit Log
                database.auditDao().insertLog(
                    AuditLogEntity(
                        adminRole = "SYSTEM",
                        action = "SYSTEM_INITIALIZED",
                        targetEntity = "DATABASE",
                        targetId = "1",
                        details = "Fresh database initialized with zero business records per project requirements."
                    )
                )
            }
        }
    }
}
