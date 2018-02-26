CREATE TABLE misc_table (
  id int(11) unsigned NOT NULL AUTO_INCREMENT,
  name varchar(125) DEFAULT NULL,
  date1 date DEFAULT NULL,
  datetime datetime DEFAULT NULL,
  time1 timestamp NULL DEFAULT NULL ,
  dou double DEFAULT NULL,
  flo float DEFAULT NULL,
  int1 int(11) DEFAULT NULL,
  bigint bigint(20) DEFAULT NULL,
  deci decimal(10,2) DEFAULT NULL,
  sint smallint(6) DEFAULT NULL,
  bi bit(1) DEFAULT NULL,
  def_time timestamp NULL ,
  def_date datetime ,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;