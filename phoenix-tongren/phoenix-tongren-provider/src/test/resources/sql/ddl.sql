use `phoenix_file`
ALTER TABLE tb_file_index ADD fileType INT(1) DEFAULT '3' COMMENT '文件类型（1-图片，2-视频，3-附件）';
ALTER TABLE tb_file_index ADD thumbnailsPath VARCHAR(255) COMMENT '缩略图路径';
ALTER TABLE tb_file_index ADD isTranscoding INT(1) DEFAULT 0 COMMENT '是否转码（1-是，0-否）';

use `phoenix_knowledge`
ALTER TABLE tb_user_permission ADD ispush INT(1) DEFAULT 0 COMMENT '是否推送（1-是，0-否）';