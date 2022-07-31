CREATE DATABASE training;
CREATE TABLE training.tblstudent (
	student_code varchar(10) NOT NULL unique,
    password varchar(50) NOT NULL,
    name varchar(50) NOT NULL,
    class varchar(20),
    address varchar(50),
    phone varchar(15),
    email varchar(50),
    gender bit,
    birthday date,
    parent_name varchar(50),
    parent_phone varchar(15),
    creationDate date,
    about varchar(255),
    profileImageUrl varchar(255),
    primary key (student_code)
);

CREATE TABLE training.tblpost (
	postId int NOT NULL unique auto_increment,
    creationDate datetime,
    score int,
    title varchar(255),
    body mediumtext,
    commentCount int,
    lastEditDate datetime,
    ownerStudentCode varchar(10),
    primary key (postid),
    foreign key (ownerStudentCode) references tblstudent(student_code)
);

CREATE TABLE training.tblcomment (
	id int NOT NULL unique auto_increment,
    postId int,
    creationDate datetime,
    score int,
    text mediumtext,
    commentCount int,
    lastEditDate datetime,
    ownerStudentCode varchar(10),
    parentId int,
    primary key (id),
    foreign key (postid) references tblpost(postid),
    foreign key (ownerStudentCode) references tblstudent(student_code)
);

CREATE TABLE training.tblvote (
	id int unique auto_increment,
    postId int,
    commentId int,
    student_code varchar(10),
    foreign key (postId) references tblpost(postId),
    foreign key (commentId) references tblcomment(id),
    foreign key (student_code) references tblstudent(student_code)
);

CREATE TABLE training.tbltag (
	id int unique auto_increment,
    tagName varchar(50),
    primary key(id)
);

CREATE TABLE training.tblpostTag (
	id int unique auto_increment,
    postId int,
    tagId int,
    count int,
    foreign key (postId) references tblpost(postId),
    foreign key (tagId) references tbltag(id)
);

CREATE TABLE training.tblnotification (
	id int unique auto_increment,
    postTitle varchar(255),
    commentText mediumtext,
    text mediumtext,
    status bit,
    student_code varchar(10),
    foreign key (student_code) references tblstudent(student_code)
);

insert into training.tblstudent (student_code, password, name, class, address, phone, email, gender, birthday, parent_name, parent_phone, creationDate, about, profileImageUrl) values('admin','1234','Admin','FITA-VNUA', null, null, null, null, null, null, null, null, null, '/images/profileImage/admin.svg');
insert into training.tblstudent (student_code, password, name, class, address, phone, email, gender, birthday, parent_name, parent_phone, creationDate, about, profileImageUrl) values('admin1234','1234','Admin1234','FITA-VNUA', null, null, null, null, null, null, null, null, null, '/images/profileImage/admin.svg');
insert into training.tblstudent (student_code, password, name, class, address, phone, email, gender, birthday, parent_name, parent_phone, creationDate, about, profileImageUrl) values('605121','1234','Vũ Minh Quang','K60THA','xã A, huyện B, tỉnh C','0909212199','buoncuoi9xvn@gmail.com',1,'1997/09/21', 'Vũ Văn Hoàng', '0912372673', '2022/03/13', 'Software Engineer tại công ty ', '/images/profileImage/male1.svg');
insert into training.tblstudent (student_code, password, name, class, address, phone, email, gender, birthday, parent_name, parent_phone, creationDate, about, profileImageUrl) values('630271','1234','Dương Thị Trâm','K63HA','xã A, huyện B, tỉnh C','0909212199','buoncuoi9xvn@gmail.com',0,'2002/06/16', 'Dương Văn Tân', '0828192726', '2022/02/15', null, '/images/profileImage/female1.svg');
insert into training.tblstudent (student_code, password, name, class, address, phone, email, gender, birthday, parent_name, parent_phone, creationDate, about, profileImageUrl) values('649881','1234','Trần Đình Mạnh','K64THA','xã A, huyện B, tỉnh C','0909212199','buoncuoi9xvn@gmail.com',1,'2003/03/08', 'Trần Văn Tính', '0781263162', '2022/03/13', null, '/images/profileImage/male6.svg');
insert into training.tblstudent (student_code, password, name, class, address, phone, email, gender, birthday, parent_name, parent_phone, creationDate, about, profileImageUrl) values('611282','1234','Sái Văn Quân','K61QLTT','xã A, huyện B, tỉnh C','0909212199','buoncuoi9xvn@gmail.com',1,'1999/03/12', 'Sái Văn Vương', '0812371738', '2022/02/13', null, '/images/profileImage/male5.svg');
insert into training.tblstudent (student_code, password, name, class, address, phone, email, gender, birthday, parent_name, parent_phone, creationDate, about, profileImageUrl) values('620121','1234','Nguyễn Kiều Trinh','K62HTTT','xã A, huyện B, tỉnh C','0909212199','buoncuoi9xvn@gmail.com',0,'2000/04/22', 'Nguyễn Văn Trung', '0826192175', '2022/03/13', null, '/images/profileImage/female3.svg');
insert into training.tblstudent (student_code, password, name, class, address, phone, email, gender, birthday, parent_name, parent_phone, creationDate, about, profileImageUrl) values('650212','1234','Đậu Văn Long','K65ATTT','xã A, huyện B, tỉnh C','0909212199','buoncuoi9xvn@gmail.com',1,'2003/10/28', 'Đậu Văn Hạnh', '0828261839', '2022/02/21', null, '/images/profileImage/male4.svg');
