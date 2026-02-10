create table course_details(id char(36),
                            name varchar(255)unique not null,
                            duration double not null,
                            description varchar(255),
                            status char(50),
                            created_at datetime not null,
                            created_by varchar(255) not null,
                            updated_at datetime,
                            updated_by varchar(255),
                            primary key(id));

alter table courses rename course_details;

create table course_topics(id char(36),
                           name varchar(255),
                           status char(50),
                           course_id char(36) not null,
                           created_at datetime not null,
                           created_by varchar(255) not null,
                           updated_at datetime,
                           updated_by varchar(255),
                           primary key(id),
                           foreign key(course_id) references course_details(id));

create table course_sub_topic(id char(36),
                              name varchar(255),
                              status char(50),
                              topic_id char(36) not null,
                              created_at datetime not null,
                              created_by varchar(255) not null,
                              updated_at datetime,
                              updated_by varchar(255),
                              primary key(id),
                              foreign key(topic_id) references course_topics(id));


create table topic_materials(id char(36),
                             name varchar(255),
                             type varchar(255),
                             description varchar(255),
                             url varchar(255),
                             status char(50),
                             topic_id char(36) not null,
                             created_at datetime not null,
                             created_by varchar(255) not null,
                             updated_at datetime,
                             updated_by varchar(255),
                             primary key(id),
                             foreign key(topic_id) references course_topics(id));

create table sub_topic_materials(id char(36),
                                 name varchar(255) not null,
                                 type varchar(255) not null,
                                 description varchar(255),
                                 url varchar(255),
                                 status char(50),
                                 sub_topic_id char(36) not null,
                                 created_at datetime not null,
                                 created_by varchar(255) not null,
                                 updated_at datetime,
                                 updated_by varchar(255),
                                 primary key(id),
                                 foreign key(sub_topic_id) references course_sub_topic(id));


create table task_details(id char(36),
                          name varchar(255),
                          question varchar(225),
                          score integer,
                          status char(50),
                          topic_id char(36) not null,
                          created_at datetime not null,
                          created_by varchar(255) not null,
                          updated_at datetime,
                          updated_by varchar(255),
                          primary key(id),
                          foreign key(topic_id) references course_topics(id));

alter table task rename task_details;
create table batch_details(id char(36),
                           batch_name varchar(255) not null,
                           start_date date,
                           end_date date,
                           mentor_name varchar(255),
                           status char(50),
                           course_id char(36) not null,
                           created_at datetime not null,
                           created_by varchar(255)not null,
                           updated_at datetime,
                           updated_by varchar(255),
                           primary key(id),
                           foreign key(course_id) references course_details(id));


create table batch_week_details(id char(36),
                                week_name varchar(225),
                                status char(50),
                                batch_id char(36) not null,
                                created_at datetime not null,
                                created_by varchar(255) not null,
                                updated_at datetime,
                                updated_by varchar(255),
                                primary key(id),
                                foreign key(batch_id) references batch_details(id));


create table batch_week_topic_details(id char(36),
                                      week_id char(36) not null,
                                      topic_id char(36) not null,
                                      status char(50),
                                      created_at datetime not null,
                                      created_by varchar(255) not null,
                                      updated_at datetime,
                                      updated_by varchar(255),
                                      primary key(id),
                                      foreign key(week_id) references batch_week_details(id),
                                      foreign key(topic_id) references course_topics(id));

create table batch_week_schedule(id char(36),
                                 day char(50) not null unique,
                                 status char(36),
                                 in_time time not null,
                                 out_time time not null,
                                 hours char(36) not null,
                                 batch_id char(36) not null,
                                 created_at datetime not null,
                                 created_by varchar(255) not null,
                                 updated_at datetime,
                                 updated_by varchar(255),
                                 primary key(id),
                                 foreign key(batch_id) references batch_details(id));

create table batch_week_task(id char(36),
                             name varchar(255),
                             question varchar(225),
                             score integer,
                             status char(50),
                             week_id char(36) not null,
                             created_at datetime not null,
                             created_by varchar(255)not null,
                             updated_at datetime,
                             updated_by varchar(255),
                             primary key(id),
                             foreign key(week_id) references batch_week_details(id));

create table employee_course_enrollment_details(id char(36),
                                                emp_status char(50),
                                                emp_id char(36) not null,
                                                batch_id char(36) not null,
                                                created_at datetime not null,
                                                created_by varchar(255) not null,
                                                updated_at datetime,
                                                updated_by varchar(255),
                                                primary key(id),
                                                foreign key(emp_id) references employee_basic_info(id),
                                                foreign key(batch_id) references batch_details(id));

create table employee_task_result_details(id char(36),
                                          url varchar(255),
                                          snapshot varchar(255),
                                          document varchar(255),
                                          score integer,
                                          duration double,
                                          status char(50),
                                          enrollment_id char(36) not null,
                                          task_id char(36) not null,
                                          created_at datetime not null,
                                          created_by varchar(255) not null,
                                          updated_at datetime,
                                          updated_by varchar(255),
                                          primary key(id),
                                          foreign key(enrollment_id) references employee_course_enrollment_details(id),
                                          foreign key(task_id) references batch_week_task(id));


create table batch_material_completed_details(id char(36),
                                              status char(50),
                                              enrollment_id char(36) not null,
                                              topic_id char(36) not null,
                                              created_at datetime not null,
                                              created_by varchar(255) not null,
                                              updated_at datetime,
                                              updated_by varchar(255),
                                              primary key(id),
                                              foreign key(enrollment_id) references employee_course_enrollment_details(id),
                                              foreign key(topic_id) references course_topics(id));


