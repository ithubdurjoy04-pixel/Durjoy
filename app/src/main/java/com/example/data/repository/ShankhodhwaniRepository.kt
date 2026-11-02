package com.example.data.repository

import com.example.data.local.*
import com.example.domain.model.AdminRole
import com.example.domain.model.FinancialSummary
import com.example.domain.model.TransactionStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.util.UUID

class ShankhodhwaniRepository(private val db: AppDatabase) {

    // Member methods
    val allMembers: Flow<List<MemberEntity>> = db.memberDao().getAllMembers()
    val activeMembers: Flow<List<MemberEntity>> = db.memberDao().getActiveMembers()

    suspend fun getMemberById(id: Long) = db.memberDao().getMemberById(id)

    suspend fun addMember(
        name: String,
        phone: String,
        address: String,
        notes: String,
        adminRole: String
    ): Long {
        val count = System.currentTimeMillis() % 10000
        val memberCode = "MBR-${String.format("%04d", count)}"
        val entity = MemberEntity(
            memberCode = memberCode,
            fullName = name.trim(),
            phone = phone.trim(),
            address = address.trim(),
            registrationDate = "২০২৬",
            notes = notes.trim(),
            status = "ACTIVE"
        )
        val id = db.memberDao().insertMember(entity)
        logAudit(adminRole, "ADD_MEMBER", "MEMBER", id.toString(), "Added member $name ($memberCode)")
        return id
    }

    suspend fun updateMember(member: MemberEntity, adminRole: String) {
        db.memberDao().updateMember(member)
        logAudit(adminRole, "UPDATE_MEMBER", "MEMBER", member.id.toString(), "Updated member ${member.fullName}")
    }

    suspend fun toggleMemberStatus(member: MemberEntity, adminRole: String) {
        val newStatus = if (member.status == "ACTIVE") "INACTIVE" else "ACTIVE"
        val updated = member.copy(status = newStatus)
        db.memberDao().updateMember(updated)
        logAudit(adminRole, "TOGGLE_MEMBER_STATUS", "MEMBER", member.id.toString(), "Status changed to $newStatus")
    }

    // Financial Draft -> Review -> Publish workflow
    val allDeposits: Flow<List<DepositEntity>> = db.financeDao().getAllDeposits()
    val allExpenses: Flow<List<ExpenseEntity>> = db.financeDao().getAllExpenses()

    val publishedDepositsTotal = db.financeDao().getPublishedDepositsTotal()
    val publishedExpensesTotal = db.financeDao().getPublishedExpensesTotal()
    val totalSuccessfulDonations = db.donationDao().getTotalSuccessfulDonations()

    val financialSummary: Flow<FinancialSummary> = combine(
        publishedDepositsTotal,
        publishedExpensesTotal,
        totalSuccessfulDonations
    ) { pubDep, pubExp, pubDon ->
        Triple(pubDep, pubExp, pubDon)
    }.combine(
        combine(
            db.financeDao().getDraftDepositsTotal(),
            db.financeDao().getDraftExpensesTotal(),
            db.financeDao().getPendingReviewDepositsCount()
        ) { draftDep, draftExp, pendingCount ->
            Triple(draftDep, draftExp, pendingCount)
        }
    ) { pubTriple, draftTriple ->
        val pubDep = pubTriple.first
        val pubExp = pubTriple.second
        val pubDon = pubTriple.third
        val draftDep = draftTriple.first
        val draftExp = draftTriple.second
        val pendingCount = draftTriple.third

        val totalIncome = pubDep + pubDon
        val balance = totalIncome - pubExp
        FinancialSummary(
            totalPublishedIncome = totalIncome,
            totalPublishedExpense = pubExp,
            currentBalance = balance,
            totalDraftDeposits = draftDep,
            totalDraftExpenses = draftExp,
            pendingReviewCount = pendingCount
        )
    }

