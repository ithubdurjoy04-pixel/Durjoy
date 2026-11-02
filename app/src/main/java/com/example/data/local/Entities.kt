package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "members")
data class MemberEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val memberCode: String,
    val fullName: String,
    val phone: String,
    val address: String,
    val status: String = "ACTIVE", // ACTIVE or INACTIVE
    val registrationDate: String,
    val notes: String = "",
    val totalDeposited: Double = 0.0
)

@Entity(tableName = "deposits")
data class DepositEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val memberId: Long,
    val memberName: String,
    val amount: Double,
    val depositDate: String,
    val paymentMethod: String, // bKash, Nagad, Cash, Bank Transfer
    val transactionRef: String = "",
    val description: String = "",
    val status: String = "DRAFT", // DRAFT, UNDER_REVIEW, PUBLISHED, REJECTED, UNPUBLISHED
    val createdByAdmin: String,
    val reviewedByAdmin: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String, // Puja, Renovation, Electricity, Bhog, Maintenance, Misc
    val amount: Double,
    val expenseDate: String,
    val paymentMethod: String,
    val description: String = "",
    val receiptImage: String = "",
    val status: String = "DRAFT", // DRAFT, UNDER_REVIEW, PUBLISHED, REJECTED, UNPUBLISHED
    val createdByAdmin: String,
    val reviewedByAdmin: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "donations")
data class DonationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val donorName: String,
    val donorPhone: String = "",
    val donorEmail: String = "",
    val amount: Double,
    val purpose: String, // Temple Renovation, Puja Sponsorship, Bhog Offering, General Fund
    val paymentMethod: String, // bKash, Nagad, Bank, Card
    val transactionRef: String,
    val status: String = "SUCCESS",
    val receiptNumber: String,
    val isAnonymous: Boolean = false,
    val prayerNote: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "notices")
data class NoticeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val titleBn: String,
    val titleEn: String,
    val contentBn: String,
    val contentEn: String,
    val publishDate: String,
    val isUrgent: Boolean = false,
    val isPublished: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val titleBn: String,
    val titleEn: String,
    val dateStr: String,
    val timeStr: String,
    val locationBn: String,
    val locationEn: String,
    val descriptionBn: String,
    val descriptionEn: String,
    val isUpcoming: Boolean = true,
    val isPublished: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "gallery_items")
data class GalleryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val titleBn: String,
    val titleEn: String,
    val albumName: String,
    val mediaType: String, // IMAGE or VIDEO
    val mediaUrl: String,
    val caption: String = "",
    val isPublished: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "complaints")
data class ComplaintEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val trackingId: String,
    val complainantName: String = "",
    val phone: String = "",
    val email: String = "",
    val subject: String,
    val message: String,
    val status: String = "NEW", // NEW, UNDER_REVIEW, RESOLVED, REJECTED
    val adminNote: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "social_links")
data class SocialLinkEntity(
    @PrimaryKey val platformKey: String,
    val name: String,
    val url: String = "",
    val isEnabled: Boolean = false,
    val isComingSoon: Boolean = true
)

@Entity(tableName = "audit_logs")
data class AuditLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val adminRole: String,
    val action: String,
    val targetEntity: String,
    val targetId: String,
    val details: String
)

@Entity(tableName = "site_settings")
data class SiteSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val templeNameBn: String = "চারটভাঙ্গা মধ্যপাড়া দুর্গা মন্দির",
    val templeNameEn: String = "Chartabhanga Madhyapara Durga Mandir",
    val taglineBn: String = "“ঐতিহ্য আমাদের, দায়িত্বও আমাদের।”",
    val taglineEn: String = "\"Our tradition, our responsibility.\"",
    val contactPhone: String = "01779760693",
    val contactEmail: String = "shankhodhwoni@gmail.com",
    val addressBn: String = "চারটভাঙ্গা মধ্যপাড়া, চারটভাঙ্গা",
    val addressEn: String = "Chartabhanga Madhyapara, Chartabhanga",
    val currentThemeMode: String = "AUTO", // AUTO or MANUAL
    val manualThemeId: String = "DAYLIGHT",
    val language: String = "bn"
)
