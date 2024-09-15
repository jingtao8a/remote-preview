CREATE SCHEMA `remote-preview` ;

DROP TABLE `remote-preview`.`file_info`;
CREATE TABLE `remote-preview`.`file_info` (
  `file_id` VARCHAR(20) NOT NULL COMMENT '文件id',
  `file_pid` VARCHAR(20) NULL COMMENT '文件父id',
  `file_size` BIGINT(20) NULL COMMENT '文件大小',
  `file_name` VARCHAR(200) NULL COMMENT '文件名',
  `folder_type` INT NULL COMMENT '0:文件 1:目录',
  `file_type` INT NULL COMMENT '1:视频 2：音频 3：图片 4：pdf 5:doc  6:excel  7:txt 8:code 9:zip 10:其它',
  `status` INT NULL COMMENT '0:转码中 1：转码失败 2：转码成功',
  `file_cover` VARCHAR(100) NULL COMMENT '封面（图片、视频）',
  `file_path` VARCHAR(100) NULL COMMENT '文件路径',
  PRIMARY KEY (`file_id`),
  INDEX `idx_file_pid` (`file_pid` ASC) INVISIBLE);
