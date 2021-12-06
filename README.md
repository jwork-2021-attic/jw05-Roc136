[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-f059dc9a6f8d3a56e377f745f24479a46679e63a5d9fe6f495e02850cd0d8118.svg)](https://classroom.github.com/online_ide?assignment_repo_id=6266502&assignment_repo_type=AssignmentRepo)
# jw05

请将jw04的迷宫任务改造为一个ruguelike的葫芦娃与妖精两方对战游戏，游戏如下图所示。

![](image-11.jpeg)

需求如下：

- 每个生物体都是一个线程
- 每个生物体的移动、攻击等行为决策可使用Minimax或其他算法（可参考https://www.baeldung.com/java-minimax-algorithm）
- 请特别注意线程race condition（两个生物体不能占据同一个tile，对同一生物体的两个攻击行为应该先后发生作用，等）
- 请发挥想象力
- 完成后录屏发qq群或小破站


---

### 灵感来源

灵感来源于《元气骑士》，模拟《元气骑士》实现的躲弹幕游戏。

### 代码相关

+ 将原来的 `Creature` 类继承 `Thread` 类从而使得每个生物都是一个单独的线程，创建生物的同时将其提交到线程池执行，由此实现“每个生物体都是一个线程”；
+ 怪物或玩家移动时会读取地图上已存在的生物体，在 `World` 类中的获取生物的方法中添加锁机制，从而限制不同生物同时进入同一个地块。
+ `Bullet` 类虽继承于 `Creature` 类，但只是用到其中的方法，并未单独创建线程，而是由每个生物的 `BulletManager` 成员进行管理。
+ 地图存储于csv文件中，可以对地图进行拓展，同时方便地图的存取。
+ 在主线程接收到键盘事件时向 `KeyEventManager` 中添加键盘事件，`Player` 定时从中读取键盘事件并在读取后清空期间的其他无效按键。
