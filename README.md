[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-f059dc9a6f8d3a56e377f745f24479a46679e63a5d9fe6f495e02850cd0d8118.svg)](https://classroom.github.com/online_ide?assignment_repo_id=6266502&assignment_repo_type=AssignmentRepo)
# jw06

请为jw05提供游戏保存功能，可包括但不限于：
- 地图保存/地图加载；
- 进度保存/进度恢复；
- 游戏过程录制/回放。

此外：
- 请使用maven进行所有第三方库的依赖管理和项目自动化构建；
- 请为自己的代码编写junit单元测试用例，代码测试覆盖率不低于50%（vscode请使用Coverage Gutters扩展，intellij IDEA请run with coverage）。


---

## 功能介绍：

1. 地图保存/加载

在主界面添加 `NEW MAP` 菜单，选择该菜单可以创建一个新地图，在旧地图上按下 `E` 键可以编辑该地图。

2. 进度保存/恢复

在游戏过程中按下 `X` 键可以保存当前游戏状态；在主界面 `LOAD RECORD` 可以恢复保存的状态。

3. 过程录制/回放

在游戏过程中按下 `R` 键开始记录游戏，再次按下 `R` 键结束记录，退出或游戏胜利是会自动保存记录。

在主界面添加 `LOAD RECORD` 菜单，进入后可以选择加载一项记录或保存的状态。

## 单元测试：

覆盖度见 [cov.xml](jwork05/cov.xml)