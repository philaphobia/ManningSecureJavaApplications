create table inventory(id int not null, name varchar(30), description varchar(100));
insert into inventory values(1,'Widget','Basic car widget');
insert into inventory values(2,'Widget 2','Advanced car widget');
create table schedule(id int not null, task_name varchar(30), description varchar(100));
insert into schedule values(1,'inventory','Count inventory');
insert into schedule values(2,'sales','Count sales');
create table docs(id int not null, title varchar(100), content varchar(100));
insert into docs values(1,'Widget repair manual','Take out the widget. Fix it. Put it back in.');
CREATE TABLE comments(id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (INCREMENT BY 1), comments VARCHAR(100));
