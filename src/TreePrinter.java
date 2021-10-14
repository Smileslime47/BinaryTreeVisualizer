import java.io.*;

class BinaryTree<T>{
    class Node{
        Node left=null,right=null;
        T data;

        Node(T d){data=d;}
        Node(T d, Node l, Node r){data=d;left=l;right=r;}
    }
    Node root;
}

/**
 * 一个Java的二叉树可视化矢量图生成器，基于Graphviz和Emden Gansner的tree.g脚本
 * @author smile_slime_47
 */
public class TreePrinter<Tree extends BinaryTree<?>> {

    //Powershell和Graphviz的环境变量路径，用分号隔开，路径参考：C:\Program Files\Graphviz\bin\;C:\Windows\System32\WindowsPowerShell\v1.0\
    static final String PATH="D:\\Program Files\\Graphviz\\bin\\;C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\";
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
    void printNode(BinaryTree<?>.Node n){
        if(n!=null){
            try {
                if(n.left!=null)out.write("    "+n.data+" -> "+n.left.data+";\n");
                if(n.right!=null)out.write("    "+n.data+" -> "+n.right.data+";\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            printNode(n.left);
            printNode(n.right);
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
            printNode(printObj.root);
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
            out.write("powershell.exe dot "+fileName+".dot | gvpr -c -f tree.gv | neato -n -Tsvg -o "+fileName+".svg\n");
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
            File del=new File(fileName+".bat");
            del.delete();
            del=new File(fileName+".dot");
            del.delete();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