    suspend fun createDraftDeposit(
        memberId: Long,
        memberName: String,
        amount: Double,
        depositDate: String,
        paymentMethod: String,
        transactionRef: String,
        description: String,
        adminRole: String
    ): Long {
        val entity = DepositEntity(
            memberId = memberId,
            memberName = memberName,
            amount = amount,
            depositDate = depositDate,
            paymentMethod = paymentMethod,
            transactionRef = transactionRef,
            description = description,
            status = TransactionStatus.DRAFT.name,
            createdByAdmin = adminRole
        )
        val id = db.financeDao().insertDeposit(entity)
        logAudit(adminRole, "CREATE_DRAFT_DEPOSIT", "DEPOSIT", id.toString(), "Created draft deposit of ৳$amount for $memberName")
        return id
    }

    suspend fun createDraftExpense(
        title: String,
        category: String,
        amount: Double,
        expenseDate: String,
        paymentMethod: String,
        description: String,
        receiptImage: String,
        adminRole: String
    ): Long {
        val entity = ExpenseEntity(
            title = title,
            category = category,
            amount = amount,
            expenseDate = expenseDate,
            paymentMethod = paymentMethod,
            description = description,
            receiptImage = receiptImage,
            status = TransactionStatus.DRAFT.name,
            createdByAdmin = adminRole
        )
        val id = db.financeDao().insertExpense(entity)
        logAudit(adminRole, "CREATE_DRAFT_EXPENSE", "EXPENSE", id.toString(), "Created draft expense: $title (৳$amount)")
        return id
    }

    // Step 2: Review
    suspend fun markDepositUnderReview(id: Long, adminRole: String) {
        val item = db.financeDao().getDepositById(id) ?: return
        db.financeDao().updateDeposit(item.copy(status = TransactionStatus.UNDER_REVIEW.name, reviewedByAdmin = adminRole))
        logAudit(adminRole, "REVIEW_DEPOSIT", "DEPOSIT", id.toString(), "Deposit marked under review")
    }

    suspend fun markExpenseUnderReview(id: Long, adminRole: String) {
        val item = db.financeDao().getExpenseById(id) ?: return
        db.financeDao().updateExpense(item.copy(status = TransactionStatus.UNDER_REVIEW.name, reviewedByAdmin = adminRole))
        logAudit(adminRole, "REVIEW_EXPENSE", "EXPENSE", id.toString(), "Expense marked under review")
    }

    // Step 4: Publish
    suspend fun publishDeposit(id: Long, adminRole: String) {
        val item = db.financeDao().getDepositById(id) ?: return
        db.financeDao().updateDeposit(item.copy(status = TransactionStatus.PUBLISHED.name, reviewedByAdmin = adminRole))
        // update member's total deposited
        if (item.memberId > 0) {
            val member = db.memberDao().getMemberById(item.memberId)
            if (member != null) {
                db.memberDao().updateMember(member.copy(totalDeposited = member.totalDeposited + item.amount))
            }
        }
        logAudit(adminRole, "PUBLISH_DEPOSIT", "DEPOSIT", id.toString(), "Published deposit ৳${item.amount}")
    }

    suspend fun publishExpense(id: Long, adminRole: String) {
        val item = db.financeDao().getExpenseById(id) ?: return
        db.financeDao().updateExpense(item.copy(status = TransactionStatus.PUBLISHED.name, reviewedByAdmin = adminRole))
        logAudit(adminRole, "PUBLISH_EXPENSE", "EXPENSE", id.toString(), "Published expense ৳${item.amount} (${item.title})")
    }

    // Step 6: Unpublish
    suspend fun unpublishDeposit(id: Long, adminRole: String) {
        val item = db.financeDao().getDepositById(id) ?: return
        db.financeDao().updateDeposit(item.copy(status = TransactionStatus.UNPUBLISHED.name))
        if (item.memberId > 0) {
            val member = db.memberDao().getMemberById(item.memberId)
            if (member != null) {
                val newTotal = maxOf(0.0, member.totalDeposited - item.amount)
                db.memberDao().updateMember(member.copy(totalDeposited = newTotal))
            }
        }
        logAudit(adminRole, "UNPUBLISH_DEPOSIT", "DEPOSIT", id.toString(), "Unpublished deposit ৳${item.amount}")
    }

