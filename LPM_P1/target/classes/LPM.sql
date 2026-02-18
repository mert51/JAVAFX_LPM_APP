-- Mevcut tabloları temizleyelim (kod tarafındaki isimlerle uyumlu olacak şekilde)
DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS place;
DROP TABLE IF EXISTS student;

-- Ana Tablo: Öğrenciler
CREATE TABLE student (
    id         INTEGER PRIMARY KEY,
    name       VARCHAR(32),
    address    VARCHAR(32)
);

-- Ana Tablo: Mekanlar
CREATE TABLE place (
    id       INTEGER PRIMARY KEY,
    building VARCHAR(32),
    floor    VARCHAR(32),
    room     VARCHAR(32),
    seat     INTEGER
);

-- Junction Table: Rezervasyonlar
CREATE TABLE reservation (
    student_id INTEGER,
    place_id   INTEGER,
    date       DATE,
    duration   INTEGER,
    PRIMARY KEY (student_id, place_id, date),
    FOREIGN KEY (student_id) REFERENCES student (id),
    FOREIGN KEY (place_id) REFERENCES place (id)
);

-- Veri Girişi: student
INSERT INTO student (id, name, address)
VALUES (101, 'Ahmet Yılmaz', 'Bilgisayar Müh.'),
       (102, 'Ayşe Demir', 'Endüstri Müh.'),
       (103, 'Mehmet Çelik', 'Mimarlık'),
       (104, 'Zeynep Kara', 'Hukuk'),
       (105, 'Can Yıldız', 'Tıp Fakültesi');

-- Veri Girişi: place
INSERT INTO place (id, building, floor, room, seat)
VALUES (201, 'Merkez Kütüphane', 'Zemin Kat', 'Salon A', 1),
       (202, 'Merkez Kütüphane', 'Zemin Kat', 'Salon A', 2),
       (203, 'Merkez Kütüphane', '1. Kat', 'Sessiz Oda', 15),
       (204, 'Mühendislik B.', '3. Kat', 'Çalışma Lab.', 5),
       (205, 'Hukuk Binası', '2. Kat', 'Okuma Salonu', 12);

-- Veri Girişi: reservation (Örnek Kayıtlar)
INSERT INTO reservation (student_id, place_id, date, duration)
VALUES (101, 204, '2023-10-25', 2),
       (102, 201, '2023-10-25', 4),
       (104, 205, '2023-10-26', 3),
       (103, 203, '2023-10-26', 1);