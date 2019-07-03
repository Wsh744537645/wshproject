
***
    $ git update-index --assume-unchanged mpool/mpool-pool-sync/src/main/resources/application-prod.properties

    git update-index --no-assume-unchanged FILENAME

***
## nginx.config
```nginx
    server {
        listen 80;
        # gzip config
        gzip on;
        gzip_min_length 1k;
        gzip_comp_level 9;
        gzip_types text/plain text/css text/javascript application/json application/javascript application/x-javascript application/xml;
        gzip_vary on;
        gzip_disable "MSIE [1-6]\.";

        root /usr/share/nginx/html;

        location / {
            try_files $uri $uri/ /index.html;
        }

        location = /index.html {
            add_header Cache-Control "no-store";
        }

        location /apis {
            proxy_pass https://account-server:8081;
            proxy_set_header   X-Forwarded-Proto $scheme;
            proxy_set_header   Host              $http_host;
            proxy_set_header   X-Real-IP         $remote_addr;
        }
    }
```
***
## 矿机管理功能实现思路
* ### account_worker_group表结构
```sql
CREATE TABLE `account_worker_group` (
  `group_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '组id',
  `group_name` varchar(32) DEFAULT NULL COMMENT '组名',
  `group_seq` int(11) DEFAULT '0' COMMENT '组序号',
  `region_id` int(11) DEFAULT NULL COMMENT '区域iD',
  `status` varchar(10) DEFAULT NULL COMMENT '状态',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`group_id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4;

```
## 中间表
```sql
CREATE TABLE `account_worker__worker_group` (
  `worker_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `group_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`user_id`,`worker_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

```
* ### account_worker表结构
```sql
CREATE TABLE `account_worker` (
  `worker_id` bigint(20) NOT NULL COMMENT '矿工id',
  `user_id` bigint(20) NOT NULL COMMENT '用户iD',
  `region_id` int(11) NOT NULL COMMENT '区域Id',
  `worker_name` varchar(20) NOT NULL COMMENT '矿工名称',
  `last_share_time` datetime DEFAULT NULL,
  `worker_status` int(2) DEFAULT '0' COMMENT '0=活跃 1=不活跃 2=离线',
  `hash_accept_5m_t` double(8,3) DEFAULT '0.000',
  `hash_accept_15m_t` double(8,3) DEFAULT '0.000',
  `hash_reject_15m_t` double(8,3) DEFAULT '0.000',
  `hash_accept_1h_t` double(8,3) DEFAULT '0.000',
  `hash_reject_1h_t` double(8,3) DEFAULT '0.000',
  `rate_reject_1h` float(4,3) DEFAULT '0.000',
  `rate_reject_15m` float(4,3) DEFAULT '0.000',
  PRIMARY KEY (`worker_id`,`user_id`),
  UNIQUE KEY `worker_id` (`worker_id`,`user_id`,`region_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

```
* ### mining_workers表结构
```sql
CREATE TABLE `mining_workers` (
  `worker_id` bigint(20) NOT NULL,
  `puid` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  `worker_name` varchar(20) DEFAULT NULL,
  `accept_1m` bigint(20) NOT NULL DEFAULT '0',
  `accept_5m` bigint(20) NOT NULL DEFAULT '0',
  `accept_15m` bigint(20) NOT NULL DEFAULT '0',
  `reject_15m` bigint(20) NOT NULL DEFAULT '0',
  `accept_1h` bigint(20) NOT NULL DEFAULT '0',
  `reject_1h` bigint(20) NOT NULL DEFAULT '0',
  `accept_count` int(11) NOT NULL DEFAULT '0',
  `last_share_ip` char(16) NOT NULL DEFAULT '0.0.0.0',
  `last_share_time` timestamp NOT NULL DEFAULT '1970-01-01 00:00:01',
  `miner_agent` varchar(30) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  UNIQUE KEY `puid_worker_id` (`puid`,`worker_id`),
  KEY `puid_group_id` (`puid`,`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```
* ### 1 数据库 account_worker 加一个矿机状态的字段
* ### 2 canal从mining_workers Insert,Update,Delete SQL 语句
* ### 3 mpool-pool-sync 服务 读取Canal消息 解析 Insert,Update,Delete SQL 语句
         如果是Insert SQL 语句 说明当前有矿机新连接的矿机 ?(状态直接为活跃或者判断当前15分钟是否有算力[字段 accept_15m > 0] 是活跃)
         如果是Delete SQL 语句 说明当前矿机被删除则更新account_worker 字段[worker_status] 为删除状态
         如果是Update SQL语句 就需要判断当前 SQL 字段 accept_15m > 0 来更新account_worker 字段[worker_status] 对应的状态

***