    suspend fun unpublishExpense(id: Long, adminRole: String) {
        val item = db.financeDao().getExpenseById(id) ?: return
        db.financeDao().updateExpense(item.copy(status = TransactionStatus.UNPUBLISHED.name))
        logAudit(adminRole, "UNPUBLISH_EXPENSE", "EXPENSE", id.toString(), "Unpublished expense ৳${item.amount}")
    }

    suspend fun rejectDeposit(id: Long, adminRole: String) {
        val item = db.financeDao().getDepositById(id) ?: return
        db.financeDao().updateDeposit(item.copy(status = TransactionStatus.REJECTED.name, reviewedByAdmin = adminRole))
        logAudit(adminRole, "REJECT_DEPOSIT", "DEPOSIT", id.toString(), "Rejected deposit ৳${item.amount}")
    }

    suspend fun rejectExpense(id: Long, adminRole: String) {
        val item = db.financeDao().getExpenseById(id) ?: return
        db.financeDao().updateExpense(item.copy(status = TransactionStatus.REJECTED.name, reviewedByAdmin = adminRole))
        logAudit(adminRole, "REJECT_EXPENSE", "EXPENSE", id.toString(), "Rejected expense ৳${item.amount}")
    }

    // Donation Checkout
    val allDonations: Flow<List<DonationEntity>> = db.donationDao().getAllDonations()

    suspend fun processDonation(
        name: String,
        phone: String,
        email: String,
        amount: Double,
        purpose: String,
        method: String,
        isAnonymous: Boolean,
        prayerNote: String
    ): DonationEntity {
        val receiptNo = "SHK-REC-${System.currentTimeMillis() % 1000000}"
        val txRef = "TXN-${UUID.randomUUID().toString().substring(0, 8).uppercase()}"
        val entity = DonationEntity(
            donorName = if (isAnonymous) "অজ্ঞাত ভক্ত (Anonymous)" else name.ifBlank { "ভক্ত (Devotee)" },
            donorPhone = if (isAnonymous) "" else phone,
            donorEmail = if (isAnonymous) "" else email,
            amount = amount,
            purpose = purpose,
            paymentMethod = method,
            transactionRef = txRef,
            status = "SUCCESS",
            receiptNumber = receiptNo,
            isAnonymous = isAnonymous,
            prayerNote = prayerNote
        )
        val id = db.donationDao().insertDonation(entity)
        logAudit("PUBLIC_DONOR", "DONATION_RECEIVED", "DONATION", id.toString(), "Received donation of ৳$amount via $method ($receiptNo)")
        return entity.copy(id = id)
    }

    // Complaints
    val allComplaints: Flow<List<ComplaintEntity>> = db.complaintDao().getAllComplaints()

    suspend fun submitComplaint(
        name: String,
        phone: String,
        email: String,
        subject: String,
        message: String,
        isAnonymous: Boolean
    ): String {
        val trackingId = "SHK-${(1000..9999).random()}-${(10..99).random()}"
        val entity = ComplaintEntity(
            trackingId = trackingId,
            complainantName = if (isAnonymous) "অজ্ঞাত (Anonymous)" else name,
            phone = if (isAnonymous) "" else phone,
            email = if (isAnonymous) "" else email,
            subject = subject,
            message = message,
            status = "NEW",
            adminNote = ""
        )
        db.complaintDao().insertComplaint(entity)
        logAudit("PUBLIC_USER", "SUBMIT_COMPLAINT", "COMPLAINT", trackingId, "Submitted complaint with Tracking ID: $trackingId")
        return trackingId
    }

    suspend fun getComplaintByTrackingId(trackingId: String) = db.complaintDao().getComplaintByTrackingId(trackingId.trim())

    suspend fun updateComplaintStatus(
        complaint: ComplaintEntity,
        newStatus: String,
        adminNote: String,
        adminRole: String
    ) {
        val updated = complaint.copy(
            status = newStatus,
            adminNote = adminNote,
            updatedAt = System.currentTimeMillis()
        )
        db.complaintDao().updateComplaint(updated)
        logAudit(adminRole, "UPDATE_COMPLAINT_STATUS", "COMPLAINT", complaint.trackingId, "Status updated to $newStatus")
    }