alter table credientials change column phoneNumber  phone_number varchar(225);

create table user_details(id char(36),
                          name varchar(255),
                          phone_number varchar(255),
                          user_name varchar(225),
                          password varchar(255),
                          role varchar(255),
                          gender char(50),
                          status varchar(255),
                          created_at datetime,
                          created_by varchar(255),
                          updated_at datetime,
                          updated_by varchar(255),
                          employee_basic_info_id char(36),
                          primary key(id),
                          foreign key(employee_basic_info_id) references employee_basic_info(id));


create table employee_basic_info(id char(36),
                                 profile_img longblob,
                                 first_name varchar(225),
                                 middle_name varchar(225),
                                 last_name varchar(225),
                                 dob date,
                                 gender varchar(225),
                                 phone_number char(50) unique,
                                 alternate_number char(50)unique,
                                 email char(50) not null unique,
                                 pancard_number char(50),
                                 father_name varchar(225),
                                 mother_name varchar(225),
                                 created_at datetime not null,
                                 created_by varchar(255)not null,
                                 updated_at datetime,
                                 updated_by varchar(255),
                                 primary key(id));


create table course_enrollment_details(id char(36),
                                       emp_course_status char(36),
                                       start_date date,
                                       end_date date,
                                       emp_id char(36),
                                       course_id char(36),
                                       created_at datetime not null,
                                       created_by varchar(255) not null,
                                       updated_at datetime,
                                       updated_by varchar(255),
                                       primary key(id),
                                       foreign key(emp_id) references employee_basic_info(id),
                                       foreign key(course_id) references course_details(id));

create table employee_course_week_details(id char(36),
                                          status char(36),
                                          employee_enrollment_id char(36),
                                          course_week_id char(36),
                                          created_at datetime not null,
                                          created_by varchar(255) not null,
                                          updated_at datetime,
                                          updated_by varchar(255),
                                          primary key(id),
                                          foreign key(employee_enrollment_id) references course_enrollment_details(id),
                                          foreign key(course_week_id) references course_weeks(id));

create table employee_course_topic_details(id char(36),
                                           status char(36),
                                           employee_course_week_id char(36),
                                           course_topic_id char(36),
                                           created_at datetime not null,
                                           created_by varchar(255) not null,
                                           updated_at datetime,
                                           updated_by varchar(255),
                                           primary key(id),
                                           foreign key(employee_course_week_id) references employee_course_week_details(id),
                                           foreign key(course_topic_id) references course_topics(id));

create table employee_course_sub_topic_details(id char(36),
                                               status char(36),
                                               employee_course_topic_id char(36),
                                               course_sub_topic_id char(36),
                                               created_at datetime not null,
                                               created_by varchar(255) not null,
                                               updated_at datetime,
                                               updated_by varchar(255),
                                               primary key(id),
                                               foreign key(employee_course_topic_id) references employee_course_topic_details(id),
                                               foreign key(course_sub_topic_id) references course_sub_topic(id));

create table employee_course_task_details(id char(36),
                                          status char(36),
                                          employee_course_sub_topic_id char(36),
                                          course_task_id char(36),
                                          duration double,
                                          task_url varchar(255),
                                          snapshot longblob,
                                          submitted_on date,
                                          created_at datetime not null,
                                          created_by varchar(255) not null,
                                          updated_at datetime,
                                          updated_by varchar(255),
                                          primary key(id),
                                          foreign key(employee_course_sub_topic_id) references employee_course_sub_topic_details(id),
                                          foreign key(course_task_id) references course_tasks_details(id));





















































































//praveen

CREATE TABLE `ebrain_employee_desk`.`leave_request` (
                                                        `id` CHAR(36) NOT NULL,
                                                        `leave_type` CHAR(50) NULL,
                                                        `reason` VARCHAR(200) NULL,
                                                        `from_date` DATE NULL,
                                                        `end_date` DATE NULL,
                                                        `employee_id` CHAR(36) NULL,
                                                        `created_by` VARCHAR(150) NULL,
                                                        `created_at` DATETIME NULL,
                                                        `updated_by` VARCHAR(150) NULL,
                                                        `updated_at` DATETIME NULL,
                                                        PRIMARY KEY (`id`),
                                                        FOREIGN KEY (`employee_id`)
                                                            REFERENCES `ebrain_employee_desk`.`employee_basic_info` (`id`)
);
alter table leave_request add column leave_status char(50);


create table attendance(id char(36),date date,status char(50),reason_status char(50),
                        employee_id char(36),
                        `created_by` VARCHAR(150) NULL,
                        `created_at` DATETIME NULL,
                        `updated_by` VARCHAR(150) NULL,
                        `updated_at` DATETIME NULL,
                        primary key(id),
                        foreign key(employee_id) references employee_basic_info(id));
alter table attendance add column work_mode char(50);

create table attendance_timing(id char(36),in_time time,out_time time,attendance_id char(36),
                               `created_by` VARCHAR(150) NULL,
                               `created_at` DATETIME NULL,
                               `updated_by` VARCHAR(150) NULL,
                               `updated_at` DATETIME NULL,
                               primary key(id),
                               foreign key (attendance_id)references attendance(id));

create table permission (id char(36),start_time time, end_time time,reason char(250),
                         employee_id char(36),
                         `created_by` VARCHAR(150) NULL,
                         `created_at` DATETIME NULL,
                         `updated_by` VARCHAR(150) NULL,
                         `updated_at` DATETIME NULL,
                         primary key(id),
                         foreign key (employee_id) references employee_basic_info (`id`));

