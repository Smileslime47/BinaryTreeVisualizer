import java.io.*;

/**
 * 二叉树接口
 * @param <T> 二叉树存储的数据类型
 * @param <N> 二叉树的结点类型
 * e.g. public class Tree implements BinaryTree<Character,Tree.Node>
 */
interface BinaryTree<T,N>{
    N getLeft(N n);
    N getRight(N n);
    N getRoot();
    /**
     * 返回结点用于显示的内容
     * @param n 结点
     * @return 结点存储的的数据类型
     */
    T getData(N n);
    /**
     * 仅仅用于区分各个结点，对于每个不同的结点返回一个互不相同的数字ID即可
     * @param n 结点
     * @return 返回结点ID
     */
    Integer getID(N n);
}

/**
 * 一个Java的二叉树可视化矢量图生成器，基于Graphviz
 * @param <Tree> 被打印树的类
 * @param <N> 被打印树的结点类
 * e.g. new TreePrinter<Tree,Tree.Node>
 * @author smile_slime_47
 */
public class TreePrinter<Tree extends BinaryTree<?, N>, N> {

    //Powershell和Graphviz的环境变量路径，用分号隔开，路径参考：C:\Program Files\Graphviz\bin\;C:\Windows\System32\WindowsPowerShell\v1.0\
    static final String PATH=" ";
    Tree printObj;
    BufferedWriter out;

    /**
     * 初始化TreePrinter，需要设置环境变量
     * @param t 打印树的对象
     * */
    TreePrinter(Tree t){printObj=t;}

    /**
     * 进行先序遍历生成结点的关联代码
     */
    void printNode(N n){
        if(n!=null){
            try {
                out.write(printObj.getID(n)+" [label=\""+printObj.getData(n)+"\"];\n");
                if(printObj.getLeft(n)!=null)out.write("    "+printObj.getID(n)+" -> "+printObj.getID(printObj.getLeft(n))+";\n");
                if(printObj.getRight(n)!=null)out.write("    "+printObj.getID(n)+" -> "+printObj.getID(printObj.getRight(n))+";\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            printNode(printObj.getLeft(n));
            printNode(printObj.getRight(n));
        }
    }

    /**
     * 生成Graphviz可读的文件
     */
    void spawnDot(String fileName){
        try {
            out = new BufferedWriter(new FileWriter(fileName+".dot"));
            out.write("""
                    digraph G {
                        nodesep=0.3;
                        ranksep=0.2;
                        margin=0.1;
                        node [shape=circle];
                        edge [arrowsize=0.8];
                    """);
            printNode(printObj.getRoot());
            out.write("}");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成可供运行的Bat文件，进而生成树的svg矢量图
     * 因为Java的Runtime.exec方法不支持带有符号的命令行，故转为Bat文件直接执行
     */
    void spawnBat(String fileName){
        try {
            out = new BufferedWriter(new FileWriter(fileName+".bat"));
            out.write("SET Path="+PATH+"\n");
            out.write("powershell.exe dot "+fileName+".dot | neato -n -Tsvg -o "+fileName+".svg\n");
            out.write("EXIT");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 运行Bat文件
     */
    void runBat(String fileName){
        try {
            Runtime.getRuntime().exec("cmd /c start "+fileName+".bat");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开生成的svg矢量图
     */
    void openImg(String fileName){
        try {
            Runtime.getRuntime().exec("cmd /c start "+fileName+".svg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void printTree(String fileName){
        try {
            spawnDot(fileName);
            spawnBat(fileName);
            runBat(fileName);
            //等待生成svg矢量图
            Thread.sleep(400);
            openImg(fileName);

            //删除过程中的中间文件，可以视情况注释掉这段内容
            Thread.sleep(200);
            File del=new File(fileName+".bat");
            del.delete();
            del=new File(fileName+".dot");
            del.delete();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
