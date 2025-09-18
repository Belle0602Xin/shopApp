-- 创建数据库
CREATE DATABASE IF NOT EXISTS online_shop CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE online_shop;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_email (email)
);

-- 商品表
CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    description TEXT NOT NULL,
    wholesale_price DECIMAL(10,2) NOT NULL,
    retail_price DECIMAL(10,2) NOT NULL,
    inventory INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_inventory (inventory),
    INDEX idx_description (description(100))
);

-- 订单表
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    order_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('PROCESSING', 'COMPLETED', 'CANCELED') NOT NULL DEFAULT 'PROCESSING',
    total_amount DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_order_time (order_time)
);

-- 订单项表
CREATE TABLE IF NOT EXISTS order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_order_id (order_id),
    INDEX idx_product_id (product_id)
);

-- 关注列表表
CREATE TABLE IF NOT EXISTS watchlist (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_product (user_id, product_id),
    INDEX idx_user_id (user_id),
    INDEX idx_product_id (product_id)
);

-- 插入管理员用户 (密码为: admin123，需要使用BCrypt加密)
INSERT INTO users (username, email, password, role) VALUES
('admin', 'admin@shop.com', '$2a$10$CwTycUXWue0Th2aD0.dWR.9n7SLZyOx6.w9ux.OlZc9Y7WjY7JG.O', 'ADMIN');

-- 插入示例商品
INSERT INTO products (description, wholesale_price, retail_price, inventory) VALUES
('苹果手机 iPhone 15', 4500.00, 5999.00, 50),
('三星手机 Galaxy S24', 3800.00, 4999.00, 30),
('华为笔记本 MateBook X', 5500.00, 6999.00, 20),
('小米电视 55寸', 2200.00, 2999.00, 15),
('戴尔显示器 27寸', 1200.00, 1599.00, 25),
('苹果耳机 AirPods Pro', 1200.00, 1599.00, 40),
('华为手表 Watch GT', 800.00, 1299.00, 35),
('联想键盘机械版', 350.00, 499.00, 60),
('罗技鼠标无线版', 180.00, 299.00, 80),
('索尼相机 Alpha 7', 8500.00, 11999.00, 10);

-- 插入测试用户 (密码为: user123)
INSERT INTO users (username, email, password, role) VALUES
('testuser', 'user@shop.com', '$2a$10$9/1bx6H4ZJ.vP7qSxNUc4.QhM4ZdUNR7Wg9JDJ0JbDbMWQ1Q1Q1Q1', 'USER');