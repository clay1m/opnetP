create table ACTIVITY
(
	id bigint auto_increment
		primary key,
	project_id bigint not null,
	name varchar(100) not null,
	type varchar(5) not null
)
engine=InnoDB charset=utf8
;

create index activity_project
	on ACTIVITY (project_id)
;

create table APP_USER
(
	id bigint auto_increment
		primary key,
	sso_id varchar(30) not null,
	first_name varchar(30) not null,
	last_name varchar(30) not null,
	email varchar(30) not null,
	constraint sso_id
		unique (sso_id)
)
engine=InnoDB charset=utf8
;

create table COMP_COST
(
	id bigint auto_increment
		primary key,
	duration_lb double not null,
	duration_ub double not null,
	compression_cost double not null,
	activity_id bigint not null,
	constraint comp_cost_activity
		foreign key (activity_id) references ACTIVITY (id)
			on update cascade on delete cascade
)
engine=InnoDB
;

create index comp_cost_activity
	on COMP_COST (activity_id)
;

create table DURATION_DIST
(
	id bigint auto_increment
		primary key,
	type varchar(10) not null,
	params varchar(200) not null,
	activity_id bigint not null,
	constraint activity
		foreign key (activity_id) references ACTIVITY (id)
			on update cascade on delete cascade
)
engine=InnoDB
;

create index activity
	on DURATION_DIST (activity_id)
;

create table GPR
(
	id bigint auto_increment
		primary key,
	name varchar(50) not null,
	type varchar(2) not null,
	delta double not null,
	left_activity_id bigint not null,
	right_activity_id bigint not null,
	constraint gpr_left_activity
		foreign key (left_activity_id) references ACTIVITY (id)
			on update cascade on delete cascade,
	constraint gpr_right_activity
		foreign key (right_activity_id) references ACTIVITY (id)
			on update cascade on delete cascade
)
engine=InnoDB
;

create index gpr_left_activity
	on GPR (left_activity_id)
;

create index gpr_right_activity
	on GPR (right_activity_id)
;

create table PROJECT
(
	id bigint auto_increment
		primary key,
	user_id bigint not null,
	name varchar(50) not null,
	constraint name
		unique (name),
	constraint project_user
		foreign key (user_id) references APP_USER (id)
			on update cascade on delete cascade
)
engine=InnoDB charset=utf8
;

create index project_user
	on PROJECT (user_id)
;

alter table ACTIVITY
	add constraint activity_project
		foreign key (project_id) references PROJECT (id)
			on update cascade on delete cascade
;

create table USER_DOCUMENT
(
	id bigint auto_increment
		primary key,
	user_id bigint not null,
	name varchar(100) not null,
	description varchar(255) null,
	type varchar(100) not null,
	content longblob not null,
	constraint document_user
		foreign key (user_id) references APP_USER (id)
			on update cascade on delete cascade
)
engine=InnoDB charset=utf8
;

create index document_user
	on USER_DOCUMENT (user_id)
;

