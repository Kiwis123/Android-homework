# Chapter-6 homework
班级：2017211309 学号：2017211450 姓名：郑奕伟
------
#### 一、pro版设计：
pro版相比基础版主要增加了四处：
1. layout中增加了RadioGroup，其中含有三个RadioButton，构成一个单选组，用于设置优先级；

   

2. 在”TODO 插入一条新数据，返回是否插入成功“这一项中，需要加入RadioButton的触发事件onClick的设置，保存对应优先级数据；对应的“TODO 从数据库中查询数据，并转换成 JavaBeans”这一项中，需要加入优先级level的数据访问，进而传给Note变量；

   

3. 在“TODO 从数据库中查询数据，并转换成 JavaBeans”中，我原本查询数据时将读取出的数据做一次反序，将最后更新的数据放在第一位，现在需要加入数据的排序，即根据优先级的设置（0>1>2）进行降序排序；


4. 最后就是根据不同优先级设置颜色，该段代码我添加在了NoteListAdapter中的onBindViewHolder中，根据当前view的pos和其对应的Note变量的level，利用一个NoteViewHolder变量设置其recyclerView的背景颜色，level=0设为红色，level=1设为绿色。

   

#### 二、基础版TODO完成情况
全部完成，具体如下：

1. TODO 定义表结构和SQL语句常量
			已实现，数据库除了ID之外定义了四列：
	
	+ content：输入内容
	+ time：输入时间
	+ level：优先级
	+ isdone：是否完成

	其他设置没有什么特殊的。
	
	
	
2. TODO 定义数据库名、版本；创建数据库
          正常设置，没有什么特殊的。

  

3. TODO 把一段文本写入某个存储区的文件中，再读出来，显示在fileText上
          老师已经实现了外部公有存储的方法，外部私有存储、内部存储的写入、读取方法都与老师已经实现的一样，唯一的区别在于目录的获取方式上，外部私有存储的目录获取方法为getExternalFilesDir(),内部存储的目录获取方法为getFilesDir()，然后设置具体的目录参数，按照老师的方法，进行内容的写入、读取，最后放在同一个线程中输出到UI上。

  

4. TODO 插入一条新数据，返回是否插入成功
          利用put方法将要插入的新数据放到ContentValues中，用db.insert()插入到数据库中，注意这里要处理的数据有两个，一个是传入的输入文本content，另一个需要自定义格式化好的Date变量记录当前时间。插入成功则返回值设为true。

  

5. TODO 从数据库中查询数据，并转换成 JavaBeans
          查询数据用db.query，然后利用cursor和List\<Note\>变量将查询到的数据一一保存到Note变量中，满足refresh函数的要求。

  

6. TODO 删除数据
          根据传入的Note变量的id利用db.delete()删除数据库中对应行。

  

7. TODO 更新数据
          根据传入的Note变量的id利用db.update()更新数据库中对应行的isdone列数据。