import pymysql

db = pymysql.connect(host="172.28.0.4", user="appopr", password="123456", database="service_cipher"
                     , use_unicode=True, charset='utf8', cursorclass=pymysql.cursors.DictCursor)
# 使用 cursor() 方法创建一个游标对象 cursor
cursor = db.cursor()
# 使用 execute() 方法执行 SQL 查询
# res = cursor.execute("insert into t66y.FORUM_POSTS(TOPIC_URL, TOPIC_PARTITION, TOPIC_TITLE) VALUE ('2','2','2')")
# 使用 fetchone() 方法获取单条数据.
# data = cursor.fetchall()
cursor.execute("select * from t66y.FORUM_POSTS")
data = cursor.fetchall()
print("Database version : %s " % str(data))
# db.commit()
# 关闭数据库连接
cursor.close()
db.close()
