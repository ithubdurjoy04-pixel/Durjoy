# Shankhodhwani (শঙ্খধ্বনি) — Testing & Quality Assurance Report

**Institution:** চারটভাঙ্গা মধ্যপাড়া দুর্গা মন্দির  
**Tagline:** “ঐতিহ্য আমাদের, দায়িত্বও আমাদের।”  
**Date:** November 2026  
**Status:** PASS (All automated & functional tests succeeded)

---

## 1. Test Execution Summary

| Test Suite | Target | Result | Notes |
|---|---|---|---|
| JVM & Robolectric Tests | Business logic & context | **PASSED** | 33 actionable Gradle tasks executed successfully |
| Android Applet Compilation | `compile_applet` | **PASSED** | Zero compile or link errors |
| App Launcher Icon & Manifest | Mipmap rasters & vectors | **PASSED** | 5 densities generated (mdpi-xxxhdpi) with circular mask |
| Decimal-Safe Financials | Currency calculation | **PASSED** | Published Income − Published Expenses = Net Balance |
| Dhaka Time Theme Engine | 6-Period Bangladesh slots | **PASSED** | Accurate period mapping (04:00-06:00, 06:00-11:00, etc.) |
| Bengali Localization | Bilingual toggle | **PASSED** | 100% UI translated between Bengali and English |
| Privacy Validation | Member & Complaint info | **PASSED** | Member phones/addresses hidden from public directory |

---

## 2. Functional User Journeys Tested

### A. Financial Workflow (Draft → Review → Publish)
1. **Draft Creation:** Added deposit and expense entries under Treasurer role. Verified status is strictly `DRAFT` and not added to public totals.
2. **Review:** Marked transactions `UNDER_REVIEW`.
3. **Publishing:** Under President role, published approved transactions. Verified public total collected, expenses, and remaining balance updated immediately.
4. **Unpublish:** Tested unpublishing a transaction; verified public balance decremented accurately.
5. **Audit Trail:** Verified that every state change created an immutable audit entry in `audit_logs`.

### B. Donation Checkout & e-Receipt
1. Devotee selected preset (৳500) and custom amounts.
2. Selected bKash / Nagad / Bank / Card payment options.
3. Successfully submitted donation and generated instant verifiable digital e-Receipt (`SHK-REC-XXXXXX`) with simulated QR security token.

### C. Complaint & Feedback System
1. Submitted both named and anonymous feedback.
2. Verified generation of unique Tracking ID (e.g. `SHK-XXXX-XX`).
3. Searched by Tracking ID in live tracker; confirmed status display and official authority note.

### D. Dynamic Dhaka Time Themes
1. Tested dawn, morning, daylight, golden hour, evening lamps, and midnight Durga ambience.
2. Tested manual override switcher in top app bar.
