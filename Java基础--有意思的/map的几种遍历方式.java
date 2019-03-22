//介绍一下map的遍历方式

法一 : 对map中的keySet()值进行遍历 ，然后可以用 map.get()访问值  
     for(Integer k : map.keySet())   
	   sout(k + " ---> " + map.get(k));
   
法二 ： 迭代器  Iterator it = map.entrySet().iterator();


法三 ：   for-each ,最推荐使用，适合大规模数据
   for(Map.Entry<Integer,String> t : map.entrySet()){
	   
	   sout(t.getKey() + "--->" + t.getValue())
	   
   }
  

    