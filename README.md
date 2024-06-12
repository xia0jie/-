一个windows操作系统实现Mysql主从数据库的配置方法：
1.复制主数据库数据文件到一个新文件夹作为从数据库数据文件，同时删除从数据文件中的auto.cnf文件(这个文件没有的话，Mysql启动自动生成一个新的，同时具有了新的uuid)
2.复制一份Mysql配置文件my.ini，更改从服务端端口和从客户端连接默认端口为3307，mysqlid下设置service-id=101
3.主服务器配置文件新增service-id=100，log-bin=mysql-bin
4.主服务器正常启动
5.从服务器使用命令行启动 "D:\mysql-8.0.30-winx64\mysql\mysql-8.0.30-winx64\bin\mysqld.exe" --defaults-file="D:\mysql-8.0.30-winx64\mysql\mysql-copy\my.ini" --console 路径替换为自己的
6.主服务器创建一个角色作为从服务器连接后的角色，使用show master status;查看主服务器的File和Position，查看之后如果又进行了查询，文件会自动改变，需要重新查看。
7.从服务器客户端输入命令行指令 change  master to master_host='localhost',master_user='Tom',master_password='123456',master_log_file='mysql-bin.000001',master_log_pos=998;
8.验证输入 show slave status;如果返回字段中显示 Waiting for source to send event则表明配置成功
