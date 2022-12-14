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
insert into training.tblstudent (student_code, password, name, class, address, phone, email, gender, birthday, parent_name, parent_phone, creationDate, about, profileImageUrl) values('605121','1234','V?? Minh Quang','K60THA','x?? A, huy???n B, t???nh C','0909212199','buoncuoi9xvn@gmail.com',1,'1997/09/21', 'V?? V??n Ho??ng', '0912372673', '2022/03/13', 'Software Engineer t???i c??ng ty ', '/images/profileImage/male1.svg');
insert into training.tblstudent (student_code, password, name, class, address, phone, email, gender, birthday, parent_name, parent_phone, creationDate, about, profileImageUrl) values('630271','1234','D????ng Th??? Tr??m','K63HA','x?? A, huy???n B, t???nh C','0909212199','buoncuoi9xvn@gmail.com',0,'2002/06/16', 'D????ng V??n T??n', '0828192726', '2022/02/15', null, '/images/profileImage/female1.svg');
insert into training.tblstudent (student_code, password, name, class, address, phone, email, gender, birthday, parent_name, parent_phone, creationDate, about, profileImageUrl) values('649881','1234','Tr???n ????nh M???nh','K64THA','x?? A, huy???n B, t???nh C','0909212199','buoncuoi9xvn@gmail.com',1,'2003/03/08', 'Tr???n V??n T??nh', '0781263162', '2022/03/13', null, '/images/profileImage/male6.svg');
insert into training.tblstudent (student_code, password, name, class, address, phone, email, gender, birthday, parent_name, parent_phone, creationDate, about, profileImageUrl) values('611282','1234','S??i V??n Qu??n','K61QLTT','x?? A, huy???n B, t???nh C','0909212199','buoncuoi9xvn@gmail.com',1,'1999/03/12', 'S??i V??n V????ng', '0812371738', '2022/02/13', null, '/images/profileImage/male5.svg');
insert into training.tblstudent (student_code, password, name, class, address, phone, email, gender, birthday, parent_name, parent_phone, creationDate, about, profileImageUrl) values('620121','1234','Nguy???n Ki???u Trinh','K62HTTT','x?? A, huy???n B, t???nh C','0909212199','buoncuoi9xvn@gmail.com',0,'2000/04/22', 'Nguy???n V??n Trung', '0826192175', '2022/03/13', null, '/images/profileImage/female3.svg');
insert into training.tblstudent (student_code, password, name, class, address, phone, email, gender, birthday, parent_name, parent_phone, creationDate, about, profileImageUrl) values('650212','1234','?????u V??n Long','K65ATTT','x?? A, huy???n B, t???nh C','0909212199','buoncuoi9xvn@gmail.com',1,'2003/10/28', '?????u V??n H???nh', '0828261839', '2022/02/21', null, '/images/profileImage/male4.svg');