    // Notices
    val publishedNotices: Flow<List<NoticeEntity>> = db.noticeDao().getPublishedNotices()
    val allNotices: Flow<List<NoticeEntity>> = db.noticeDao().getAllNotices()

    suspend fun createNotice(
        titleBn: String,
        titleEn: String,
        contentBn: String,
        contentEn: String,
        isUrgent: Boolean,
        adminRole: String
    ): Long {
        val entity = NoticeEntity(
            titleBn = titleBn,
            titleEn = titleEn,
            contentBn = contentBn,
            contentEn = contentEn,
            publishDate = "২০২৬",
            isUrgent = isUrgent,
            isPublished = true
        )
        val id = db.noticeDao().insertNotice(entity)
        logAudit(adminRole, "CREATE_NOTICE", "NOTICE", id.toString(), "Created notice: $titleBn")
        return id
    }

    suspend fun deleteNotice(notice: NoticeEntity, adminRole: String) {
        db.noticeDao().deleteNotice(notice)
        logAudit(adminRole, "DELETE_NOTICE", "NOTICE", notice.id.toString(), "Deleted notice: ${notice.titleBn}")
    }

    // Events
    val publishedEvents: Flow<List<EventEntity>> = db.eventDao().getPublishedEvents()
    val allEvents: Flow<List<EventEntity>> = db.eventDao().getAllEvents()

    suspend fun createEvent(
        titleBn: String,
        titleEn: String,
        dateStr: String,
        timeStr: String,
        locationBn: String,
        locationEn: String,
        descriptionBn: String,
        descriptionEn: String,
        adminRole: String
    ): Long {
        val entity = EventEntity(
            titleBn = titleBn,
            titleEn = titleEn,
            dateStr = dateStr,
            timeStr = timeStr,
            locationBn = locationBn,
            locationEn = locationEn,
            descriptionBn = descriptionBn,
            descriptionEn = descriptionEn,
            isUpcoming = true,
            isPublished = true
        )
        val id = db.eventDao().insertEvent(entity)
        logAudit(adminRole, "CREATE_EVENT", "EVENT", id.toString(), "Created event: $titleBn")
        return id
    }

    // Gallery
    val publishedGallery: Flow<List<GalleryEntity>> = db.galleryDao().getPublishedGallery()
    val allGallery: Flow<List<GalleryEntity>> = db.galleryDao().getAllGallery()

    suspend fun addGalleryItem(
        titleBn: String,
        titleEn: String,
        albumName: String,
        mediaType: String,
        caption: String,
        adminRole: String
    ): Long {
        val entity = GalleryEntity(
            titleBn = titleBn,
            titleEn = titleEn,
            albumName = albumName,
            mediaType = mediaType,
            mediaUrl = "",
            caption = caption,
            isPublished = true
        )
        val id = db.galleryDao().insertGallery(entity)
        logAudit(adminRole, "ADD_GALLERY", "GALLERY", id.toString(), "Added gallery item: $titleBn")
        return id
    }

    // Social Links
    val allSocialLinks: Flow<List<SocialLinkEntity>> = db.socialDao().getAllSocialLinks()

    suspend fun updateSocialLink(link: SocialLinkEntity, adminRole: String) {
        db.socialDao().updateSocialLink(link)
        logAudit(adminRole, "UPDATE_SOCIAL_LINK", "SOCIAL", link.platformKey, "Updated link for ${link.name}")
    }

    // Audit logs
    val auditLogs: Flow<List<AuditLogEntity>> = db.auditDao().getAllLogs()

    private suspend fun logAudit(
        role: String,
        action: String,
        targetEntity: String,
        targetId: String,
        details: String
    ) {
        db.auditDao().insertLog(
            AuditLogEntity(
                adminRole = role,
                action = action,
                targetEntity = targetEntity,
                targetId = targetId,
                details = details
            )
        )
    }

    // Site Settings
    val siteSettings: Flow<SiteSettingsEntity?> = db.settingsDao().getSettings()

    suspend fun updateSiteSettings(settings: SiteSettingsEntity, adminRole: String) {
        db.settingsDao().saveSettings(settings)
        logAudit(adminRole, "UPDATE_SETTINGS", "SETTINGS", "1", "Updated system and theme settings")
    }
}
