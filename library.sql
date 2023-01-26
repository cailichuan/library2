/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80030
 Source Host           : localhost:3306
 Source Schema         : library

 Target Server Type    : MySQL
 Target Server Version : 80030
 File Encoding         : 65001

 Date: 09/01/2023 21:22:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin_account
-- ----------------------------
DROP TABLE IF EXISTS `admin_account`;
CREATE TABLE `admin_account`  (
  `account` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin_account
-- ----------------------------
INSERT INTO `admin_account` VALUES ('admin', 'b9d11b3be25f5a1a7dc8ca04cd310b28');

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book`  (
  `id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `title` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `author` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `number` int(0) NULL DEFAULT NULL,
  `class` int(0) NULL DEFAULT NULL,
  `img_format` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of book
-- ----------------------------
INSERT INTO `book` VALUES ('09579c14-3f25-4e0d-8351-e03b04dd7ca8', '哲学研究', '维特根斯坦', 10, 1, NULL);
INSERT INTO `book` VALUES ('0b4d3fb8-a1de-4ff7-8888-22bbf7a7f527', '西方哲学史', '斯通普夫', 101, 1, NULL);
INSERT INTO `book` VALUES ('10e064c0-f92d-4d92-90e4-236c42cb80dd', '史记', '司马迁', 101, 2, NULL);
INSERT INTO `book` VALUES ('130886ab-9da2-4276-9765-c70ca1999842', '现代性大屠杀', 'tom', 9, 1, NULL);
INSERT INTO `book` VALUES ('141b932f-ed78-4cc2-aeb0-63a9bbe8c0ab', '高等数学', '佚名', 101, 0, NULL);
INSERT INTO `book` VALUES ('1c71e439-234a-43d8-a884-56d45c5567bf', '新华字典', '佚名', 101, 0, NULL);
INSERT INTO `book` VALUES ('22124f57-ece9-40a3-95c9-3a78a9c87c2a', 'a', '小明', 9, 3, NULL);
INSERT INTO `book` VALUES ('25380d52-fab4-4187-b28d-a4e252c539b5', '大学英语', '佚名', 101, 0, NULL);
INSERT INTO `book` VALUES ('256f9945-ab3e-43ff-913a-e6ec48420b58', '11', '小明', 9, 0, NULL);
INSERT INTO `book` VALUES ('25e2ceeb-3f52-4570-861e-1bc6e83ad2a2', 'C语言', '佚名', 101, 0, NULL);
INSERT INTO `book` VALUES ('2b59ae89-e875-4840-b424-17bf6ce858ac', 'java', '佚名', 101, 0, NULL);
INSERT INTO `book` VALUES ('2dc32340-3a21-4ee4-a5bd-0ed2c1b7be73', '英语字典', '维特根斯坦', 101, 0, NULL);
INSERT INTO `book` VALUES ('30e2d26a-6ccb-4064-aa5d-ca9810c9aced', '4324', '小明', 130, 0, NULL);
INSERT INTO `book` VALUES ('32369a17-1808-4c75-adb7-b59d1ada2987', '莎士比亚全集', '莎士比亚', 101, 4, NULL);
INSERT INTO `book` VALUES ('353a77c8-8a01-46f6-81bb-927c4eba0f13', '2', '小明', 9, 0, NULL);
INSERT INTO `book` VALUES ('3e71631e-b429-42a6-bc05-10a041389382', '231', '小明', 20, 0, NULL);
INSERT INTO `book` VALUES ('3fc92759-1ee9-4fc3-b9ae-ad6821328e9d', '4453', '小明', 20, 0, NULL);
INSERT INTO `book` VALUES ('4088138d-39ae-4164-aac0-87a950b9663f', '山海经', '佚名', 101, 4, NULL);
INSERT INTO `book` VALUES ('4295aabc-1fdf-4fa0-887c-0c73aac00f96', '21', '小明', 212, 0, NULL);
INSERT INTO `book` VALUES ('4aa57a43-000f-4903-b4cc-e6715d253b6c', '古希腊神话', '佚名', 101, 4, NULL);
INSERT INTO `book` VALUES ('4ea822c6-ef68-445e-8a40-bc0acc8aeb4f', '儿童心理学', '佚名', 101, 2, NULL);
INSERT INTO `book` VALUES ('5952ba45-f6b8-45f3-a325-090b9c30e3c7', '厚黑学', '邓开文', 101, 2, NULL);
INSERT INTO `book` VALUES ('5a1dc87e-6bed-4358-9941-30422d496571', '212', '小明', 9, 0, NULL);
INSERT INTO `book` VALUES ('5b5ac8c7-bb2b-4ef6-8686-835b2d967860', '现代文学理论研究', '佚名', 101, 4, NULL);
INSERT INTO `book` VALUES ('60218088-fa69-4b01-b9a4-8bbb27d4d751', '323', '2323', 32, 0, NULL);
INSERT INTO `book` VALUES ('61e8635e-dff2-4f3c-99d4-60838245c707', '法律讲义', '张三', 101, 0, NULL);
INSERT INTO `book` VALUES ('6216df12-0e64-494a-97dc-21f298589dd4', '77', '小明', 212, 0, NULL);
INSERT INTO `book` VALUES ('69e9ec69-6bf7-4807-bf3e-f23c6a9a07ee', '5', '小明', 20, 0, NULL);
INSERT INTO `book` VALUES ('6ab7179c-09b7-4d8e-841f-aff010edfd6c', '依恋类型探究', '佚名', 101, 2, NULL);
INSERT INTO `book` VALUES ('7013a479-500d-4e33-bb6b-779c9c5f4b36', '心理学的危害', '维特根斯坦', 101, 2, NULL);
INSERT INTO `book` VALUES ('714bd575-349a-45c3-8ae0-a256feaa1ea8', '2323', '小明', 32, 0, NULL);
INSERT INTO `book` VALUES ('72410c24-3c6a-4dd9-a77c-4c324ca0d40c', '纯粹理性批判', '康德', 101, 1, NULL);
INSERT INTO `book` VALUES ('74a9bc07-1bb4-45f5-92bb-aeaf76f1a3f1', '上帝是否存在', '小红', 101, 1, NULL);
INSERT INTO `book` VALUES ('752bb659-96e4-4375-81ea-bd7129938dac', '嘿嘿', '但丁1', 101, 4, NULL);
INSERT INTO `book` VALUES ('77ac159b-a831-4625-b07c-b049aa2013fa', '我有圣杯', '邓开文', 101, 5, NULL);
INSERT INTO `book` VALUES ('7889f809-fd11-4bc1-a794-c5deab58b9ae', '61', '小明', 130, 0, NULL);
INSERT INTO `book` VALUES ('7b74ee8f-6cff-4d21-982c-02e5e7371988', '1221', '小明', 212, 0, NULL);
INSERT INTO `book` VALUES ('7c91f23a-e0fa-4f32-a601-2b5f3ddcf4cc', '要去哪', '邓开文', 101, 3, NULL);
INSERT INTO `book` VALUES ('7dc7f53c-274d-4e82-94a1-5018f4acf455', '3', '小明', 20, 0, NULL);
INSERT INTO `book` VALUES ('85990053-3948-402b-9f43-9e5535387a81', '三十天成为巴菲特', '邓开文', 101, 3, NULL);
INSERT INTO `book` VALUES ('8f44c08f-db3a-4e35-aa6a-51a98875f4bc', '成为马云', '邓开文', 101, 3, NULL);
INSERT INTO `book` VALUES ('8f81cb3e-32ca-4b56-8b00-f8751c03ff33', '利维坦', '佚名', 101, 1, NULL);
INSERT INTO `book` VALUES ('9025efb1-c185-4d28-9131-67f7909c05d6', '宗教与哲学', '佚名', 101, 1, NULL);
INSERT INTO `book` VALUES ('a7d0a68b-b409-431e-999b-94ccdb2fd988', '塔罗牌', '佚名', 101, 5, NULL);
INSERT INTO `book` VALUES ('abb8f2c6-28e7-405b-a6ed-d2cd2a6c06ba', '答案书', '佚名', 101, 5, NULL);
INSERT INTO `book` VALUES ('b4940b1c-e23f-4d9a-8a31-b86e9db25cf6', '群体心理学', '佚名', 101, 2, NULL);
INSERT INTO `book` VALUES ('b5179661-61e2-419a-bab2-86047603be2f', '乌合之众', '席勒', 101, 2, NULL);
INSERT INTO `book` VALUES ('bb23ee05-0041-47cc-87b9-c34e0ce693ad', '文化失忆', '汤姆', 101, 5, NULL);
INSERT INTO `book` VALUES ('bf3f6c27-0cdb-4b3e-a0a9-a2f467bfe9eb', '百年孤独', '马尔克斯', 101, 4, NULL);
INSERT INTO `book` VALUES ('c12f9133-c806-4700-99d9-7e203e560681', '121', '小明', 2121, 0, NULL);
INSERT INTO `book` VALUES ('ca8efdbe-1d51-4c56-b8e0-b360f7748441', '你为什么老是回避', '佚名', 101, 2, NULL);
INSERT INTO `book` VALUES ('cd0b20c2-b426-4c3b-99cd-f1bdbc37b0d5', '1212', '小明', 9, 0, NULL);
INSERT INTO `book` VALUES ('cd8dce65-44a3-4d4e-b6ac-c424f7cf2cad', 'mysql数据库原理2', '佚名', 101, 0, NULL);
INSERT INTO `book` VALUES ('d2ea8258-6590-44a5-8df0-490447405458', '演员的自我修养', '小周', 101, 0, NULL);
INSERT INTO `book` VALUES ('d35d726c-0dd9-4b82-ac41-e56831e67737', 'php', '佚名', 101, 0, NULL);
INSERT INTO `book` VALUES ('da01cf95-55da-46b9-8ee7-cc498ab82e6f', 'a', '小明', 9, 0, 'png');
INSERT INTO `book` VALUES ('db6befaa-4d73-483a-a2c1-665eca1129aa', '小逻辑', '黑格尔', 20, 1, NULL);
INSERT INTO `book` VALUES ('de96770f-0b55-41d6-ad8c-da523823eb96', '艺术鉴赏', '佚名', 101, 0, NULL);
INSERT INTO `book` VALUES ('e542d839-d73d-4a1b-8a9d-0d6815e8df8d', '1', 'tom', 9, 0, NULL);
INSERT INTO `book` VALUES ('ea211f71-d544-4d25-93c3-f21ead29e780', '故事会', 'xx出版社', 101, 4, NULL);
INSERT INTO `book` VALUES ('ec7c50ee-5394-4d87-9170-765fb76149c4', '哈利波特全集', '艾琳1', 101, 4, NULL);
INSERT INTO `book` VALUES ('f31119c3-cf14-4c1f-91e1-4020313bc74a', '11', '11', 111, 0, NULL);
INSERT INTO `book` VALUES ('f4582e90-131e-47ef-8ba6-ab5d68880bd3', 'c语言大师', '邓开文', 101, 0, NULL);

-- ----------------------------
-- Table structure for borrow_book
-- ----------------------------
DROP TABLE IF EXISTS `borrow_book`;
CREATE TABLE `borrow_book`  (
  `id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `gross` int(0) NULL DEFAULT NULL,
  `stock` int(0) NULL DEFAULT NULL,
  `lend` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of borrow_book
-- ----------------------------
INSERT INTO `borrow_book` VALUES ('0b4d3fb8-a1de-4ff7-8888-22bbf7a7f527', 101, 98, 3);

-- ----------------------------
-- Table structure for borrow_user
-- ----------------------------
DROP TABLE IF EXISTS `borrow_user`;
CREATE TABLE `borrow_user`  (
  `id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `u_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `b_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `borrow` int(0) NULL DEFAULT NULL,
  `act_number` int(0) NULL DEFAULT NULL,
  `date` datetime(0) NULL DEFAULT NULL,
  INDEX `u_id`(`u_id`) USING BTREE,
  INDEX `b_id`(`b_id`) USING BTREE,
  CONSTRAINT `borrow_user_ibfk_1` FOREIGN KEY (`u_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `borrow_user_ibfk_2` FOREIGN KEY (`b_id`) REFERENCES `book` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of borrow_user
-- ----------------------------
INSERT INTO `borrow_user` VALUES ('7017837134358917120', '24841eca-7dbd-4ab6-8287-239939b75e5b', '09579c14-3f25-4e0d-8351-e03b04dd7ca8', 1, 6, '2023-01-08 12:59:18');
INSERT INTO `borrow_user` VALUES ('7017837134358917120', '24841eca-7dbd-4ab6-8287-239939b75e5b', '09579c14-3f25-4e0d-8351-e03b04dd7ca8', 0, 6, '2023-01-08 12:59:34');
INSERT INTO `borrow_user` VALUES ('7017837551713136640', '24841eca-7dbd-4ab6-8287-239939b75e5b', '0b4d3fb8-a1de-4ff7-8888-22bbf7a7f527', 1, 6, '2023-01-08 13:00:57');
INSERT INTO `borrow_user` VALUES ('7017837551713136640', '24841eca-7dbd-4ab6-8287-239939b75e5b', '0b4d3fb8-a1de-4ff7-8888-22bbf7a7f527', 0, 1, '2023-01-08 13:01:07');
INSERT INTO `borrow_user` VALUES ('7017837551713136640', '24841eca-7dbd-4ab6-8287-239939b75e5b', '0b4d3fb8-a1de-4ff7-8888-22bbf7a7f527', 0, 5, '2023-01-08 13:01:31');
INSERT INTO `borrow_user` VALUES ('7017838842870902784', '24841eca-7dbd-4ab6-8287-239939b75e5b', '0b4d3fb8-a1de-4ff7-8888-22bbf7a7f527', 1, 6, '2023-01-08 13:06:05');
INSERT INTO `borrow_user` VALUES ('7017838842870902784', '24841eca-7dbd-4ab6-8287-239939b75e5b', '0b4d3fb8-a1de-4ff7-8888-22bbf7a7f527', 0, 3, '2023-01-08 13:06:12');

-- ----------------------------
-- Table structure for class
-- ----------------------------
DROP TABLE IF EXISTS `class`;
CREATE TABLE `class`  (
  `id` int(0) NOT NULL,
  `classname` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of class
-- ----------------------------
INSERT INTO `class` VALUES (0, '教育');
INSERT INTO `class` VALUES (1, '哲学');
INSERT INTO `class` VALUES (2, '心理学');
INSERT INTO `class` VALUES (3, '实用');
INSERT INTO `class` VALUES (4, '文学');
INSERT INTO `class` VALUES (5, '其它');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `name` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `t_n` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('0acbb103-723a-4624-9af5-44f277b58032', '三四', '13865901256');
INSERT INTO `user` VALUES ('0cd25a4e-ffc5-4d7e-9e83-55324fc68d55', '五六', '14263341533');
INSERT INTO `user` VALUES ('0f4d61b2-7a5c-48e4-8743-207c19ccfb31', '五三', '14279979041');
INSERT INTO `user` VALUES ('243525c4-a3a7-4a6c-8558-6e6e15e32aa9', '八二', '14651595411');
INSERT INTO `user` VALUES ('24841eca-7dbd-4ab6-8287-239939b75e5b', '五二', '14062769845');
INSERT INTO `user` VALUES ('2bcf48f4-7232-44c9-b6da-77ad178db1b9', '二三', '13938752988');
INSERT INTO `user` VALUES ('2e35e2d5-1e60-4769-a3e0-85bea2d39436', '九八', '14532537740');
INSERT INTO `user` VALUES ('30594213-25b8-4185-9e0a-6f419f54d01d', '七四', '13718812726');
INSERT INTO `user` VALUES ('3649022a-4aef-42fd-936a-e98c5835e218', '九三', '13858616529');
INSERT INTO `user` VALUES ('38c5d438-6650-424d-9e26-7d3f7220d584', '蔡李川', '13421204411');
INSERT INTO `user` VALUES ('3bcdeabb-e316-4c58-a9f9-5972ee2b5517', '一四', '14032452976');
INSERT INTO `user` VALUES ('3be6167b-f8eb-4742-aaca-545ef287c3fe', '四二', '14042086941');
INSERT INTO `user` VALUES ('3ebf3b81-87f1-4daf-b818-fc7b898ac8f1', '五七', '13794216651');
INSERT INTO `user` VALUES ('45b987e6-35c6-4be6-8db1-9192dbc9b33a', '六五', '14429414651');
INSERT INTO `user` VALUES ('46cc59c9-cefa-48ca-a364-83a1fb449c87', '六九', '14264702813');
INSERT INTO `user` VALUES ('484dc98e-e36b-4c22-95af-60d38b7d2acc', '七六', '14324159289');
INSERT INTO `user` VALUES ('4b4ed5ef-c933-4130-b84b-82b27d556726', '二二', '14223424892');
INSERT INTO `user` VALUES ('4cd44818-bc51-4d37-b629-82ff472099d3', '六七', '14598325911');
INSERT INTO `user` VALUES ('50e5aeb4-562b-42e4-a80b-6d56a14a4d5d', '二一', '14208277013');
INSERT INTO `user` VALUES ('570fff78-374b-44eb-85a7-d63d4ae7dbf1', '二六', '14465103438');
INSERT INTO `user` VALUES ('591641d1-9d15-4182-a390-117bdc6e6bf2', '三二', '14440192297');
INSERT INTO `user` VALUES ('5aa772cf-f2fa-40f2-80f2-db41d073b42b', '五八', '14441936330');
INSERT INTO `user` VALUES ('5cb59c50-d4a0-4f93-83b3-8bad37e9e4c6', '八九', '13679556112');
INSERT INTO `user` VALUES ('642621c8-56bf-482a-8fa1-679fdaadae24', '七二', '14758527858');
INSERT INTO `user` VALUES ('65b8ada6-6b8b-4175-96ac-798426b018e4', '伍六六', '13421204411');
INSERT INTO `user` VALUES ('6a1eab7a-9ffe-4de9-baaf-4a3e5bc5631e', '七五', '13932406141');
INSERT INTO `user` VALUES ('6c7754d0-5e63-4a39-a8c1-0ab4b81d0ace', '九一', '14376526301');
INSERT INTO `user` VALUES ('6ccf1b5a-4824-40ee-8bae-55b97c188193', '一五', '14696892320');
INSERT INTO `user` VALUES ('7017026637929783296', '蔡李川', '13421204411');
INSERT INTO `user` VALUES ('7017032194677940224', '蔡李川', '13421204411');
INSERT INTO `user` VALUES ('7017046668449292288', '蔡李川', '13421204411');
INSERT INTO `user` VALUES ('7017047157911986176', '蔡李川', '13421204411');
INSERT INTO `user` VALUES ('7017533548685041664', '蔡李川', '13421204411');
INSERT INTO `user` VALUES ('7018147191277625344', '蔡李川', '13421204411');
INSERT INTO `user` VALUES ('7404cab2-ef46-4328-8208-7519ce359c31', '一八', '13894909687');
INSERT INTO `user` VALUES ('78532389-6264-4eba-a7ab-3563574f9944', '三八', '13938191045');
INSERT INTO `user` VALUES ('7a692ad6-06bb-4504-9e84-8f95e6fa3b2c', '一六', '14242009720');
INSERT INTO `user` VALUES ('7cbf908d-7166-423e-8c07-cd8558c9f2cb', '伍六七', '13782340044');
INSERT INTO `user` VALUES ('7d0e0fe5-8732-4825-b98e-0bdbef86814b', '四三', '13957325458');
INSERT INTO `user` VALUES ('7e273a4f-475c-4aa5-8597-2c1108645cbf', '八八', '14287743311');
INSERT INTO `user` VALUES ('7f1c5554-0dc8-4abc-be95-072cd7145817', '三一', '13808561733');
INSERT INTO `user` VALUES ('80816d41-a6a6-41a8-9011-799b788ea3d9', '八四', '13996414259');
INSERT INTO `user` VALUES ('86436bca-2b5a-4edf-a1ee-2f3e41091395', '三三', '14023815364');
INSERT INTO `user` VALUES ('8695cb6a-c5e7-4b4e-b0ec-21c81f6d5ad0', '八一', '14131738506');
INSERT INTO `user` VALUES ('894c02ec-d38d-4d14-9f82-5c926d8e4a21', '一三', '13775452811');
INSERT INTO `user` VALUES ('89846f28-b393-4632-a6da-cab432ef68ca', '二九', '14665961809');
INSERT INTO `user` VALUES ('8bac94c5-7223-40f1-9931-e2bdb9cd5bb8', '六一', '14294202416');
INSERT INTO `user` VALUES ('8c78e35b-a60e-4a4d-8a21-687129baa324', '五五', '14601249345');
INSERT INTO `user` VALUES ('8e1662f4-9764-47f1-aaa5-b7cc58a7fbdc', '六四', '13919849639');
INSERT INTO `user` VALUES ('8ecabc14-8bc2-40c6-95b5-7f83f59e7103', '二八', '14751479781');
INSERT INTO `user` VALUES ('9a36ca56-79fa-47fa-910e-8e4e6af64adc', '三五', '13750315417');
INSERT INTO `user` VALUES ('9b36c713-ed3e-47bd-901e-7727219742cf', '九二', '13955350817');
INSERT INTO `user` VALUES ('a5c0b4e7-3862-4221-a7b1-092eec2a705d', '一七', '14077688058');
INSERT INTO `user` VALUES ('a643fc04-80cc-4617-bb1f-da6424144e4e', '六三', '13879115434');
INSERT INTO `user` VALUES ('a6f66566-f30c-421e-a4f5-60de7c01a39f', '四四', '13856722818');
INSERT INTO `user` VALUES ('a890365c-2938-456e-a9b9-a2a2109fae6f', '六二', '13625373071');
INSERT INTO `user` VALUES ('a9bba693-e686-41b9-8680-2ecf49114adc', '四一', '14540976274');
INSERT INTO `user` VALUES ('b116844c-23e5-4aaa-ab63-2a423f2e056c', '九七', '14097498958');
INSERT INTO `user` VALUES ('b542d758-a904-49a7-96fc-6770d0c14a17', '九六', '14728775562');
INSERT INTO `user` VALUES ('b904e8f7-dc1b-4676-b3e1-e1116c21b5dc', '七七', '13777597726');
INSERT INTO `user` VALUES ('bc55d1db-78b1-4ab9-a25d-22ad7310133b', '八五', '13944460978');
INSERT INTO `user` VALUES ('bc75c52c-b2c9-4fba-bbba-4012fa5e6930', '三七', '13886240229');
INSERT INTO `user` VALUES ('bfcce1c0-8cec-4e45-96f1-eb31f40dd3a3', '七八', '14142591313');
INSERT INTO `user` VALUES ('c0d21ebb-2446-4557-80d8-1750f3115116', '二七', '14729954259');
INSERT INTO `user` VALUES ('c1fc7294-534c-4556-afe4-738caaf9f507', '二四', '13960531824');
INSERT INTO `user` VALUES ('c4b1e5fb-9854-41a6-9aca-c4d29a4f8f6b', '一一', '14512205506');
INSERT INTO `user` VALUES ('c5306257-7520-4635-8c3c-d2ac4f217ccc', '八三', '13783053051');
INSERT INTO `user` VALUES ('c91c9cdc-2ef8-4c19-a44b-f40cf73f248e', '七一', '14120601038');
INSERT INTO `user` VALUES ('ce85d80c-9224-475b-ac54-a1ea335e343f', '七九', '14253928521');
INSERT INTO `user` VALUES ('d0e26639-4847-4552-8e4b-2cdf2a59b4b0', '六八', '13736807319');
INSERT INTO `user` VALUES ('d582bd65-34ad-419b-a011-003a2371d74a', '八七', '13674698507');
INSERT INTO `user` VALUES ('d5842ad8-b517-46c5-81ee-b2441945089d', '四八', '14472147238');
INSERT INTO `user` VALUES ('d68a1bd4-c54f-495d-be21-00efa63a2445', '八六', '14280943536');
INSERT INTO `user` VALUES ('d8f87049-5149-48fd-a39f-617094d93807', '三六', '14795621151');
INSERT INTO `user` VALUES ('de5e57fd-1e91-421e-8948-99068664eedb', '六六', '14027954448');
INSERT INTO `user` VALUES ('e17b7641-6c31-4c94-9e57-af4497a60ca2', '二五', '14442652444');
INSERT INTO `user` VALUES ('e1ab0608-17d9-4b57-b8e4-c2f5e69a3de5', '九五', '14361917169');
INSERT INTO `user` VALUES ('e260c57d-90a6-4d42-be5f-08b9e3e919c0', '七三', '14776567351');
INSERT INTO `user` VALUES ('e42dc8f1-4cc4-49aa-b06a-3c98f44ecc4d', '四五', '14253784359');
INSERT INTO `user` VALUES ('e9c3d034-85e7-4554-9a15-8740cdccec4f', '三九', '14312890097');
INSERT INTO `user` VALUES ('f21b5053-48bb-43a5-a0fa-3b8253f2128c', '四九', '13968442570');
INSERT INTO `user` VALUES ('f3ddff9b-e0e6-4813-8cbe-4a9fbed99e4a', '五四', '13794932757');
INSERT INTO `user` VALUES ('f7fea072-5bf8-47b9-b826-cd18e9506796', '四七', '14524183965');

-- ----------------------------
-- Table structure for user_account
-- ----------------------------
DROP TABLE IF EXISTS `user_account`;
CREATE TABLE `user_account`  (
  `u_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `account` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`u_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_account
-- ----------------------------
INSERT INTO `user_account` VALUES ('24841eca-7dbd-4ab6-8287-239939b75e5b', 'user', 'ad42c83ac4d3b86de14f207c46a0df0e');
INSERT INTO `user_account` VALUES ('7017026637929783296', 'cai', '7e4754e21eb3a93f5a3eb1e54a88f67f');
INSERT INTO `user_account` VALUES ('7017032194677940224', 'cai2', '8b8dc8e70fac70b1c6690c26c4a3d903');
INSERT INTO `user_account` VALUES ('7017046668449292288', 'cai3', '79419112c5c7de2eed21810fd8bae7e4');
INSERT INTO `user_account` VALUES ('7017047157911986176', 'cai31', '319f2a74626fd6478eee14b2b6099308');
INSERT INTO `user_account` VALUES ('7017533548685041664', 'cc', '1bddc1a2c3f1e97b94a50310ea94308c');

SET FOREIGN_KEY_CHECKS = 1;
