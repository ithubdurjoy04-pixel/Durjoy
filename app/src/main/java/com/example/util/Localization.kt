package com.example.util

object Localization {
    // App Bar & Brand
    fun templeName(isBn: Boolean) = if (isBn) "চারটভাঙ্গা মধ্যপাড়া দুর্গা মন্দির" else "Chartabhanga Madhyapara Durga Mandir"
    fun appBrand(isBn: Boolean) = if (isBn) "শঙ্খধ্বনি" else "Shankhodhwani"
    fun tagline(isBn: Boolean) = if (isBn) "“ঐতিহ্য আমাদের, দায়িত্বও আমাদের।”" else "“Our tradition, our responsibility.”"

    // Navigation Tabs
    fun navHome(isBn: Boolean) = if (isBn) "মূলপাতা" else "Home"
    fun navNotices(isBn: Boolean) = if (isBn) "বিজ্ঞপ্তি ও পর্ব" else "Notices"
    fun navFinances(isBn: Boolean) = if (isBn) "হিসাব ও অনুদান" else "Finances"
    fun navHistory(isBn: Boolean) = if (isBn) "ঐতিহ্য ও চিত্র" else "Heritage"
    fun navPortal(isBn: Boolean) = if (isBn) "প্রশাসন ও সেবা" else "Admin & Portal"

    // Financial Cards
    fun totalCollected(isBn: Boolean) = if (isBn) "মোট সংগৃহীত" else "Total Collected"
    fun totalExpenses(isBn: Boolean) = if (isBn) "মোট ব্যয়" else "Total Expenses"
    fun currentBalance(isBn: Boolean) = if (isBn) "বর্তমান উদ্বৃত্ত" else "Remaining Balance"
    fun transparencyNotice(isBn: Boolean) = if (isBn) "কেবল অনুমোদিত ও প্রকাশিত হিসাব প্রদর্শিত" else "Showing only verified and published accounts"
    fun quickDonate(isBn: Boolean) = if (isBn) "অনুদান দিন" else "Donate Now"
    fun makeDeposit(isBn: Boolean) = if (isBn) "সদস্য জমা" else "Member Deposit"

    // Time-based Ambience
    fun currentDhakaTime(isBn: Boolean) = if (isBn) "বাংলাদেশ সময়" else "Bangladesh Time (Dhaka)"
    fun activeThemeLabel(isBn: Boolean) = if (isBn) "বর্তমান পরিবেশ" else "Current Atmosphere"
    fun autoThemeBadge(isBn: Boolean) = if (isBn) "স্বয়ংক্রিয় সময়সূচী" else "Auto Schedule"
    fun manualThemeBadge(isBn: Boolean) = if (isBn) "ম্যানুয়াল থিম" else "Manual Override"

    // Quick Action Buttons
    fun submitComplaint(isBn: Boolean) = if (isBn) "অভিযোগ / পরামর্শ" else "Feedback / Complaint"
    fun trackComplaint(isBn: Boolean) = if (isBn) "অভিযোগ ট্র্যাকিং" else "Track Complaint"
    fun viewCommittee(isBn: Boolean) = if (isBn) "কমিটি সদস্যবৃন্দ" else "Executive Committee"
    fun publicMembers(isBn: Boolean) = if (isBn) "সাধারণ সদস্য তালিকা" else "General Members"
    fun emergencyContact(isBn: Boolean) = if (isBn) "জরুরি যোগাযোগ" else "Emergency Contact"

    // Admin Roles
    fun rolePresident(isBn: Boolean) = if (isBn) "সভাপতি" else "President"
    fun roleSecretary(isBn: Boolean) = if (isBn) "সম্পাদক" else "Secretary"
    fun roleTreasurer(isBn: Boolean) = if (isBn) "কোষাধ্যক্ষ" else "Treasurer"

    // Workflow
    fun draftStatus(isBn: Boolean) = if (isBn) "খসড়া" else "Draft"
    fun underReviewStatus(isBn: Boolean) = if (isBn) "পর্যালোচনাধীন" else "Under Review"
    fun publishedStatus(isBn: Boolean) = if (isBn) "প্রকাশিত" else "Published"
    fun unpublishedStatus(isBn: Boolean) = if (isBn) "অপ্রকাশিত" else "Unpublished"
    fun rejectedStatus(isBn: Boolean) = if (isBn) "বাতিল" else "Rejected"

    // Actions
    fun btnPublish(isBn: Boolean) = if (isBn) "প্রকাশ করুন" else "Publish"
    fun btnUnpublish(isBn: Boolean) = if (isBn) "অপ্রকাশিত করুন" else "Unpublish"
    fun btnApprove(isBn: Boolean) = if (isBn) "অনুমোদন করুন" else "Approve"
    fun btnReject(isBn: Boolean) = if (isBn) "প্রত্যাখ্যান" else "Reject"
    fun btnEdit(isBn: Boolean) = if (isBn) "সম্পাদনা" else "Edit"
    fun btnDelete(isBn: Boolean) = if (isBn) "মুছুন" else "Delete"
    fun btnSave(isBn: Boolean) = if (isBn) "সংরক্ষণ করুন" else "Save"
    fun btnCancel(isBn: Boolean) = if (isBn) "বাতিল" else "Cancel"
    fun btnClose(isBn: Boolean) = if (isBn) "বন্ধ করুন" else "Close"
    fun btnSubmit(isBn: Boolean) = if (isBn) "দাখিল করুন" else "Submit"
}
