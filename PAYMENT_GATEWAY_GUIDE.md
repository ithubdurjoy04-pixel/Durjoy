# Shankhodhwani Payment Gateway Configuration Guide

Chartabhanga Madhyapara Durga Mandir

---

## 1. Supported Donation Channels

The platform provides a unified donation checkout engine supporting:
- **bKash (বিকাশ)**: Merchant / Personal Send Money & Checkout
- **Nagad (নগদ)**: Personal / Merchant Transfer
- **Bank Transfer (ব্যাংক হিসাব)**: Direct institutional deposit with Sonali Bank PLC
- **Debit / Credit Cards**: Visa, Mastercard, Internet Banking via SSLCOMMERZ

---

## 2. SSLCOMMERZ Integration Setup

### Environment Variables
Configure the following in the Render / Backend settings:
```env
SSLCOMMERZ_STORE_ID=your_store_id_here
SSLCOMMERZ_STORE_PASS=your_store_password_here
SSLCOMMERZ_IS_LIVE=false # Set to true for live production transactions
SSLCOMMERZ_SUCCESS_URL=https://shankhodhwoni.netlify.app/api/payment/success
SSLCOMMERZ_FAIL_URL=https://shankhodhwoni.netlify.app/api/payment/fail
SSLCOMMERZ_CANCEL_URL=https://shankhodhwoni.netlify.app/api/payment/cancel
SSLCOMMERZ_IPN_URL=https://shankhodhwani-backend.onrender.com/api/payment/ipn
```

### Verification Rule
- A transaction is NEVER validated solely based on frontend redirect.
- The server calls the SSLCOMMERZ Order Validation API (`https://sandbox.sslcommerz.com/validator/api/validationserverAPI.php` or live endpoint) using `val_id` and checks that the amount and currency match exactly.
- Upon successful validation, the system writes the donation record and issues a cryptographically timestamped electronic donation receipt (`SHK-REC-XXXXXX`).

---

## 3. Manual Payment Instructions

As mandated by institutional governance guidelines, manual payment details (personal or merchant account numbers) are displayed exclusively within the checkout flow after the devotee selects bKash or Nagad, preventing sensitive accounts from being exposed on generic pages.
