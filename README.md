# 一个二叉树的可视化库

# 环境
需要安装Graphviz

# 设置环境变量
```
static final String PATH=" "
```
这里要填写Powershell和Graphviz的路径，用分号分割，如C:\Program Files\Graphviz\bin\;C:\Windows\System32\WindowsPowerShell\v1.0\
Powershell一般为C:\Windows\System32\WindowsPowerShell\v1.0\
Graphviz为安装路径下的bin目录，如C:\Program Files\Graphviz\bin\

# 使用
需要先继承库中的BinaryTree接口

使用例：

public class Tree implements BinaryTree<Character,Tree.Node>

new TreePrinter<Tree,Tree.Node>