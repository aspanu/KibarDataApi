insert into USERS (first_name, last_name, email, google_sub_id, company, position, user_type)
values
('Alice', 'Herianto', 'alice@kibar.id', 'sub1', 'Kibar', 'VP of Cool', 'PARTICIPANT'),
('Bob', 'Suparman', 'bob@kibar.id', 'sub1', 'Kibar', 'Managing Director of Badassery', 'PARTICIPANT'),
('Carol', 'Wulandari', 'carol@kibar.id', 'sub1', '1000 Startups', 'President of Awesome', 'PARTICIPANT'),
('Dave', 'Prasetyo', 'dave@kibar.id', 'sub1', 'DevNet', 'Lead Amazinger', 'ADMIN');

insert into ACTIVITIES (name, activity_date, description)
values
('PWA', '2017-9-12', 'Progressive Web App'),
('Hackathon', '2017-9-5', 'Hackathon with a theme of sports and well-being'),
('Career Fair','2017-9-6', 'Post hackathon career fair');
