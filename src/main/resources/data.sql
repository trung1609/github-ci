-- ============================================================
-- data.sql - Dữ liệu mẫu tự động nạp khi khởi động ứng dụng
-- Sử dụng trong môi trường local và demo để có data ngay lập tức
-- ============================================================

INSERT INTO users (name, email, role, active, created_at, updated_at)
VALUES
  ('Nguyễn Công Hưởng',  'huong.nguyen@devops.edu.vn',  'LECTURER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('Đinh Thành Nam',     'nam.dinh@devops.edu.vn',      'LECTURER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('Trần Văn An',        'an.tran@student.edu.vn',      'STUDENT',  true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('Lê Thị Bình',        'binh.le@student.edu.vn',      'STUDENT',  true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('Phạm Minh Châu',     'chau.pham@student.edu.vn',    'STUDENT',  true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('Hoàng Đức Dũng',     'dung.hoang@student.edu.vn',   'STUDENT',  true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('Ngô Thị Emm',        'emm.ngo@student.edu.vn',      'STUDENT',  true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('System Admin',       'admin@devops.edu.vn',         'ADMIN',    true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
