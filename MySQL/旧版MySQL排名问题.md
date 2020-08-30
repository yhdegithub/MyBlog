MySql 8.0之后支持开窗函数 ，使得排名问题迎刃而解 ，但是并非所有公司都使用的最新的8之后的版本，这里给总结一下老版数据库是怎么完成同样的功能的

#### 排名问题本质：

排名问题，注释利用自己连接自己的全连接（t1 join t2），然后对t2的待排名属性进行筛选， 最后进行分类， 计数。比如下表，求解年龄排序：

 

![计算机生成了可选文字: 8 8 6 1 4 2 5 3 7 POWS name 赵阿Q 万倩 袁、洹 袁、洹 吴瑾婷 吴瑾婷 王大锤 周之人 1n age 49 38 33 33 24 24 19 18 06 job 全融 全融 全融 全融 SeC](file:///C:/Users/23712/AppData/Local/Temp/msohtmlclip1/01/clip_image001.png)

 

1、排名并列，且连续

select t1.* , 1+ count( distinct t2.age )  as myRank 

from test as t1 , test as t2

where t2.age > t1.age

groUP by t1.id

order by myRank

![计算机生成了可选文字: 6 1 4 2 5 3 7 7rows name 万倩 袁恒 袁恒 吴瑾婷 吴瑾婷 王大锤 周之人 1n age 38 33 24 24 19 18 。04 》myRank》 job 全融》 全融》 全融》](file:///C:/Users/23712/AppData/Local/Temp/msohtmlclip1/01/clip_image002.png)

值得注意的是，由于count(null)为0 ，所以对于最大age的属性，我们需要单独求解出来 ，然后再union

select * , 1 as myRank from test where age = (select max(age) from test);

![计算机生成了可选文字: job 1 name POWIn age 8》赵阿Q》》全融 @．04 》myRank》 1](file:///C:/Users/23712/AppData/Local/Temp/msohtmlclip1/01/clip_image003.png)

 

====> 换一种写法，用 <= 代替上面的< ,就可以不用写1 + count(distinct t2.age)...............

 

select t1.* , count(distinct t2.age) as myRank

from test as t1, test as t2

where t1.age <= t2.age

group by t1.id

order by myRank,t1.id;

![计算机生成了可选文字: job 全融 全融 全融 全融 8 8 6 1 4 2 5 3 7 POWS name 赵阿Q 万倩 袁、洹 袁恒 吴瑾婷 吴瑾婷 王大锤 周之人 In age 38 33 33 24 24 19 18 。04 myRank 1 2 3 3 4 4 5 6](file:///C:/Users/23712/AppData/Local/Temp/msohtmlclip1/01/clip_image004.png)

2、排名并列，且不连续

 

修改1中的distinct字段即可 ，理清<= 和distinct的那里（排名第一的特殊化处理，这里不重复叙述了）

 

select t1.* , 1+ count( t2.age) as myRank

from test as t1, test as t2

where t1.age < t2.age

group by t1.id

order by myRank,t1.id;

![计算机生成了可选文字: 》myRank》 6 1 4 2 5 3 7 name 万倩 袁、洹 袁、洹 吴瑾婷 吴瑾婷 王大锤 周之人 age 38 33 33 24 24 19 18 job 全融 全融 全融](file:///C:/Users/23712/AppData/Local/Temp/msohtmlclip1/01/clip_image005.png)

 

  

select t1.* ,1 as myRank from test as t1 where age = (select max(age) from test);     （union上面的结果即为完整结果）

![计算机生成了可选文字: 》myRank》 1 1 name 8》赵阿Q POWIn age 04 job SeC](file:///C:/Users/23712/AppData/Local/Temp/msohtmlclip1/01/clip_image006.png)

 

 

3、排名不并列（按照年纪， id两个属性排序.年龄相同则id小的更靠前），且连续

既然都不并列了，那肯定就连续的撒。

写法无非就是 1 中多增加一个判定条件，除了年纪更大 ，还可以年纪相同，但是id更小

（同样的排第一的特殊处理）

select t1.* , count(t2.age) as myRank

from test as t1, test as t2

where t1.age < t2.age or (t1.age = t2.age and t1.id > t2.id)

group by t1.id

order by myRank,t1.id;

+----+--------+-----+----

![计算机生成了可选文字: 》myRank》 6 1 4 2 5 3 7 name 万倩 袁、洹 袁、洹 吴瑾婷 吴瑾婷 王大锤 周之人 age 38 33 33 24 24 19 18 job 全融 全融 全融](file:///C:/Users/23712/AppData/Local/Temp/msohtmlclip1/01/clip_image007.png)

或者使用变量，这个最简单

 

1、(set @myRank := 0 )

 

2、select t1.* , (@myRank := @myRank +1 )

from test as t1

order by t1.age desc ,t1.id;

![计算机生成了可选文字: (@myRank。 8 6 4 2 3 7 name 赵阿Q 万倩 袁恒 王大锤 周之人 age 38 33 24 19 18 job 全融 全融 全融 @myRank +1凵 1 2 4 6 8](file:///C:/Users/23712/AppData/Local/Temp/msohtmlclip1/01/clip_image008.png)

 

4、排名不并列， 且不连续

都不并列了，那么排名数字肯定是连续的撒 ，所以这种情况是不存在的 ，不考虑





### 总结

总归的来说，排名问题就是：

##### 1、利用表的自连接，然后进行筛选t2中某个属性的数目

##### 2、利用变量 @NUm

##### 3、最简单的还是使用开窗函数

