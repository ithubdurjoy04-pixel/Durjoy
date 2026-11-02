-- =============================================================================
-- SHANKHODHWANI (শঙ্খধ্বনি) - CENTRALIZED POSTGRESQL DATABASE SCHEMA
-- চারটভাঙ্গা মধ্যপাড়া দুর্গা মন্দির
-- Version: 1.0.0
-- =============================================================================

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 1. ADMIN ROLES
CREATE TABLE admin_roles (
    id VARCHAR(50) PRIMARY KEY,
    title_bn VARCHAR(100) NOT NULL,
    title_en VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO admin_roles (id, title_bn, title_en, description) VALUES
('PRESIDENT', 'সভাপতি', 'President', 'Overall institutional governance, financial approval/rejection, publish/unpublish, and audit inspection.'),
('SECRETARY', 'সম্পাদক', 'Secretary', 'Member management, notice board, events, gallery, and complaint resolution.'),
('TREASURER', 'কোষাধ্যক্ষ', 'Treasurer', 'Member deposit entries, expense verification, draft preparation, receipt generation, and fiscal auditing.');

-- 2. USERS (ADMIN ACCOUNTS)
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(100) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role_id VARCHAR(50) NOT NULL REFERENCES admin_roles(id),
    full_name_bn VARCHAR(150) NOT NULL,
    full_name_en VARCHAR(150) NOT NULL,
    phone VARCHAR(30),
    is_active BOOLEAN DEFAULT TRUE,
    last_login TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 3. ROLE PERMISSIONS
CREATE TABLE role_permissions (
    id SERIAL PRIMARY KEY,
    role_id VARCHAR(50) REFERENCES admin_roles(id) ON DELETE CASCADE,
    permission_key VARCHAR(100) NOT NULL,
    UNIQUE(role_id, permission_key)
);

-- 4. MEMBERS DIRECTORY
CREATE TABLE members (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    member_code VARCHAR(50) UNIQUE NOT NULL,
    full_name VARCHAR(150) NOT NULL,
    phone VARCHAR(30) NOT NULL, -- Private: Not exposed publicly
    address TEXT NOT NULL,      -- Private: Not exposed publicly
    status VARCHAR(20) DEFAULT 'ACTIVE', -- ACTIVE / INACTIVE
    profile_photo_url TEXT,
    notes TEXT,
    total_deposited DECIMAL(12, 2) DEFAULT 0.00,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_members_code ON members(member_code);
CREATE INDEX idx_members_status ON members(status);

-- 5. MEMBER DEPOSITS (IRREGULAR DEPOSITS WORKFLOW)
CREATE TABLE deposits (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    member_id UUID REFERENCES members(id) ON DELETE SET NULL,
    member_name VARCHAR(150) NOT NULL,
    amount DECIMAL(12, 2) NOT NULL CHECK (amount > 0),
    deposit_date VARCHAR(50) NOT NULL,
    payment_method VARCHAR(50) NOT NULL, -- bKash, Nagad, Cash, Bank Transfer
    transaction_ref VARCHAR(100),
    description TEXT,
    attachment_url TEXT,
    status VARCHAR(30) DEFAULT 'DRAFT', -- DRAFT, UNDER_REVIEW, PUBLISHED, REJECTED, UNPUBLISHED
    created_by_user UUID REFERENCES users(id),
    reviewed_by_user UUID REFERENCES users(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_deposits_status ON deposits(status);
CREATE INDEX idx_deposits_member ON deposits(member_id);

-- 6. TEMPLE EXPENSES
CREATE TABLE expenses (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title VARCHAR(200) NOT NULL,
    category VARCHAR(100) NOT NULL, -- Puja, Renovation, Electricity, Bhog, Maintenance, Misc
    amount DECIMAL(12, 2) NOT NULL CHECK (amount > 0),
    expense_date VARCHAR(50) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    description TEXT,
    receipt_attachment_url TEXT,
    status VARCHAR(30) DEFAULT 'DRAFT', -- DRAFT, UNDER_REVIEW, PUBLISHED, REJECTED, UNPUBLISHED
    created_by_user UUID REFERENCES users(id),
    reviewed_by_user UUID REFERENCES users(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_expenses_status ON expenses(status);

-- 7. DONATIONS & ONLINE PAYMENT TRANSACTIONS
CREATE TABLE donations (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    receipt_number VARCHAR(100) UNIQUE NOT NULL,
    donor_name VARCHAR(150) NOT NULL,
    donor_phone VARCHAR(30),
    donor_email VARCHAR(255),
    amount DECIMAL(12, 2) NOT NULL CHECK (amount > 0),
    purpose VARCHAR(100) NOT NULL,
    payment_method VARCHAR(50) NOT NULL, -- bKash, Nagad, Bank, Card, Gateway
    transaction_ref VARCHAR(100) UNIQUE NOT NULL,
    status VARCHAR(30) DEFAULT 'SUCCESS', -- SUCCESS, PENDING, FAILED
    is_anonymous BOOLEAN DEFAULT FALSE,
    prayer_note TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_donations_receipt ON donations(receipt_number);
CREATE INDEX idx_donations_status ON donations(status);

-- 8. PAYMENT GATEWAY LOGS (SSLCOMMERZ / BKASH WEBHOOKS)
CREATE TABLE payment_transactions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    gateway_name VARCHAR(50) NOT NULL,
    gateway_session_id VARCHAR(200),
    transaction_id VARCHAR(200) UNIQUE NOT NULL,
    val_id VARCHAR(200),
    amount DECIMAL(12, 2) NOT NULL,
    currency VARCHAR(10) DEFAULT 'BDT',
    status VARCHAR(50) NOT NULL,
    raw_payload JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 9. NOTICES
CREATE TABLE notices (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title_bn VARCHAR(255) NOT NULL,
    title_en VARCHAR(255) NOT NULL,
    content_bn TEXT NOT NULL,
    content_en TEXT NOT NULL,
    publish_date VARCHAR(50) NOT NULL,
    attachment_url TEXT,
    is_urgent BOOLEAN DEFAULT FALSE,
    is_published BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 10. EVENTS & TEMPLE CALENDAR
CREATE TABLE events (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title_bn VARCHAR(255) NOT NULL,
    title_en VARCHAR(255) NOT NULL,
    date_str VARCHAR(100) NOT NULL,
    time_str VARCHAR(100) NOT NULL,
    location_bn VARCHAR(255) NOT NULL,
    location_en VARCHAR(255) NOT NULL,
    description_bn TEXT NOT NULL,
    description_en TEXT NOT NULL,
    image_url TEXT,
    is_upcoming BOOLEAN DEFAULT TRUE,
    is_published BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 11. GALLERY ITEMS (PHOTO & VIDEO)
CREATE TABLE gallery_items (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title_bn VARCHAR(255) NOT NULL,
    title_en VARCHAR(255) NOT NULL,
    album_name VARCHAR(150) NOT NULL,
    media_type VARCHAR(20) NOT NULL, -- IMAGE / VIDEO
    media_url TEXT NOT NULL,
    caption TEXT,
    is_published BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 12. COMPLAINT & FEEDBACK PORTAL
CREATE TABLE complaints (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tracking_id VARCHAR(50) UNIQUE NOT NULL,
    complainant_name VARCHAR(150),
    phone VARCHAR(30),
    email VARCHAR(255),
    subject VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    status VARCHAR(30) DEFAULT 'NEW', -- NEW, UNDER_REVIEW, RESOLVED, REJECTED
    admin_note TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_complaints_tracking ON complaints(tracking_id);

-- 13. SOCIAL MEDIA CHANNELS
CREATE TABLE social_links (
    platform_key VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    url TEXT DEFAULT '',
    is_enabled BOOLEAN DEFAULT FALSE,
    is_coming_soon BOOLEAN DEFAULT TRUE,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO social_links (platform_key, name, url, is_enabled, is_coming_soon) VALUES
('facebook', 'Facebook', '', false, true),
('youtube', 'YouTube', '', false, true),
('instagram', 'Instagram', '', false, true),
('whatsapp', 'WhatsApp', '', false, true),
('telegram', 'Telegram', '', false, true),
('tiktok', 'TikTok', '', false, true),
('twitter', 'X (Twitter)', '', false, true);

-- 14. SYSTEM AUDIT LOGS
CREATE TABLE audit_logs (
    id BIGSERIAL PRIMARY KEY,
    admin_role VARCHAR(100) NOT NULL,
    action VARCHAR(100) NOT NULL,
    target_entity VARCHAR(100) NOT NULL,
    target_id VARCHAR(100) NOT NULL,
    details TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_audit_created ON audit_logs(created_at DESC);

-- 15. SITE & THEME SETTINGS
CREATE TABLE site_settings (
    id INT PRIMARY KEY DEFAULT 1,
    temple_name_bn VARCHAR(200) DEFAULT 'চারটভাঙ্গা মধ্যপাড়া দুর্গা মন্দির',
    temple_name_en VARCHAR(200) DEFAULT 'Chartabhanga Madhyapara Durga Mandir',
    tagline_bn VARCHAR(200) DEFAULT '“ঐতিহ্য আমাদের, দায়িত্বও আমাদের।”',
    tagline_en VARCHAR(200) DEFAULT '“Our tradition, our responsibility.”',
    contact_phone VARCHAR(50) DEFAULT '01779760693',
    contact_email VARCHAR(100) DEFAULT 'shankhodhwoni@gmail.com',
    current_theme_mode VARCHAR(20) DEFAULT 'AUTO',
    language VARCHAR(10) DEFAULT 'bn'
);

INSERT INTO site_settings (id) VALUES (1);
